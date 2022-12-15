package org.gara.desertstorm.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.gara.desertstorm.Utils;
import org.gara.desertstorm.entity.DSEntities;
import org.gara.desertstorm.entity.Tornado;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Random;
import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
	private static final int SPAWN_DISTANCE = 16;

	@Inject(method = "tick", at = @At(value = "HEAD"))
	private void spawnTornadoes(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		ServerWorld world = (ServerWorld) (Object) this;
		List<ServerPlayerEntity> players = world.getPlayers((player) -> player.isAlive() && !player.isSpectator());
		if (!players.isEmpty()) {
			Random random = world.random;
			if (random.nextInt(50000) == 0) {
				ServerPlayerEntity unluckyPlayer = players.get(random.nextInt(players.size()));
				Tornado tornado = Utils.CreateAndTeleport(DSEntities.TORNADO, world, unluckyPlayer.getPos().add(random.nextInt(-SPAWN_DISTANCE, SPAWN_DISTANCE), 0, random.nextInt(-SPAWN_DISTANCE, SPAWN_DISTANCE)));
				world.spawnEntity(tornado);
			}
		}
	}
}
