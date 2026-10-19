package com.hungrypethider;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class HungryPetHiderPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(HungryPetHiderPlugin.class);
		RuneLite.main(args);
	}
}
