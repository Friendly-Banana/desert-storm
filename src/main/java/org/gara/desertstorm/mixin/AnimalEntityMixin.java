package org.gara.desertstorm.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.RabbitEntity;
import org.gara.desertstorm.DesertStorm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnimalEntity.class)
@SuppressWarnings({"ConstantConditions"})
public class AnimalEntityMixin {
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void takeDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        // rabbit immune to illagers
        if ((Object) this instanceof RabbitEntity rabbit && rabbit.getRabbitType() == RabbitEntity.KILLER_BUNNY_TYPE) {
            Entity entity = source.getAttacker();
            if (entity instanceof LivingEntity livingEntity && livingEntity.getGroup() == EntityGroup.ILLAGER)
                cir.setReturnValue(false);
        }
        // flying pigs don't take fall damage
        if ((Object) this instanceof PigEntity pig && pig.world.getGameRules().getBoolean(DesertStorm.FLYING_PIGS) && source.isFromFalling())
            cir.setReturnValue(false);
    }
}
