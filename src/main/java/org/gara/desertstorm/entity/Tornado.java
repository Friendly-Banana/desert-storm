package org.gara.desertstorm.entity;

import com.chocohead.mm.api.ClassTinkerers;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.gara.desertstorm.DamageSources;
import org.gara.desertstorm.DesertStorm;
import org.gara.desertstorm.Utils;

import static net.minecraft.world.GameRules.DO_MOB_GRIEFING;

public class Tornado extends HostileEntity {
	public static final EntityDimensions dimensions = EntityDimensions.fixed(3, 5);
	public static final double YEET_VELOCITY = 1;
	private final ServerBossBar bossEvent;
	private int duration;

	public Tornado(EntityType<? extends Tornado> entityType, World level) {
		super(entityType, level);
		duration = (random.nextInt(60) + 60) * 20;
		this.bossEvent = (ServerBossBar) new ServerBossBar(this.getDisplayName(), ClassTinkerers.getEnum(BossBar.Color.class, "GOLD"), BossBar.Style.PROGRESS).setDarkenSky(true);
	}

	public static DefaultAttributeContainer.Builder createTornadoAttributes() {
		return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10);
	}

	private void newFlyingBlock() {
		BlockPos currentPos = this.getBlockPos();
		// shouldn't take too long
		int limit = 0;
		BlockPos randomPos;
		BlockState blockState;
		// pick up random block
		do {
			limit++;
			if (limit > 10) return;
			randomPos = currentPos.add(random.nextInt(7) - 3, 0, random.nextInt(7) - 3);
			blockState = this.world.getBlockState(randomPos);
		} while (blockState.isAir() || blockState.hasBlockEntity() || blockState.getBlock().getBlastResistance() > 30);
		this.world.removeBlock(randomPos, false);
		FallingBlockEntity fallingBlock = FallingBlockEntity.spawnFromBlock(this.world, this.getBlockPos(), blockState);
		Utils.SetTimeFallingToMax(fallingBlock);
		fallingBlock.startRiding(this, true);
	}

	@Override
	public void onPlayerCollision(PlayerEntity player) {
		player.damage(DamageSources.TORNADO, 0.5f);
		player.setVelocity(player.getVelocity().add(0, YEET_VELOCITY, 0));
	}

	@Override
	protected boolean canAddPassenger(Entity passenger) {
		return passenger instanceof FallingBlockEntity && super.canAddPassenger(passenger);
	}

	@Override
	public boolean handleAttack(Entity attacker) {
		if (!world.isClient && attacker instanceof PlayerEntity player && player.getActiveItem().isOf(DesertStorm.TORNADO_CLEANER_ITEM)) {
			age += 50;
		}
		return true;
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
	protected void initGoals() {
		this.goalSelector.add(0, new FollowClassGoal(this, LivingEntity.class, 1, 16));
		this.targetSelector.add(0, new ActiveTargetGoal<>(this, LivingEntity.class, false, false));
	}

	@Override
	public void tick() {
		if (!this.world.isClient) {
			if (age >= duration) {
				while (getFirstPassenger() instanceof FallingBlockEntity fallingBlock) {
					fallingBlock.timeFalling = 1;
					fallingBlock.stopRiding();
				}
				this.discard();
				return;
			}
			if (getFirstPassenger() instanceof FallingBlockEntity fallingBlock && fallingBlock.age > 100) {
				fallingBlock.timeFalling = 1;
				fallingBlock.stopRiding();
			}
			this.bossEvent.setPercent(1 - (float) age / duration);
			// 20 Ticks per Second
			if (age % (10 * 20) == 0 && this.world.getGameRules().getBoolean(DO_MOB_GRIEFING)) {
				newFlyingBlock();
			}
		}
		super.tick();
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
	}

	@Override
	public boolean tryAttack(Entity target) {
		return false;
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		return false;
	}

	@Override
	public void pushAwayFrom(Entity entity) {
		entity.setVelocity(entity.getVelocity().add(0, YEET_VELOCITY, 0));
	}
}