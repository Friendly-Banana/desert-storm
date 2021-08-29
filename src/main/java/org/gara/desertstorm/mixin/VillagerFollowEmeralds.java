package org.gara.desertstorm.mixin;

import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.passive.VillagerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerEntity.class)
public class VillagerFollowEmeralds {
	@Inject(at = @At("HEAD"), method = "registerBrainGoals()Lnet/minecraft/world/entity/ai/Brain")
	private void registerBrainGoals(Brain<VillagerEntity> brain, CallbackInfo info) {
		System.out.println("This line is printed by an example mod mixin!");
	}
}
