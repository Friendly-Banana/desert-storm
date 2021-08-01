package org.gara.desertstorm.entities;

import java.util.ArrayList;
import java.util.List;

import org.gara.desertstorm.Utils;
import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.impl.object.builder.FabricEntityType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;

public class Sandstorm extends AreaEffectCloud {
    private final ServerBossEvent bossEvent;
    List<FallingBlockEntity> flyingBlocks;

    public Sandstorm(EntityType<? extends Sandstorm> entityType, Level level) {
        super(entityType, level);
        this.bossEvent = (ServerBossEvent) (new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.YELLOW,
                BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
        
        this.setDuration(3 * 60 * 20);
        this.setRadius(5.0F);
        this.setParticle(ParticleTypes.DRAGON_BREATH);
        this.setFixedColor(Utils.SAND_COLOR);
        this.addEffect(new MobEffectInstance(MobEffects.HARM, 3, 1));

        flyingBlocks = new ArrayList<FallingBlockEntity>();
        for (int i = 0; i < 3; i++) {
            FallingBlockEntity sandEntity = FabricEntityType.FALLING_BLOCK.create(level);
            sandEntity.time = 1;
            sandEntity.setPos(this.getPosition(0.1F));
            sandEntity.setNoGravity(true);
            sandEntity.startRiding(this);
            level.addFreshEntity(sandEntity);
            flyingBlocks.add(sandEntity);
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.bossEvent.setProgress(1 - (float) tickCount / this.getDuration());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (this.getDuration() <= 0) {
            setDuration((random.nextInt(150) + 30) * 20);
        }
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
    }

    @Override
    public void setCustomName(@Nullable Component component) {
        super.setCustomName(component);
        this.bossEvent.setName(this.getDisplayName());
    }

    @Override
    public void startSeenByPlayer(ServerPlayer serverPlayer) {
        super.startSeenByPlayer(serverPlayer);
        this.bossEvent.addPlayer(serverPlayer);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer serverPlayer) {
        super.stopSeenByPlayer(serverPlayer);
        this.bossEvent.removePlayer(serverPlayer);
    }
}