package org.gara.desertstorm.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class TornadoDamage extends DamageSource {
    protected TornadoDamage() {
        super("sandstorm");
        this.setScaledWithDifficulty();
    }

    public Text getDeathMessage(LivingEntity livingEntity) {
        return new TranslatableText("death.desertstom.tornado",
                new Object[] { livingEntity.getDisplayName() });
    }
}