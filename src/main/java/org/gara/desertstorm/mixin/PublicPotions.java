package org.gara.desertstorm.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.core.Registry;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;

@Mixin(Potions.class)
public interface PublicPotions {
    @Invoker("register")
    public static Potion register(String string, Potion potion) {
        return Registry.register(Registry.POTION, string, potion);
    }
}