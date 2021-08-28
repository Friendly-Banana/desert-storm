package org.gara.desertstorm.items;

import org.gara.desertstorm.Utils;

import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;

public final class Cocktails {

    public static final Potion RADIOACTIVE_COCKTAIL;
    public static final Potion DISLOCATOR;
    public static final Potion SUNRISE;
    public static final Potion SUNSET;
    public static final Potion ICECOLD;
    public static final Potion HOT_COCOA;
    public static final Potion HEALTHY_SMOOTHIE;
    public static final Potion MULTIVITAMINE;
    public static final Potion MIDAS_SPECIAL;
    public static final Potion MOLOTOV;

    public static Potion register(String string, Potion potion) {
        return Registry.register(Registry.POTION, Utils.MOD_ID + ":" + string, potion);
    }

    static {
        // Special Effects
        RADIOACTIVE_COCKTAIL = register("radioactive",
                new Potion(new MobEffectInstance[] { new MobEffectInstance(MobEffects.NIGHT_VISION, 3600),
                        new MobEffectInstance(MobEffects.GLOWING, 3600) }));
        DISLOCATOR = register("dislocator",
                new Potion(new MobEffectInstance[] { new MobEffectInstance(MobEffects.GLOWING, 3600) }));
        ICECOLD = register("icecold",
                new Potion(new MobEffectInstance[] { new MobEffectInstance(MobEffects.GLOWING, 3600) }));
        MIDAS_SPECIAL = register("midas_special",
                new Potion(new MobEffectInstance[] { new MobEffectInstance(MobEffects.GLOWING, 3600) }));
        MOLOTOV = register("molotov",
                new Potion(new MobEffectInstance[] { new MobEffectInstance(MobEffects.HARM, 3600) }));
        // Minecraft Effects
        SUNRISE = register("sunrise",
                new Potion(new MobEffectInstance[] { new MobEffectInstance(MobEffects.GLOWING, 3600) }));
        SUNSET = register("sunset",
                new Potion(new MobEffectInstance[] { new MobEffectInstance(MobEffects.ABSORPTION, 3600),
                        new MobEffectInstance(MobEffects.REGENERATION, 1800) }));
        HOT_COCOA = register("hot_cocoa",
                new Potion(new MobEffectInstance[] { new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 30),
                        new MobEffectInstance(MobEffects.DIG_SPEED, 30) }));
        HEALTHY_SMOOTHIE = register("healthy_smoothie",
                new Potion(new MobEffectInstance[] { new MobEffectInstance(MobEffects.HEAL, 10, 5) }));
        MULTIVITAMINE = register("multivitamine", new Potion(new MobEffectInstance[] {
                new MobEffectInstance(MobEffects.NIGHT_VISION, 3600), new MobEffectInstance(MobEffects.HEAL, 10, 5) }));
    }
}