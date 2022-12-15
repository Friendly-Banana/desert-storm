package org.gara.desertstorm.mixin;

import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RabbitEntity.class)
public abstract class RabbitImmuneToIllager extends AnimalEntityMixin {
	public RabbitImmuneToIllager(EntityType<? extends PassiveEntity> type, World world) {
		super(type, world);
	}

	@Shadow
	public abstract int getRabbitType();

	@Override
	void handleDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (getRabbitType() == RabbitEntity.KILLER_BUNNY_TYPE) {
			if (source.getAttacker() instanceof LivingEntity livingEntity && livingEntity.getGroup() == EntityGroup.ILLAGER) {
				cir.setReturnValue(false);
			}
		}
	}
}
