package org.gara.desertstorm.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.gara.desertstorm.DamageSources;
import org.gara.desertstorm.Utils;

public class RollingBarrel extends HostileEntity {
    LivingEntity owner;

    public RollingBarrel(EntityType<? extends RollingBarrel> entityType, World world) {
        super(entityType, world);
    }

    public RollingBarrel(LivingEntity owner, World world, Vec3d pos) {
        this(DSEntities.ROLLING_BARREL, world);
        this.refreshPositionAfterTeleport(pos);
        this.owner = owner;
    }

    public static DefaultAttributeContainer.Builder createBarrelAttributes() {
        return createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5).add(EntityAttributes.GENERIC_MAX_HEALTH, 5);
    }

    @Override
    public void tick() {
        if (!this.world.isClient && age >= 5 * 20) {
            this.discard();
            return;
        }
        super.tick();
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 0.5D, false));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true, true));
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        super.onPlayerCollision(player);
        if (Utils.IsSurvival(player)) {
            player.damage(DamageSources.BARREL, 0.5f);
            this.world.createExplosion(owner, DamageSources.BARREL, null, this.getX(), this.getY(), this.getZ(), 5F, true, Explosion.DestructionType.NONE);
            this.discard();
        }
    }

    @Override
    public boolean isOnFire() {
        return true;
    }
}