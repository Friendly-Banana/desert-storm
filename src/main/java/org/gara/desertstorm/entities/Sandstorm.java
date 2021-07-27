package org.gara.desertstorm.entities;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class Sandstorm extends AreaEffectCloud {
    public Sandstorm(EntityType<? extends AreaEffectCloud> entityType, Level level) {
        super(entityType, level);
        /*Entity entity = this.getOwner();
        if (entity instanceof LivingEntity) {
           areaEffectCloud.setOwner((LivingEntity)entity);
        }

        areaEffectCloud.setParticle(ParticleTypes.DUST);
        areaEffectCloud.setRadius(3.0F);
        areaEffectCloud.setDuration(600);
        areaEffectCloud.setRadiusPerTick((7.0F - areaEffectCloud.getRadius()) / (float)areaEffectCloud.getDuration());
        areaEffectCloud.addEffect(new MobEffectInstance(MobEffects.HARM, 1, 1))*/
    }
}