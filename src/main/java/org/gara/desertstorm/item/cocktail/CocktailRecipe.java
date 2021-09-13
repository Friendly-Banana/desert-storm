package org.gara.desertstorm.item.cocktail;

import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public record CocktailRecipe(Identifier id, ItemStack output,
                             Ingredient... ingredients) implements Recipe<SidedInventory> {

    public boolean isIngredient(ItemStack itemStack) {
        for (Ingredient ingredient : this.ingredients) {
            if (ingredient.test(itemStack)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return DefaultedList.copyOf(Ingredient.EMPTY, this.ingredients);
    }

    @Override
    public ItemStack getOutput() {
        return this.output;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public boolean matches(SidedInventory inv, World world) {
        if (inv.size() < ingredients.length)
            return false;
        int foundIngredients = 0;
        for (Ingredient ingredient : ingredients) {
            for (int i = 0; i < inv.size(); i++) {
                if (ingredient.test(inv.getStack(i))) {
                    ++foundIngredients;
                    break;
                }
            }
        }
        return foundIngredients == ingredients.length;
    }

    @Override
    public ItemStack craft(SidedInventory inventory) {
        return this.getOutput().copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 3 && height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<CocktailRecipe> {
        public static final Type INSTANCE = new Type();

        private Type() {
        }
    }
}