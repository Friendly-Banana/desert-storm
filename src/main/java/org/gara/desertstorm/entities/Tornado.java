package org.gara.desertstorm.entities;

import java.util.ArrayList;
import java.util.List;

import org.gara.desertstorm.DesertStorm;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class Tornado extends Entity {
    public static final EntityDimensions dimensions = EntityDimensions.fixed(3, 5);
    private static final ParticleOptions particleOptions = new BlockParticleOption(ParticleTypes.FALLING_DUST,
            Blocks.SAND.defaultBlockState());
    private static final TornadoDamage damageSource = new TornadoDamage();
    private final ServerBossEvent bossEvent;
    private List<FallingBlockEntity> flyingBlocks;
    private int duration;

    public Tornado(EntityType<? extends Tornado> entityType, Level level) {
        super(entityType, level);
        // new BossEvent.BossBarColor("gold", ChatFormatting.GOLD)
        this.bossEvent = (ServerBossEvent) (new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.YELLOW,
                BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);

        flyingBlocks = new ArrayList<FallingBlockEntity>();
    }

    public Tornado(Level level, Vec3 pos) {
        this(DesertStorm.TORNADO, level);
        this.setPos(pos);
    }

    private void NewFlyingBlock() {
        BlockPos currentPos = this.getOnPos();
        BlockPos randomPos;
        BlockState blockState;
        for (int i = 0; i < 3; i++) {
            // pick up random block
            do {
                randomPos = currentPos.offset(random.nextInt(7) - 3, 0, random.nextInt(7) - 3);
                blockState = this.level.getBlockState(randomPos);
            } while (blockState.isAir() || blockState.hasBlockEntity()
                    || blockState.getBlock().getExplosionResistance() > 30);
            this.level.removeBlock(randomPos, false);
            FallingBlockEntity fallingBlock = new FallingBlockEntity(this.level, this.getX(), this.getEyeY(),
                    this.getZ(), blockState);
            fallingBlock.time = 1;
            fallingBlock.startRiding(this);
            this.level.addFreshEntity(fallingBlock);
            flyingBlocks.add(fallingBlock);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            float radius = dimensions.width;
            int area = Mth.ceil(3.1415927F * radius);// * radius);
            float h = radius;

            for (int i = 0; i < area; ++i) {
                float l = this.random.nextFloat() * 6.2831855F;
                float m = Mth.sqrt(this.random.nextFloat()) * h;
                double xPos = this.getX() + (double) (Mth.cos(l) * m);
                double yPos = this.getY() + random.nextDouble() * dimensions.height;
                double zPos = this.getZ() + (double) (Mth.sin(l) * m);
                double xRange = (0.5D - this.random.nextDouble()) * 0.15D;
                double yRange = dimensions.height;
                double zRange = (0.5D - this.random.nextDouble()) * 0.15D;

                if (this.random.nextInt(3) == 0) {
                    this.level.addAlwaysVisibleParticle(particleOptions, xPos, yPos, zPos, xRange, yRange, zRange);
                } else {
                    this.level.addParticle(particleOptions, xPos, yPos, zPos, xRange, yRange, zRange);
                }
            }
        } else {
            if (tickCount >= duration) {
                this.discard();
            }
            this.bossEvent.setProgress(1 - (float) tickCount / duration);
            // 20 Ticks per Second
            if (tickCount % (10 * 20) == 0) {
                NewFlyingBlock();
            }
        }
    }

    @Override
    public void playerTouch(Player player) {
        super.playerTouch(player);
        player.hurt(damageSource, 1.5f);
    }

    @Override
    public void push(Entity entity) {
        if (entity.isAttackable()) {
            entity.setDeltaMovement(entity.getDeltaMovement().add(0, 4, 0));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putInt("Age", this.tickCount);
        compoundTag.putInt("Duration", this.duration);
        /*
         * for (FallingBlockEntity fallingBlockEntity : flyingBlocks) {
         * fallingBlockEntity.saveAsPassenger(compoundTag); }
         */
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
    protected void defineSynchedData() {
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