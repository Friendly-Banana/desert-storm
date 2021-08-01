package org.gara.desertstorm.entities;

import java.util.ArrayList;
import java.util.List;

import org.gara.desertstorm.Utils;

import net.fabricmc.fabric.impl.object.builder.FabricEntityType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class Tornado extends Entity {
    private static final ParticleOptions particles = ParticleTypes.SMOKE;
    private static final TornadoDamage damageSource = new TornadoDamage();
    private final ServerBossEvent bossEvent;
    private List<FallingBlockEntity> flyingBlocks;
    private int duration;

    public Tornado(EntityType<? extends Tornado> entityType, Level level) {
        super(entityType, level);
        this.bossEvent = (ServerBossEvent) (new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.YELLOW,
                BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);

        flyingBlocks = new ArrayList<FallingBlockEntity>();
    }

    /*public Tornado(Level level, Vec3 pos) {
        this(DesertStorm.TORNADO, level);
        this.setPos(pos);
    }*/

    private void NewFlyingBlock() {
        for (int i = 0; i < 3; i++) {
            FallingBlockEntity sandEntity = FabricEntityType.FALLING_BLOCK.create(this.level);
            sandEntity.time = 1;
            //sandEntity.startRiding(this);
            this.level.addFreshEntity(sandEntity);
            flyingBlocks.add(sandEntity);
        }
    }

    @Override
    public void push(Entity entity) {
        super.push(entity);
        entity.push(0, 2, 0);
    }

    @Override
    public void playerTouch(Player player) {
        super.playerTouch(player);
        player.hurt(damageSource, 3);
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount >= duration) {
            Utils.Debug();
            this.discard();
        }
        this.bossEvent.setProgress(1 - (float) tickCount / duration);
        if (tickCount % 20 == 0) {
            NewFlyingBlock();
        }
    }

    @Override
    protected void defineSynchedData() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putInt("Age", this.tickCount);
      compoundTag.putInt("Duration", this.duration);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        this.tickCount = compoundTag.getInt("Age");
        this.duration = compoundTag.getInt("Duration");
        if (duration <= 0) {
            duration = (random.nextInt(30) + 90) * 20;
        }
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public void setCustomName(Component component) {
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

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}