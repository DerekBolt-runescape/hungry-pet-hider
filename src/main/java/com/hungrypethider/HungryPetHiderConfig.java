package com.hungrypethider;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(HungryPetHiderConfig.GROUP)
public interface HungryPetHiderConfig extends Config
{
	String GROUP = "hungrypethider";

	@ConfigItem(
		keyName = "followerName",
		name = "Only hide followers named",
		description = "The plugin only hides your follower if its name contains this text (not case sensitive). Leave blank to hide any follower.",
		position = 0
	)
	default String followerName()
	{
		return "Labrador puppy";
	}

	@ConfigItem(
		keyName = "hungryPattern",
		name = "Hungry message (regex)",
		description = "Game message that means your pet is hungry. Your pet is shown when this appears.",
		position = 1
	)
	default String hungryPattern()
	{
		return "Your puppy is very hungry.";
	}

	@ConfigItem(
		keyName = "fedPattern",
		name = "Fed message (regex)",
		description = "Game message that means your pet has been fed. Your pet is hidden when this appears.",
		position = 2
	)
	default String fedPattern()
	{
		return "";
	}

	@ConfigItem(
		keyName = "itemOnPetCountsAsFed",
		name = "Item used on pet = fed",
		description = "Treat using any item on your follower as feeding it. Handy if there is no fed message.",
		position = 3
	)
	default boolean itemOnPetCountsAsFed()
	{
		return true;
	}

	@ConfigItem(
		keyName = "notifyWhenHungry",
		name = "Notify when hungry",
		description = "Send a RuneLite notification when your pet becomes hungry",
		position = 4
	)
	default boolean notifyWhenHungry()
	{
		return true;
	}

	@ConfigItem(
		keyName = "debugMessages",
		name = "Debug chat messages",
		description = "Print a chat line whenever the plugin changes your pet's hunger state",
		position = 5
	)
	default boolean debugMessages()
	{
		return false;
	}
}
