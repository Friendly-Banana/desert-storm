package org.gara.desertstorm.mixin;

import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.gara.desertstorm.DesertStorm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "shouldDismount", at = @At(value = "TAIL"), cancellable = true)
    private void lowerPigInsteadOfDismounting(CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.world.getGameRules().getBoolean(DesertStorm.FLYING_PIGS) && player.getVehicle() instanceof PigEntity pig && pig.canBeControlledByRider() && !pig.isOnGround()) {
            cir.setReturnValue(false);
        }
    }
}
