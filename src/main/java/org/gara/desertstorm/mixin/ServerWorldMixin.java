package org.gara.desertstorm.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.WorldChunk;
import org.gara.desertstorm.DesertStorm;
import org.gara.desertstorm.entity.Sandstorm;
import org.gara.desertstorm.entity.Tornado;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void tornadoes(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        ServerWorld world = (ServerWorld) (Object) this;
        if (world.random.nextInt(10000) == 0) {
            List<ServerPlayerEntity> players = world.getPlayers((player) -> player.isAlive() && !player.isSpectator());
            if (!players.isEmpty()) {
                ServerPlayerEntity unluckyPlayer = players.get(world.random.nextInt(players.size()));
                Tornado tornado = DesertStorm.TORNADO.create(world);
                tornado.refreshPositionAfterTeleport(unluckyPlayer.getPos().add(world.random.nextInt(-10, 10), 0, world.random.nextInt(-10, 10)));
                world.spawnEntity(tornado);
            }
        }
    }

    @Inject(method = "tickChunk", at = @At(value = "HEAD"))
    private void sandstorms(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci) {
        ServerWorld world = (ServerWorld) (Object) this;
        if (!world.getDimension().hasSkyLight()) return;
        BlockPos center = chunk.getPos().getCenterAtY(chunk.getHighestNonEmptySectionYOffset());
        if (world.getBiome(center).doesNotSnow(center) && world.random.nextInt(10000) == 0 && world.random.nextDouble() < (world.getLocalDifficulty(center)).getLocalDifficulty() * 0.01) {
            Sandstorm sandstorm = DesertStorm.SANDSTORM.create(world);
            sandstorm.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(center));
            world.spawnEntity(sandstorm);
        }
    }
}
