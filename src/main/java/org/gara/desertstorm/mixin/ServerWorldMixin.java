package org.gara.desertstorm.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
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
    private static final int SPAWN_DISTANCE = 16;

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void spawnTornadoesAndSandstorms(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        ServerWorld world = (ServerWorld) (Object) this;
        List<ServerPlayerEntity> players = world.getPlayers((player) -> player.isAlive() && !player.isSpectator());
        if (!players.isEmpty()) {
            ServerPlayerEntity unluckyPlayer = players.get(world.random.nextInt(players.size()));
            // Sandstorms
            BlockPos playerBlockPos = unluckyPlayer.getBlockPos();
            if (world.getBiome(playerBlockPos).doesNotSnow(playerBlockPos) && world.random.nextInt(1000) == 0 && world.random.nextDouble() > (world.getLocalDifficulty(playerBlockPos)).getLocalDifficulty() / 10) {
                Sandstorm sandstorm = DesertStorm.SANDSTORM.create(world);
                sandstorm.refreshPositionAfterTeleport(unluckyPlayer.getPos().add(world.random.nextInt(-SPAWN_DISTANCE, SPAWN_DISTANCE), 0,
                        world.random.nextInt(-SPAWN_DISTANCE, SPAWN_DISTANCE)));
                world.spawnEntity(sandstorm);
            }  // Tornadoes
            else if (world.random.nextInt(10000) == 0) {
                Tornado tornado = DesertStorm.TORNADO.create(world);
                tornado.refreshPositionAfterTeleport(unluckyPlayer.getPos().add(world.random.nextInt(-SPAWN_DISTANCE, SPAWN_DISTANCE), 0,
                        world.random.nextInt(-SPAWN_DISTANCE, SPAWN_DISTANCE)));
                world.spawnEntity(tornado);
            }
        }
    }
}
