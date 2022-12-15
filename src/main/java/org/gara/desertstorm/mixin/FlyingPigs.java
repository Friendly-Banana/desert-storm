package org.gara.desertstorm.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.gara.desertstorm.DesertStorm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PigEntity.class)
public abstract class FlyingPigs extends AnimalEntityMixin {
	private static Input input;
	private boolean hasElytra;

	protected FlyingPigs(EntityType<? extends PassiveEntity> entityType, World world) {
		super(entityType, world);
		throw new AssertionError();
	}

	@Shadow
	public abstract boolean canBeControlledByRider();

	@Inject(method = "travel", at = @At(value = "TAIL"))
	private void controlFlyingPig(Vec3d movementInput, CallbackInfo ci) {
		if (input == null) {
			ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;
			if (clientPlayer != null) input = clientPlayer.input;
		}
		if (input == null) {
			return;
		}
		if (world.getGameRules().getBoolean(DesertStorm.FLYING_PIGS) && canBeControlledByRider()) {
			if (isOnGround()) {
				if (hasElytra) {
					hasElytra = false;
					equipStack(EquipmentSlot.CHEST, ItemStack.EMPTY);
				}
			} else {
				if (!hasElytra) {
					hasElytra = true;
					equipStack(EquipmentSlot.CHEST, Items.ELYTRA.getDefaultStack());
				}
			}
			if (input.jumping) updateVelocity(0.5f, new Vec3d(0, 1, 0.5));
			setFlag(Entity.FALL_FLYING_FLAG_INDEX, !isOnGround());
		}
	}

	@Inject(method = "getSaddledSpeed", at = @At(value = "HEAD"), cancellable = true)
	private void flySpeed(CallbackInfoReturnable<Float> cir) {
		PigEntity pig = (PigEntity) (Object) this;
		if (world.getGameRules().getBoolean(DesertStorm.FLYING_PIGS) && (!isOnGround() || hasNoGravity())) {
			cir.setReturnValue((float) pig.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) * 2.0f);
		}
	}

	@Override
	void handleDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (world.getGameRules().getBoolean(DesertStorm.FLYING_PIGS) && source.isFromFalling()) cir.setReturnValue(false);
	}
}