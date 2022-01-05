package org.gara.desertstorm.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.mob.MobEntity;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class FollowClassGoal extends Goal {
    private final MobEntity mob;
    private final TargetPredicate targetPredicate;
    private final Class<? extends LivingEntity> targetClass;
    private final double speed;
    private final EntityNavigation navigation;
    private LivingEntity target;
    private int updateCountdownTicks;

    public FollowClassGoal(MobEntity mob, Class<? extends LivingEntity> targetClass, double speed, float maxDistance) {
        this.mob = mob;
        this.targetClass = targetClass;
        this.speed = speed;
        this.navigation = mob.getNavigation();
        this.targetPredicate = TargetPredicate.createAttackable().setBaseMaxDistance(maxDistance).setPredicate(target -> target != null && mob.getClass() != target.getClass() && !target.isSpectator());
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK, Control.TARGET));
    }

    @Nullable
    private LivingEntity getClosestEntity() {
        return this.mob.world.<LivingEntity>getClosestEntity(targetClass, targetPredicate, this.mob, this.mob.getX(), this.mob.getY(), this.mob.getZ(), this.mob.getBoundingBox());
    }

    @Override
    public boolean canStart() {
        LivingEntity closestEntity = getClosestEntity();
        if (closestEntity != null) {
            this.target = closestEntity;
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldContinue() {
        return this.target != null && !this.navigation.isIdle();
    }

    @Override
    public void start() {
        this.updateCountdownTicks = 0;
    }

    @Override
    public void stop() {
        this.target = null;
        this.navigation.stop();
    }

    @Override
    public void tick() {
        this.target = getClosestEntity();
        if (this.target == null || this.mob.isLeashed()) {
            return;
        }
        this.mob.getLookControl().lookAt(this.target, 10.0f, this.mob.getMaxLookPitchChange());
        if (--this.updateCountdownTicks > 0) {
            return;
        }
        mob.setCustomName(target.getDisplayName());
        this.updateCountdownTicks = this.getTickCount(10);
        this.navigation.startMovingTo(this.target, this.speed);
    }
}