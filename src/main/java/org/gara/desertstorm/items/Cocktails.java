package org.gara.desertstorm.items;

import org.gara.desertstorm.mixin.PublicPotions;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;

public final class Cocktails {

    public static final Potion RADIOACTIVE_COCKTAIL;

    static {
        RADIOACTIVE_COCKTAIL = PublicPotions.register("radioactive",
                new Potion(new MobEffectInstance[] { new MobEffectInstance(MobEffects.NIGHT_VISION, 3600),
                        new MobEffectInstance(MobEffects.GLOWING, 3600) }));
    }
}