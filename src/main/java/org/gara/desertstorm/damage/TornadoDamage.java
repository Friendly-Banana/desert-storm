package org.gara.desertstorm.damage;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class TornadoDamage extends DamageSource {
    public TornadoDamage() {
        super("sandstorm");
        this.setScaledWithDifficulty();
    }

    public Text getDeathMessage(LivingEntity livingEntity) {
        return new TranslatableText("death.desertstom.tornado",
                livingEntity.getDisplayName());
    }
}