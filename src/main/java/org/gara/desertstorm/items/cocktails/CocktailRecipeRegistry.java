package org.gara.desertstorm.items.cocktails;

import java.util.List;

import com.google.common.collect.Lists;

import org.gara.desertstorm.DesertStorm;
import org.gara.desertstorm.Utils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.Ingredient;

public class CocktailRecipeRegistry {
   public static final int field_30942 = 20;
   private static final List<CocktailRecipe> COCKTAIL_RECIPES = Lists.newArrayList();

   public static boolean isValidIngredient(ItemStack stack) {
      for (int i = 0; i < COCKTAIL_RECIPES.size(); ++i) {
         if ((COCKTAIL_RECIPES.get(i)).isIngredient(stack)) {
            return true;
         }
      }

      return false;
   }

   public static boolean isBrewable(CocktailItem cocktail) {
      int i = 0;

      for (int j = COCKTAIL_RECIPES.size(); i < j; ++i) {
         if ((COCKTAIL_RECIPES.get(i)).getOutput().isOf(cocktail)) {
            return true;
         }
      }

      return false;
   }

   protected static CocktailRecipe getRecipe(List<ItemStack> items) {
      int i = 0;
      boolean canMatch;

      for (int j = COCKTAIL_RECIPES.size(); i < j; ++i) {
         canMatch = true;
         CocktailRecipe recipe = COCKTAIL_RECIPES.get(i);
         for (ItemStack itemStack : items) {
            if (!recipe.isIngredient(itemStack)) {
               canMatch = false;
               break;
            }
         }
         if (canMatch) {
            return recipe;
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
      registerRecipe(Cocktails.MOLOTOV, Items.GUNPOWDER, Items.FIRE_CHARGE);
      registerRecipe(Cocktails.HEALTHY_SMOOTHIE, DesertStorm.BANANA_ITEM, Items.FIRE_CHARGE);
   }

   private static void registerRecipe(Cocktail output, Item... items) {
      Ingredient[] ingredients = new Ingredient[items.length];
      for (int i = 0; i < items.length; i++) {
         ingredients[i] = Ingredient.ofItems(items[i]);
      }
      COCKTAIL_RECIPES.add(new CocktailRecipe(Utils.NewIdentifier(output.getName()), PotionUtil.setPotion(new ItemStack(DesertStorm.COCKTAIL), output), ingredients));
   }
}
