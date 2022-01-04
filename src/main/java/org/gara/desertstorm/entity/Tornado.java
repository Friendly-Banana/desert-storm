package org.gara.desertstorm.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.FollowMobGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.gara.desertstorm.DamageSources;
import org.gara.desertstorm.DesertStorm;

import static net.minecraft.world.GameRules.DO_MOB_GRIEFING;

public class Tornado extends HostileEntity {
    public static final EntityDimensions dimensions = EntityDimensions.fixed(3, 5);
    private static final ParticleEffect particleOptions = new BlockStateParticleEffect(ParticleTypes.FALLING_DUST,
            Blocks.SAND.getDefaultState());
    private final ServerBossBar bossEvent;
    public int duration;

    public Tornado(EntityType<? extends Tornado> entityType, World level) {
        super(entityType, level);
        //                                                                        new BossBar.Color("gold", Formatting.GOLD)
        this.bossEvent = (ServerBossBar) new ServerBossBar(this.getDisplayName(), BossBar.Color.YELLOW, BossBar.Style.PROGRESS).setDarkenSky(true);
    }

    public static DefaultAttributeContainer.Builder createTornadoAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10);
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
        FallingBlockEntity fallingBlock = new FallingBlockEntity(this.world, this.getX(), this.getEyeY(), this.getZ(), blockState);
        // prevent dropping and despawning
        fallingBlock.timeFalling = -2147483648;
        fallingBlock.startRiding(this, true);
        this.world.spawnEntity(fallingBlock);
    }

    private void DropBlock() {
        BlockPos currentPos = this.getBlockPos();
        BlockPos randomPos;
        do {
            randomPos = currentPos.add(random.nextInt(7) - 3, 0, random.nextInt(7) - 3);
        } while (!this.world.isAir(randomPos));
        if (getFirstPassenger() instanceof FallingBlockEntity fallingBlock) {
            this.world.setBlockState(randomPos, fallingBlock.getBlockState());
            fallingBlock.discard();
        }
    }

    @Override
    public void tick() {
        if (!this.world.isClient) {
            if (age >= duration) {
                while (getFirstPassenger() instanceof FallingBlockEntity) {
                    DropBlock();
                }
                this.discard();
                return;
            }
            if (getFirstPassenger() instanceof FallingBlockEntity fallingBlock && fallingBlock.age > 600)
                DropBlock();
            this.bossEvent.setPercent(1 - (float) age / duration);
            // 20 Ticks per Second
            if (age % (10 * 20) == 0 && this.world.getGameRules().getBoolean(DO_MOB_GRIEFING)) {
                NewFlyingBlock();
            }
        }
        super.tick();
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return passenger instanceof FallingBlockEntity && super.canAddPassenger(passenger);
    }

    @Override
    public void pushAwayFrom(Entity entity) {
        Vec3d diff = entity.getPos().subtract(this.getPos()).multiply(0.5f);
        entity.setVelocity(diff.add(0, 0.7, 0));
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        player.damage(DamageSources.TORNADO, 0.5f);
        super.onPlayerCollision(player);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compoundTag) {
        super.writeCustomDataToNbt(compoundTag);
        compoundTag.putInt("Age", this.age);
        compoundTag.putInt("Duration", this.duration);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compoundTag) {
        super.readCustomDataFromNbt(compoundTag);
        this.age = compoundTag.getInt("Age");
        this.duration = compoundTag.getInt("Duration");
        if (duration <= 0) {
            duration = (random.nextInt(30) + 90) * 20;
        }
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new FollowMobGoal(this, 0.2, 1, 16));
        this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 16));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true, false));
    }

    @Override
    public boolean handleAttack(Entity attacker) {
        if (!world.isClient && attacker instanceof PlayerEntity player && (player.getMainHandStack().isOf(DesertStorm.TORNADO_CLEANER_ITEM) || player.getOffHandStack().isOf(DesertStorm.TORNADO_CLEANER_ITEM)))
            age += 50;
        return true;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
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
}