package org.gara.desertstorm.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.world.World;
import org.gara.desertstorm.entity.BuildNestGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpiderEntity.class)
public abstract class SpiderMixin extends HostileEntity {
	protected SpiderMixin(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
		throw new AssertionError();
	}

	@Inject(method = "initGoals", at = @At("TAIL"))
	void placeCobwebs(CallbackInfo ci) {
		this.goalSelector.add(5, new BuildNestGoal((SpiderEntity) (Object) this));
	}
}