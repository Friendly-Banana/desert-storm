package org.gara.desertstorm.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.gara.desertstorm.Cocktails;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrewingRecipeRegistry.class)
public class AddCocktailsToRegistry {
    @Inject(method = "registerDefaults", at = @At(value = "TAIL"))
    private static void registerCocktails(CallbackInfo ci) {
        registerPotionRecipe(Potions.MUNDANE, Items.RABBIT_FOOT, Potions.LUCK);
        registerPotionRecipe(Potions.MUNDANE, Items.EMERALD, Cocktails.RADIOACTIVE);
        registerPotionRecipe(Potions.MUNDANE, Items.CHORUS_FRUIT, Cocktails.DISLOCATOR);
        registerPotionRecipe(Potions.MUNDANE, Items.GLOW_BERRIES, Cocktails.SUNRISE);
        registerPotionRecipe(Potions.MUNDANE, Items.SWEET_BERRIES, Cocktails.SUNSET);
        registerPotionRecipe(Potions.MUNDANE, Items.SNOWBALL, Cocktails.ICED);
        registerPotionRecipe(Potions.MUNDANE, Items.COCOA_BEANS, Cocktails.HOT_COCOA);
        registerPotionRecipe(Potions.MUNDANE, Items.WHEAT_SEEDS, Cocktails.HEALTHY_SMOOTHIE);
        registerPotionRecipe(Potions.MUNDANE, Items.APPLE, Cocktails.MULTIVITAMIN);
        registerPotionRecipe(Potions.MUNDANE, Items.CARROT, Cocktails.MULTIVITAMIN);
        registerPotionRecipe(Potions.MUNDANE, Items.GOLDEN_APPLE, Cocktails.MIDAS_SPECIAL);
        registerPotionRecipe(Potions.MUNDANE, Items.GOLDEN_CARROT, Cocktails.MIDAS_SPECIAL);
        registerPotionRecipe(Potions.MUNDANE, Items.FIRE_CHARGE, Cocktails.MOLOTOV);
    }

    @Shadow
    private static void registerPotionRecipe(Potion input, Item item, Potion output) {
        throw new AssertionError();
    }
}