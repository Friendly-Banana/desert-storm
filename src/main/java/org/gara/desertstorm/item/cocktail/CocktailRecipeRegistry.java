package org.gara.desertstorm.item.cocktail;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import org.gara.desertstorm.DesertStorm;
import org.gara.desertstorm.Utils;

import java.util.ArrayList;
import java.util.List;

public class CocktailRecipeRegistry {
    private static final List<CocktailRecipe> COCKTAIL_RECIPES = new ArrayList<>();

    public static boolean isValidIngredient(ItemStack stack) {
        for (CocktailRecipe cocktailRecipe : COCKTAIL_RECIPES) {
            if (cocktailRecipe.isIngredient(stack)) {
                return true;
            }
        }

        return false;
    }

    private static CocktailRecipe getRecipe(List<ItemStack> items) {
        boolean canMatch;

        for (CocktailRecipe cocktailRecipe : COCKTAIL_RECIPES) {
            canMatch = true;
            for (ItemStack itemStack : items) {
                if (!cocktailRecipe.isIngredient(itemStack)) {
                    canMatch = false;
                    break;
                }
            }
            if (canMatch) {
                return cocktailRecipe;
            }
        }
        return null;
    }

    public static boolean hasRecipe(List<ItemStack> items) {
        return getRecipe(items) != null;
    }

    public static ItemStack craft(List<ItemStack> items) {
        return getRecipe(items).getOutput();
    }

    public static void registerDefaults() {
        registerRecipe(Cocktails.RADIOACTIVE_COCKTAIL, Items.EMERALD, DesertStorm.BANANA_ITEM);
        registerRecipe(Cocktails.DISLOCATOR, Items.CHORUS_FRUIT);
        registerRecipe(Cocktails.SUNRISE, Items.GLOW_BERRIES);
        registerRecipe(Cocktails.SUNSET, Items.SWEET_BERRIES, DesertStorm.BANANA_ITEM);
        registerRecipe(Cocktails.ICED, Items.SNOWBALL);
        registerRecipe(Cocktails.HOT_COCOA, Items.COCOA_BEANS);
        registerRecipe(Cocktails.HEALTHY_SMOOTHIE, Items.WHEAT_SEEDS);
        registerRecipe(Cocktails.MULTIVITAMIN, Items.APPLE, Items.CARROT, Items.BEETROOT);
        registerRecipe(Cocktails.MIDAS_SPECIAL, Items.GOLDEN_APPLE, Items.GOLDEN_CARROT, Items.GLISTERING_MELON_SLICE);
        registerRecipe(Cocktails.MOLOTOV, Items.GUNPOWDER, Items.FIRE_CHARGE);
    }

    private static void registerRecipe(Cocktail output, Item... items) {
        Ingredient[] ingredients = new Ingredient[items.length];
        for (int i = 0; i < items.length; i++) {
            ingredients[i] = Ingredient.ofItems(items[i]);
        }
        COCKTAIL_RECIPES.add(new CocktailRecipe(Utils.NewIdentifier(output.name),
                CocktailUtil.setCocktail(new ItemStack(DesertStorm.COCKTAIL), output), ingredients));
    }
}
