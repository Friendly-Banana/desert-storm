package org.gara.desertstorm.item.cocktails;

import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class CocktailRecipe implements Recipe<SidedInventory> {
    // You can add as much inputs as you want here.
    // It is important to always use Ingredient, so you can support tags.
    private final Ingredient[] ingredients;
    private final ItemStack output;
    private final Identifier id;
    // TEST_RECIPE_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MODID, "test_furnace"), new CookingRecipeSerializer<>(TestRecipe::new, 200));

    public CocktailRecipe(Identifier id, ItemStack output, Ingredient... ingredients) {
        this.id = id;
        this.ingredients = ingredients;
        this.output = output;
    }

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
        return true;
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
        private Type() {
        }

        public static final Type INSTANCE = new Type();
        public static final String ID = "mixer_recipe";
    }
}