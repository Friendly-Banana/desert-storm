package org.gara.desertstorm.items;

import org.gara.desertstorm.DesertStorm;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class SandblasterMaterial implements Tier {

    public static final SandblasterMaterial INSTANCE = new SandblasterMaterial();

    @Override
    public int getUses() {
        return 1000;
    }

    @Override
    public float getSpeed() {
        return 5;
    }

    @Override
    public float getAttackDamageBonus() {
        return 0;
    }

    @Override
    public int getLevel() {
        return 2;
    }

    @Override
    public int getEnchantmentValue() {
        return 15;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(DesertStorm.SANDSTAR_ITEM);
    }
}