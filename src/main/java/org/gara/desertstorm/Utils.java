package org.gara.desertstorm;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class Utils {
	public static final String MOD_ID = "desertstorm";
	public static final LogLevel LOG_LEVEL = LogLevel.INFO;

	public static void Log(String log) {
		Log(log, LogLevel.INFO);
	}

	public static void Log(String log, LogLevel level) {
		if (level.compareTo(LOG_LEVEL) >= 0) {
			System.out.println(MOD_ID + "[" + level.toString() + "]: " + log);
		}
	}

	public static boolean IsCreative(Player player) {return player.getAbilities().instabuild;}

	public static TranslatableComponent GetTooltip(String itemName) {
		return new TranslatableComponent("item." + MOD_ID + "." + itemName + ".tooltip");
	}

	public static ResourceLocation NewIdentifier(String name) {
		return new ResourceLocation(MOD_ID, name);
	}
}