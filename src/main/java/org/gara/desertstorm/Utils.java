package org.gara.desertstorm;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class Utils {
    public static final String MOD_ID = "desertstorm";
    public static final int GOLD = 0xffd700, SAND = 0xffe11f;

    private static int counter = 0;

    public static void Debug() {
        ++counter;
        System.out.println(StackWalker.getInstance().walk(stream -> stream.skip(1).findFirst().get()).getMethodName() + ": " + counter);
    }

    public static void Log(Object... objects) {
        StringBuilder str = new StringBuilder();
        for (Object object : objects) {
            str.append(object.toString());
            str.append(", ");
        }
        System.out.println(str.substring(0, str.length() - 2));
    }

    public static TranslatableText GetTooltip(String itemName, Object... objects) {
        return new TranslatableText("item." + MOD_ID + "." + itemName + ".tooltip", objects);
    }

    public static Identifier NewIdentifier(String name) {
        return new Identifier(MOD_ID, name);
    }

    public static boolean IsSurvival(PlayerEntity player) {
        return !player.isCreative() && !player.isSpectator();
    }
}