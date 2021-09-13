package org.gara.desertstorm.entity;

import net.fabricmc.fabric.impl.object.builder.FabricEntityType;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.gara.desertstorm.Utils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Sandstorm extends AreaEffectCloudEntity {
    private final ServerBossBar bossEvent;
    final List<FallingBlockEntity> flyingBlocks;

    public Sandstorm(EntityType<? extends Sandstorm> entityType, World level) {
        super(entityType, level);
        this.bossEvent = (ServerBossBar) (new ServerBossBar(this.getDisplayName(), BossBar.Color.YELLOW,
                BossBar.Style.PROGRESS)).setDarkenSky(true);
        this.setDuration(3 * 60 * 20);
        this.setRadius(5.0F);
        this.setParticleType(ParticleTypes.DRAGON_BREATH);
        this.setColor(Utils.SAND);
        this.addEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 1));
        this.addEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 10));

        flyingBlocks = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            FallingBlockEntity sandEntity = FabricEntityType.FALLING_BLOCK.create(level);
            sandEntity.timeFalling = 1;
            sandEntity.setPosition(this.getLerpedPos(0.1F));
            sandEntity.setNoGravity(true);
            sandEntity.startRiding(this);
            level.spawnEntity(sandEntity);
            flyingBlocks.add(sandEntity);
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.bossEvent.setPercent(1 - (float) age / this.getDuration());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compoundTag) {
        super.readCustomDataFromNbt(compoundTag);
        if (this.getDuration() <= 0) {
            setDuration((random.nextInt(150) + 30) * 20);
        }
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
    }

    @Override
    public void setCustomName(@Nullable Text component) {
        super.setCustomName(component);
        this.bossEvent.setName(this.getDisplayName());
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity serverPlayer) {
        super.onStartedTrackingBy(serverPlayer);
        this.bossEvent.addPlayer(serverPlayer);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity serverPlayer) {
        super.onStoppedTrackingBy(serverPlayer);
        this.bossEvent.removePlayer(serverPlayer);
    }
}