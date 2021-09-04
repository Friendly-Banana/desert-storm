package org.gara.desertstorm.item;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import org.gara.desertstorm.DesertStorm;

public class SandblasterMaterial implements ToolMaterial {

    public static final SandblasterMaterial INSTANCE = new SandblasterMaterial();

    @Override
    public int getDurability() {
        return 1000;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 5;
    }

    @Override
    public float getAttackDamage() {
        return 0;
    }

    @Override
    public int getMiningLevel() {
        return 2;
    }

    @Override
    public int getEnchantability() {
        return 15;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(DesertStorm.SANDSTAR_ITEM);
    }
}