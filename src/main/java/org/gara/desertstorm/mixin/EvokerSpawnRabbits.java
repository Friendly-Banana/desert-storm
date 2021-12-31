package org.gara.desertstorm.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import org.gara.desertstorm.DesertStorm;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(targets = "net.minecraft.entity.mob.EvokerEntity$SummonVexGoal")
public class EvokerSpawnRabbits {
    @Shadow
    @Final
    EvokerEntity field_7267;

    @Inject(method = "castSpell", at = @At("HEAD"), cancellable = true)
    private void spawnVexOrRabbit(CallbackInfo ci) {
        ServerWorld serverWorld = (ServerWorld) field_7267.world;
        int rabbitChance = serverWorld.getGameRules().getInt(DesertStorm.EVOKER_RABBIT_CHANCE);
        Random random = field_7267.getRandom();
        if (rabbitChance != 0 && random.nextInt(0, 10) < rabbitChance) {
            BlockPos blockPos = field_7267.getBlockPos().add(-2 + random.nextInt(5), 1, -2 + random.nextInt(5));
            // spawn killer bunny, rabbit type 99
            RabbitEntity rabbit = EntityType.RABBIT.create(serverWorld);
            rabbit.initialize(serverWorld, field_7267.world.getLocalDifficulty(blockPos), SpawnReason.MOB_SUMMONED, null, null);
            rabbit.refreshPositionAndAngles(blockPos, 0.0f, 0.0f);
            rabbit.setRabbitType(RabbitEntity.KILLER_BUNNY_TYPE);
            serverWorld.spawnEntity(rabbit);
            field_7267.setCustomName(new TranslatableText("entity.desertstorm.rabbit_evoker"));
            field_7267.setCustomNameVisible(true);
            ci.cancel();
        }
    }
}
