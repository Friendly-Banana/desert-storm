package org.gara.desertstorm.items.cocktails;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

public class CocktailRecipeSerializer implements RecipeSerializer<CocktailRecipe> {
    // Turns json into Recipe
    @Override
    public CocktailRecipe read(Identifier recipeId, JsonObject json) {
        ExampleRecipeJsonFormat recipeJson = new Gson().fromJson(json, ExampleRecipeJsonFormat.class);
        // Validate all fields are there
        if (recipeJson.inputA == null || recipeJson.inputB == null || recipeJson.inputC == null
                || recipeJson.outputItem == null) {
            throw new JsonSyntaxException("A required attribute is missing!");
        }
        Ingredient inputA = Ingredient.fromJson(recipeJson.inputA);
        Ingredient inputB = Ingredient.fromJson(recipeJson.inputB);
        Ingredient inputC = Ingredient.fromJson(recipeJson.inputC);
        Item outputItem = Registry.ITEM.getOrEmpty(new Identifier(recipeJson.outputItem))
                // Validate the inputted item actually exists
                .orElseThrow(() -> new JsonSyntaxException("No such item: " + recipeJson.outputItem));
        ItemStack output = new ItemStack(outputItem);

        return new CocktailRecipe(recipeId, output, inputA, inputB, inputC);
    }

    // Turns Recipe into PacketByteBuf
    @Override
    public void write(PacketByteBuf packetData, CocktailRecipe recipe) {
        DefaultedList<Ingredient> ingredients = recipe.getIngredients();
        for (Ingredient ingredient : ingredients) {
            ingredient.write(packetData);
        }
        packetData.writeItemStack(recipe.getOutput());
    }

    // Turns PacketByteBuf into Recipe
    @Override
    public CocktailRecipe read(Identifier id, PacketByteBuf packetData) {
        return null;
    }
}