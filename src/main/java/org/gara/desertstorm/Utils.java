package org.gara.desertstorm;

import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class Utils {
	public static final String MOD_ID = "desertstorm";

	public static TranslatableText GetTooltip(String itemName) {
		return new TranslatableText("item." + MOD_ID + "." + itemName + ".tooltip");
	}

	public static Identifier NewIdentifier(String name) {
		return new Identifier(MOD_ID, name);
	}
}