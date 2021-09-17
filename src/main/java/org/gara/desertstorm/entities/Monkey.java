package org.gara.desertstorm.entities;

import java.util.EnumSet;

import org.gara.desertstorm.DesertStorm;
import org.gara.desertstorm.Utils;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.StructureFeature;

public class Monkey extends AnimalEntity {

    private BlockPos blockPos;
    private boolean hasBanana;

    public Monkey(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createMonkeyAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2F);
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
    public PassiveEntity createChild(ServerWorld world, PassiveEntity other) {
        return DesertStorm.MONKEY.create(world);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(DesertStorm.BANANA_ITEM);
    }

    protected boolean isNearTarget() {
        BlockPos blockPos = this.getNavigation().getTargetPos();
        return blockPos != null ? blockPos.isWithinDistance(this.getPos(), 12.0D) : false;
    }

    public boolean getHasBanana() {
        return this.hasBanana;
    }

    public void setHasBanana(boolean hasBanana) {
        this.hasBanana = hasBanana;
    }

    public BlockPos getTreasurePos() {
        return this.blockPos;
    }

    public void setTreasurePos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    static class LeadToNearbyTreasureGoal extends Goal {
        private final Monkey monkey;
        private boolean noPathToStructure;

        LeadToNearbyTreasureGoal(Monkey monkey) {
            this.monkey = monkey;
            this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
        }

        public boolean canStop() {
            Utils.Debug();
            return false;
        }

        public boolean canStart() {
            Utils.Debug();
            return this.monkey.getHasBanana();
        }

        public boolean shouldContinue() {
            Utils.Debug();
            BlockPos blockPos = this.monkey.getTreasurePos();
            return !(new BlockPos(blockPos.getX(), this.monkey.getY(), blockPos.getZ()))
                    .isWithinDistance(this.monkey.getPos(), 4.0D) && !this.noPathToStructure;
        }

        public void start() {
            Utils.Debug();
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
            Utils.Debug();
            BlockPos blockPos = this.monkey.getTreasurePos();
            if ((new BlockPos((double) blockPos.getX(), this.monkey.getY(), (double) blockPos.getZ()))
                    .isWithinDistance(this.monkey.getPos(), 4.0D) || this.noPathToStructure) {
                this.monkey.setHasBanana(false);
            }

        }

        public void tick() {
            World world = this.monkey.world;
            if (true ||this.monkey.isNearTarget() || this.monkey.getNavigation().isIdle()) {
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

                this.monkey.getLookControl().lookAt(vec3d2.x, vec3d2.y, vec3d2.z,
                        (this.monkey.getBodyYawSpeed() + 20), this.monkey.getLookPitchSpeed());
                this.monkey.getNavigation().startMovingTo(vec3d2.x, vec3d2.y, vec3d2.z, 1.3D);
                if (world.random.nextInt(80) == 0) {
                    world.sendEntityStatus(this.monkey, (byte) 38);
                }
            }

        }
    }
}