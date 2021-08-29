package org.gara.desertstorm.items;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.potion.Potion;
import net.minecraft.util.registry.Registry;
import org.gara.desertstorm.Utils;

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
                new Potion(new StatusEffectInstance[] { new StatusEffectInstance(StatusEffects.NIGHT_VISION, 3600),
                        new StatusEffectInstance(StatusEffects.GLOWING, 3600) }));
        DISLOCATOR = register("dislocator",
                new Potion(new StatusEffectInstance[] { new StatusEffectInstance(StatusEffects.GLOWING, 3600) }));
        ICECOLD = register("icecold",
                new Potion(new StatusEffectInstance[] { new StatusEffectInstance(StatusEffects.GLOWING, 3600) }));
        MIDAS_SPECIAL = register("midas_special",
                new Potion(new StatusEffectInstance[] { new StatusEffectInstance(StatusEffects.GLOWING, 3600) }));
        MOLOTOV = register("molotov",
                new Potion(new StatusEffectInstance[] { new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 3600) }));
        // Minecraft Effects
        SUNRISE = register("sunrise",
                new Potion(new StatusEffectInstance[] { new StatusEffectInstance(StatusEffects.GLOWING, 3600) }));
        SUNSET = register("sunset",
                new Potion(new StatusEffectInstance[] { new StatusEffectInstance(StatusEffects.ABSORPTION, 3600),
                        new StatusEffectInstance(StatusEffects.REGENERATION, 1800) }));
        HOT_COCOA = register("hot_cocoa",
                new Potion(new StatusEffectInstance[] { new StatusEffectInstance(StatusEffects.SPEED, 30),
                        new StatusEffectInstance(StatusEffects.HASTE, 30) }));
        HEALTHY_SMOOTHIE = register("healthy_smoothie",
                new Potion(new StatusEffectInstance[] { new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 10, 5) }));
        MULTIVITAMINE = register("multivitamine", new Potion(new StatusEffectInstance[] {
                new StatusEffectInstance(StatusEffects.NIGHT_VISION, 3600), new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 10, 5) }));
    }
}