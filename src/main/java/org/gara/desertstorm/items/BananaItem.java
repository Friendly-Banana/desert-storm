package org.gara.desertstorm.items;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;

public class BananaItem extends CustomItem {
    public static final FoodProperties FOOD_PROPERTIES = (new FoodProperties.Builder()).nutrition(3).saturationMod(2.5F)
            .effect(new MobEffectInstance(MobEffects.ABSORPTION, 30 * 20, 0), 1.0F).alwaysEat().build();

    public BananaItem(Properties properties) {
        super("banana", properties);
    }
}