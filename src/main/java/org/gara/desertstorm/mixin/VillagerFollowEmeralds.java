package org.gara.desertstorm.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.npc.Villager;

@Mixin(Villager.class)
public class VillagerFollowEmeralds {
	@Inject(at = @At("HEAD"), method = "registerBrainGoals()Lnet/minecraft/world/entity/ai/Brain")
	private void registerBrainGoals(Brain<Villager> brain, CallbackInfo info) {
		System.out.println("This line is printed by an example mod mixin!");
	}
}
