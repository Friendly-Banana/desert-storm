package org.gara.desertstorm;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class Utils {
	public static final String MOD_ID = "desertstorm";
	public static final int SAND_COLOR = 16769311;
	private static int counter = 0;

	public static void Debug() {
		++counter;
		Log(counter);
	}

	public static void Log(Object... objects) {
		StringBuilder str = new StringBuilder();
		for (Object object : objects) {
			str.append(object.toString());
			str.append(", ");
		}
		System.out.println(MOD_ID + str.substring(0, str.length() - 2));
	}

	public static TranslatableComponent GetTooltip(String itemName, Object... objects) {
		return new TranslatableComponent("item." + MOD_ID + "." + itemName + ".tooltip", objects);
	}

	public static ResourceLocation NewIdentifier(String name) {
		return new ResourceLocation(MOD_ID, name);
	}

	public static boolean IsSurvival(Player player) {
		return !player.isCreative() && !player.isSpectator();
	}
}