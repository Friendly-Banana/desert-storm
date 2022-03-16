package org.gara.desertstorm.entity;

import net.minecraft.block.Blocks;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import org.gara.desertstorm.Utils;

public class Sandstorm extends AreaEffectCloudEntity {
    private static final ParticleEffect PARTICLE_EFFECT = new BlockStateParticleEffect(ParticleTypes.FALLING_DUST,
            Blocks.SAND.getDefaultState());
    public static EntityDimensions dimensions = EntityDimensions.fixed(10, 5);

    public Sandstorm(EntityType<? extends Sandstorm> entityType, World world) {
        super(entityType, world);
        // 3 minutes at 20 ticks per second
        this.setDuration(3 * 60 * 20);
        this.setRadius(dimensions.width);
        this.setParticleType(PARTICLE_EFFECT);
        this.setColor(Utils.SAND);
        this.addEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 1));
        this.addEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 10));
        this.setDuration(3 * 60 * 20);
        Utils.Log(getDuration());
    }

    @Override
    public void tick() {
        super.tick();
        this.setRadius(dimensions.width * (float) age / getDuration());
        if (world.isClient) {
            float radius = getRadius();
            double lastZ = this.getX();
            double lastX = this.getZ();
            for (int y = 0; y < dimensions.height; y++) {
                for (float angle = 0; angle < 360; angle += 30) {
                    // calculate points on circle
                    double radians = Math.toRadians(angle);
                    double x = this.getX() + radius * Math.cos(radians);
                    double z = this.getZ() + radius * Math.sin(radians);
                    this.world.addImportantParticle(PARTICLE_EFFECT, x, y + this.getY(), z, x - lastX, 1, z - lastZ);
                    lastX = x;
                    lastZ = z;
                }
            }
        }
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return dimensions;
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }
}