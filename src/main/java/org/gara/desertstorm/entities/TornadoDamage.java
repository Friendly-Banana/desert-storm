package org.gara.desertstorm.entities;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class TornadoDamage extends DamageSource {
    protected TornadoDamage() {
        super("sandstorm");
        this.setScalesWithDifficulty();
    }

    public Component getLocalizedDeathMessage(LivingEntity livingEntity) {
        return new TranslatableComponent("death.desertstom.tornado",
                new Object[] { livingEntity.getDisplayName() });
    }
}