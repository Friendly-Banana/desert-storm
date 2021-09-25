package org.gara.desertstorm.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.gara.desertstorm.DesertStorm;
import org.gara.desertstorm.DamageSources;
import org.jetbrains.annotations.Nullable;

public class RollingBarrel extends HostileEntity {
    LivingEntity owner;

    public RollingBarrel(EntityType<? extends RollingBarrel> entityType, World world) {
        super(entityType, world);
    }

    public RollingBarrel(@Nullable LivingEntity owner, World world, Vec3d pos) {
        this(DesertStorm.ROLLING_BARREL, world);
        this.refreshPositionAfterTeleport(pos);
        this.owner = owner;
    }

    public static DefaultAttributeContainer.Builder createBarrelAttributes() {
        return createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 2.5);
    }

    @Override
    protected void onBlockCollision(BlockState state) {
        super.onBlockCollision(state);
        world.createExplosion(owner, DamageSources.BARREL, null, this.getX(), this.getY(), this.getZ(), 1.5F, true, Explosion.DestructionType.NONE);
    }
}