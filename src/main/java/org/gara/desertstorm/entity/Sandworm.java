package org.gara.desertstorm.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class Sandworm extends HostileEntity {
    public Sandworm(EntityType<? extends Sandworm> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3).add(EntityAttributes.GENERIC_MAX_HEALTH, 5);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 0.5D, false));
        this.goalSelector.add(3, new DisappearIntoGround(this));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true, true));
    }
}

class DisappearIntoGround extends Goal {
    private final Sandworm sandworm;

    public DisappearIntoGround(Sandworm sandworm) {
        this.sandworm = sandworm;
    }

    @Override
    public boolean canStart() {
        if (this.sandworm.getTarget() != null) {
            return false;
        }
        return this.sandworm.getRandom().nextInt(toGoalTicks(20)) <= this.sandworm.world.getDifficulty().getId();
    }

    @Override
    public void tick() {
        World world = this.sandworm.world;
        if (this.sandworm.age % (3 * 20) == 0) {
        }
    }
}