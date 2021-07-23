package org.gara.desertstorm;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class SandblasterMaterial implements ToolMaterial {

    public static final SandblasterMaterial INSTANCE = new SandblasterMaterial();
    
    @Override
    public float getAttackDamage() {
        return 3.0f;
    }

    @Override
    public int getDurability() {
        return 1000;
    }

    @Override
    public int getEnchantability() {
        return 15;
    }

    @Override
    public int getMiningLevel() {
        return 2;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 5.0f;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(DesertStorm.SANDSTAR_ITEM);
    }
}