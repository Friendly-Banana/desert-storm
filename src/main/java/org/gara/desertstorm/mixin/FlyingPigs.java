package org.gara.desertstorm.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.math.Vec3d;
import org.gara.desertstorm.DesertStorm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PigEntity.class)
public class FlyingPigs {
    private static Input input;

    @Inject(method = "travel", at = @At(value = "TAIL"), cancellable = true)
    private void controlFlyingPig(Vec3d movementInput, CallbackInfo ci) {
        if (input == null) {
            ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;
            if (clientPlayer != null) input = clientPlayer.input;
        }
        if (input == null) {
            ci.cancel();
            return;
        }
        PigEntity pig = (PigEntity) (Object) this;
        pig.setNoGravity(false);
        if (pig.world.getGameRules().getBoolean(DesertStorm.FLYING_PIGS) && pig.canBeControlledByRider()) {
            if (input.jumping) pig.addVelocity(0, 0.05, 0);
            if (input.sneaking) pig.addVelocity(0, -0.05, 0);
            pig.setNoGravity(!pig.isOnGround());
        }
    }

    @Inject(method = "getSaddledSpeed", at = @At(value = "HEAD"), cancellable = true)
    private void flySpeed(CallbackInfoReturnable<Float> cir) {
        PigEntity pig = (PigEntity) (Object) this;
        if (pig.world.getGameRules().getBoolean(DesertStorm.FLYING_PIGS) && (!pig.isOnGround() || pig.hasNoGravity())) {
            cir.setReturnValue((float) pig.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) * 2);
        }
    }
}