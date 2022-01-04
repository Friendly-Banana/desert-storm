package org.gara.desertstorm.entity;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import org.gara.desertstorm.Utils;

public class Sandstorm extends AreaEffectCloudEntity {
    public Sandstorm(EntityType<? extends Sandstorm> entityType, World world) {
        super(entityType, world);
        // 3 minutes at 20 ticks per second
        this.setDuration(3 * 60 * 20);
        this.setRadius(10);
        this.setParticleType(ParticleTypes.DRAGON_BREATH);
        this.setColor(Utils.SAND);
        this.addEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 1));
        this.addEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 10));
    }

    @Override
    public void tick() {
        super.tick();
        this.setRadius(10 * (float) age / getDuration());
        /*if (world.isClient) {
            double lastZ = this.getX();
            double lastX = this.getZ();
            for (int y = 0; y < dimensions.height; y++) {
                float radius = dimensions.width - y;
                for (float angle = 0; angle < 360; angle += 30) {
                    // calculate points on circle
                    double radians = Math.toRadians(angle);
                    double x = this.getX() + radius * Math.cos(radians);
                    double z = this.getZ() + radius * Math.sin(radians);
                    this.world.addImportantParticle(particleOptions, x, this.getY() + y, z, x - lastX, 1, z - lastZ);
                    lastX = x;
                    lastZ = z;
                }
            }
        }*/
    }
}