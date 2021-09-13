package org.gara.desertstorm.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.gara.desertstorm.DesertStorm;
import org.gara.desertstorm.damage.TornadoDamage;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.GameRules.DO_MOB_GRIEFING;

public class Tornado extends Entity {
    public static final EntityDimensions dimensions = EntityDimensions.fixed(3, 5);
    private static final ParticleEffect particleOptions = new BlockStateParticleEffect(ParticleTypes.FALLING_DUST,
            Blocks.SAND.getDefaultState());
    private static final TornadoDamage damageSource = new TornadoDamage();
    private final ServerBossBar bossEvent;
    private final List<FallingBlockEntity> flyingBlocks;
    private int duration;

    public Tornado(EntityType<? extends Tornado> entityType, World level) {
        super(entityType, level);
        // new BossEvent.BossBarColor("gold", ChatFormatting.GOLD)
        this.bossEvent = (ServerBossBar) (new ServerBossBar(this.getDisplayName(), BossBar.Color.YELLOW,
                BossBar.Style.PROGRESS)).setDarkenSky(true);

        flyingBlocks = new ArrayList<>();
    }

    public Tornado(World level, Vec3d pos) {
        this(DesertStorm.TORNADO, level);
        this.setPosition(pos);
    }

    private void NewFlyingBlock() {
        BlockPos currentPos = this.getBlockPos();
        // shouldn't take too long
        int limit = 0;
        BlockPos randomPos;
        BlockState blockState;
        // pick up random block
        do {
            limit++;
            if (limit > 10)
                return;
            randomPos = currentPos.add(random.nextInt(7) - 3, 0, random.nextInt(7) - 3);
            blockState = this.world.getBlockState(randomPos);
        } while (blockState.isAir() || blockState.hasBlockEntity() || blockState.getBlock().getBlastResistance() > 30);
        this.world.removeBlock(randomPos, false);
        FallingBlockEntity fallingBlock = new FallingBlockEntity(this.world, this.getX(), this.getEyeY(), this.getZ(),
                blockState);
        fallingBlock.timeFalling = 1;
        fallingBlock.startRiding(this);
        this.world.spawnEntity(fallingBlock);
        flyingBlocks.add(fallingBlock);
        // drop old block
        if (flyingBlocks.size() > 3) {
            DropBlock();
        }
    }

    private void DropBlock() {
        BlockPos currentPos = this.getBlockPos();
        BlockPos randomPos;
        do {
            randomPos = currentPos.add(random.nextInt(7) - 3, 0, random.nextInt(7) - 3);
        } while (!this.world.isAir(randomPos));
        FallingBlockEntity blockEntity = flyingBlocks.remove(0);
        this.world.setBlockState(randomPos, blockEntity.getBlockState());
        blockEntity.discard();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.world.isClient) {
            float radius = dimensions.width;
            int area = MathHelper.ceil(3.1415927F * radius);// * radius);

            for (int i = 0; i < area; ++i) {
                float l = this.random.nextFloat() * 6.2831855F;
                float m = MathHelper.sqrt(this.random.nextFloat()) * radius;
                double xPos = this.getX() + (double) (MathHelper.cos(l) * m);
                double yPos = this.getY() + random.nextDouble() * dimensions.height;
                double zPos = this.getZ() + (double) (MathHelper.sin(l) * m);
                double xRange = (0.5D - this.random.nextDouble()) * 0.15D;
                double yRange = dimensions.height;
                double zRange = (0.5D - this.random.nextDouble()) * 0.15D;

                if (this.random.nextBoolean()) {
                    this.world.addImportantParticle(particleOptions, xPos, yPos, zPos, xRange, yRange, zRange);
                } else {
                    this.world.addParticle(particleOptions, xPos, yPos, zPos, xRange, yRange, zRange);
                }
            }

        } // Server
        else {
            if (age >= duration) {
                for (int i = 0; i < flyingBlocks.size(); i++) {
                    DropBlock();
                }
                this.discard();
            }
            this.bossEvent.setPercent(1 - (float) age / duration);
            // 20 Ticks per Second
            if (age % (10 * 20) == 0 && this.world.getGameRules().getBoolean(DO_MOB_GRIEFING)) {
                NewFlyingBlock();
            }
        }
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        super.onPlayerCollision(player);
        player.damage(damageSource, 1.5f);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound compoundTag) {
        compoundTag.putInt("Age", this.age);
        compoundTag.putInt("Duration", this.duration);
        /*
         * for (FallingBlockEntity fallingBlockEntity : flyingBlocks) {
         * fallingBlockEntity.saveAsPassenger(compoundTag); }
         */
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compoundTag) {
        this.age = compoundTag.getInt("Age");
        this.duration = compoundTag.getInt("Duration");
        if (duration <= 0) {
            duration = (random.nextInt(30) + 90) * 20;
        }
    }

    @Override
    protected void initDataTracker() {
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public void setCustomName(Text component) {
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

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }
}