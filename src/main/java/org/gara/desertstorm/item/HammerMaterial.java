package org.gara.desertstorm.item;

import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class HammerMaterial implements ToolMaterial {
	public static final HammerMaterial INSTANCE = new HammerMaterial();

	@Override
	public int getDurability() {
		return 500;
	}

	@Override
	public float getMiningSpeedMultiplier() {
		return 5;
	}

	@Override
	public float getAttackDamage() {
		return 2;
	}

	@Override
	public int getMiningLevel() {
		return 3;
	}

	@Override
	public int getEnchantability() {
		return 30;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return Ingredient.ofItems(Items.IRON_INGOT);
	}
}