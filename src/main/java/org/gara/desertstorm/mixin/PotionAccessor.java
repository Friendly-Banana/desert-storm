package org.gara.desertstorm.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.potion.Potion;

@Mixin(Potion.class)
public interface PotionAccessor {
    @Accessor
    String getBaseName();
}