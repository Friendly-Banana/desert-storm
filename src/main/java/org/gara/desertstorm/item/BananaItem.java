package org.gara.desertstorm.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

public class BananaItem extends CustomItem {
    public static final FoodComponent FOOD_PROPERTIES = (new FoodComponent.Builder()).hunger(3).saturationModifier(2.5F)
            .statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 30 * 20, 0), 1.0F).alwaysEdible().build();

    public BananaItem(Settings properties) {
        super("banana", properties);
    }
}