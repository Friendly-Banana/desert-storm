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
    }
}