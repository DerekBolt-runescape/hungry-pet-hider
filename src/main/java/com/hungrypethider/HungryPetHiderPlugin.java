package com.hungrypethider;

import com.google.inject.Provides;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuAction;
import net.runelite.api.NPC;
import net.runelite.api.Renderable;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.Notifier;
import net.runelite.client.callback.Hooks;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;

@Slf4j
@PluginDescriptor(
	name = "Hungry Pet Hider",
	description = "Hides your follower while it is fed and shows it again when it gets hungry",
	tags = {"pet", "follower", "hide", "hunger", "dog"}
)
public class HungryPetHiderPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private Hooks hooks;

	@Inject
	private HungryPetHiderConfig config;

	@Inject
	private Notifier notifier;

	@Inject
	private ChatMessageManager chatMessageManager;

	private final Hooks.RenderableDrawListener drawListener = this::shouldDraw;

	private Pattern hungryPattern;
	private Pattern fedPattern;

	// Start "hungry" (visible) so the pet is never hidden until we have seen it get fed.
	private volatile boolean petHungry = true;

	@Provides
	HungryPetHiderConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(HungryPetHiderConfig.class);
	}

	@Override
	protected void startUp()
	{
		compilePatterns();
		petHungry = true;
		hooks.registerRenderableDrawListener(drawListener);
	}

	@Override
	protected void shutDown()
	{
		hooks.unregisterRenderableDrawListener(drawListener);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (HungryPetHiderConfig.GROUP.equals(event.getGroup()))
		{
			compilePatterns();
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		// After logging out we cannot know the hunger state, so default back to visible.
		if (event.getGameState() == GameState.LOGIN_SCREEN || event.getGameState() == GameState.HOPPING)
		{
			petHungry = true;
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		ChatMessageType type = event.getType();
		if (type != ChatMessageType.GAMEMESSAGE && type != ChatMessageType.SPAM)
		{
			return;
		}

		String message = Text.removeTags(event.getMessage());

		// Check hungry first so a message like "...is very hungry" can never count as fed.
		if (hungryPattern != null && hungryPattern.matcher(message).find())
		{
			setHungry(true);
		}
		else if (fedPattern != null && fedPattern.matcher(message).find())
		{
			setHungry(false);
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (!config.itemOnPetCountsAsFed() || event.getMenuAction() != MenuAction.WIDGET_TARGET_ON_NPC)
		{
			return;
		}

		NPC target = event.getMenuEntry().getNpc();
		NPC follower = client.getFollower();
		if (target != null && follower != null && target.getIndex() == follower.getIndex())
		{
			setHungry(false);
		}
	}

	private void setHungry(boolean hungry)
	{
		if (petHungry == hungry)
		{
			return;
		}

		petHungry = hungry;

		if (hungry && config.notifyWhenHungry())
		{
			notifier.notify("Your pet is hungry!");
		}

		if (config.debugMessages())
		{
			chatMessageManager.queue(QueuedMessage.builder()
				.type(ChatMessageType.CONSOLE)
				.runeLiteFormattedMessage("[Hungry Pet Hider] Pet is now " + (hungry ? "hungry (shown)" : "fed (hidden)"))
				.build());
		}
	}

	boolean shouldDraw(Renderable renderable, boolean drawingUI)
	{
		if (petHungry || !(renderable instanceof NPC))
		{
			return true;
		}

		NPC follower = client.getFollower();
		if (follower == null || renderable != follower)
		{
			return true;
		}

		// Only hide while the follower matches the name filter (e.g. still a puppy).
		return !matchesFollowerName(follower);
	}

	private boolean matchesFollowerName(NPC follower)
	{
		String filter = config.followerName();
		if (filter == null || filter.trim().isEmpty())
		{
			return true;
		}

		String name = follower.getName();
		return name != null && Text.removeTags(name).toLowerCase().contains(filter.trim().toLowerCase());
	}

	private void compilePatterns()
	{
		hungryPattern = compile(config.hungryPattern(), "hungry");
		fedPattern = compile(config.fedPattern(), "fed");
	}

	private Pattern compile(String regex, String label)
	{
		if (regex == null || regex.trim().isEmpty())
		{
			return null;
		}

		try
		{
			return Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		}
		catch (PatternSyntaxException e)
		{
			log.warn("Invalid {} regex: {}", label, regex, e);
			return null;
		}
	}
}
