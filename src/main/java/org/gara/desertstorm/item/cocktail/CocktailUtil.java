package org.gara.desertstorm.item.cocktail;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

public class CocktailUtil {
    private static final String KEY = "Cocktail";

    public static Cocktail getCocktail(ItemStack stack) {
        return getCocktail(stack.getNbt());
    }

    public static Cocktail getCocktail(@Nullable NbtCompound compound) {
        return compound == null ? Cocktails.EMPTY : Cocktail.byId(compound.getString(KEY));
    }

    public static ItemStack setCocktail(ItemStack stack, Cocktail cocktail) {
        Identifier identifier = Registry.POTION.getId(cocktail);
        if (cocktail == Cocktails.EMPTY) {
            stack.removeSubNbt(KEY);
        } else {
            stack.getOrCreateNbt().putString(KEY, identifier.toString());
        }

        return stack;
    }
}
