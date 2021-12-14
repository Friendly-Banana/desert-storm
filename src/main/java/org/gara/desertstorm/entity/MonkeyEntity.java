package org.gara.desertstorm.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.ai.pathing.SpiderNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.StructureFeature;
import org.gara.desertstorm.DesertStorm;

import java.util.EnumSet;

public class MonkeyEntity extends AnimalEntity {
    protected static final TrackedData<Byte> CLIMBING_FLAG;
    protected static final TrackedData<BlockPos> TREASURE_POS;
    protected static final TrackedData<Boolean> HAS_FOOD;

    static {
        CLIMBING_FLAG = DataTracker.registerData(MonkeyEntity.class, TrackedDataHandlerRegistry.BYTE);
        TREASURE_POS = DataTracker.registerData(MonkeyEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
        HAS_FOOD = DataTracker.registerData(MonkeyEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    public MonkeyEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createMonkeyAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2F);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("TreasurePosX", this.getTreasurePos().getX());
        nbt.putInt("TreasurePosY", this.getTreasurePos().getY());
        nbt.putInt("TreasurePosZ", this.getTreasurePos().getZ());
        nbt.putBoolean("GotBanana", this.getHasFood());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        int i = nbt.getInt("TreasurePosX");
        int j = nbt.getInt("TreasurePosY");
        int k = nbt.getInt("TreasurePosZ");
        this.setTreasurePos(new BlockPos(i, j, k));
        super.readCustomDataFromNbt(nbt);
        this.setHasFood(nbt.getBoolean("GotBanana"));
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!itemStack.isEmpty() && itemStack.isOf(DesertStorm.COCONUT_ITEM)) {
            if (!this.world.isClient) {
                this.playSound(SoundEvents.ENTITY_LLAMA_EAT, 1.0F, 1.0F);
            }
            this.setHasFood(true);
            if (!player.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }
            return ActionResult.success(this.world.isClient);
        } else {
            return super.interactMob(player, hand);
        }
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new LeadToNearbyTreasureGoal(this));
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25D));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0D));
        this.goalSelector.add(3, new TemptGoal(this, 1.1D, Ingredient.ofItems(DesertStorm.BANANA_ITEM), false));
        this.goalSelector.add(4, new FollowParentGoal(this, 1.1D));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(CLIMBING_FLAG, (byte) 0);
        this.dataTracker.startTracking(TREASURE_POS, BlockPos.ORIGIN);
        this.dataTracker.startTracking(HAS_FOOD, true);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.world.isClient) {
            this.setClimbingWall(this.horizontalCollision);
        }
    }

    @Override
    public boolean isClimbing() {
        return (this.dataTracker.get(CLIMBING_FLAG) & 1) != 0;
    }

    public void setClimbingWall(boolean climbing) {
        byte b = this.dataTracker.get(CLIMBING_FLAG);
        if (climbing) {
            b = (byte) (b | 1);
        } else {
            b &= -2;
        }
        this.dataTracker.set(CLIMBING_FLAG, b);
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new SpiderNavigation(this, world);
    }

    public MonkeyEntity createChild(ServerWorld world, PassiveEntity other) {
        return DesertStorm.MONKEY.create(world);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(DesertStorm.BANANA_ITEM);
    }

    protected boolean isNearTarget() {
        BlockPos blockPos = this.getNavigation().getTargetPos();
        return blockPos != null && blockPos.isWithinDistance(this.getPos(), 12.0D);
    }

    public boolean getHasFood() {
        return this.dataTracker.get(HAS_FOOD);
    }

    public void setHasFood(boolean hasFood) {
        this.dataTracker.set(HAS_FOOD, hasFood);
    }

    public BlockPos getTreasurePos() {
        return this.dataTracker.get(TREASURE_POS);
    }

    public void setTreasurePos(BlockPos blockPos) {
        this.dataTracker.set(TREASURE_POS, blockPos);
    }

    static class LeadToNearbyTreasureGoal extends Goal {
        private final MonkeyEntity monkey;
        private boolean noPathToStructure;

        LeadToNearbyTreasureGoal(MonkeyEntity monkey) {
            this.monkey = monkey;
            this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
        }

        public boolean canStop() {
            return false;
        }

        public boolean canStart() {
            return this.monkey.getHasFood();
        }

        public boolean shouldContinue() {
            BlockPos blockPos = this.monkey.getTreasurePos();
            return !(new BlockPos(blockPos.getX(), this.monkey.getY(), blockPos.getZ()))
                    .isWithinDistance(this.monkey.getPos(), 4.0D) && !this.noPathToStructure;
        }

        public void start() {
            if (this.monkey.world instanceof ServerWorld serverWorld) {
                this.noPathToStructure = false;
                this.monkey.getNavigation().stop();
                BlockPos blockPos = this.monkey.getBlockPos();
                BlockPos blockPos2 = serverWorld.locateStructure(StructureFeature.JUNGLE_PYRAMID, blockPos, 50, false);
                if (blockPos2 == null) {
                    this.noPathToStructure = true;
                    return;
                } else {
                    this.monkey.setTreasurePos(blockPos2);
                }

                serverWorld.sendEntityStatus(this.monkey, (byte) 38);
            }
        }

        public void stop() {
            BlockPos blockPos = this.monkey.getTreasurePos();
            if ((new BlockPos(blockPos.getX(), this.monkey.getY(), blockPos.getZ()))
                    .isWithinDistance(this.monkey.getPos(), 4.0D) || this.noPathToStructure) {
                this.monkey.setHasFood(false);
            }
        }

        public void tick() {
            World world = this.monkey.world;
            if (this.monkey.isNearTarget() || this.monkey.getNavigation().isIdle()) {
                Vec3d vec3d = Vec3d.ofCenter(this.monkey.getTreasurePos());
                Vec3d vec3d2 = NoPenaltyTargeting.find(this.monkey, 16, 1, vec3d, 0.39269909262657166D);
                if (vec3d2 == null) {
                    vec3d2 = NoPenaltyTargeting.find(this.monkey, 8, 4, vec3d, 1.5707963705062866D);
                }

                if (vec3d2 != null) {
                    BlockPos blockPos = new BlockPos(vec3d2);
                    if (!world.getBlockState(blockPos).canPathfindThrough(world, blockPos, NavigationType.LAND)) {
                        vec3d2 = NoPenaltyTargeting.find(this.monkey, 8, 5, vec3d, 1.5707963705062866D);
                    }
                }

                if (vec3d2 == null) {
                    this.noPathToStructure = true;
                    return;
                }

                this.monkey.getLookControl().lookAt(vec3d2.x, vec3d2.y, vec3d2.z, (this.monkey.getBodyYawSpeed() + 20),
                        this.monkey.getLookPitchSpeed());
                this.monkey.getNavigation().startMovingTo(vec3d2.x, vec3d2.y, vec3d2.z, 1.3D);
                if (world.random.nextInt(80) == 0) {
                    world.sendEntityStatus(this.monkey, (byte) 38);
                }
            }

        }
    }
}