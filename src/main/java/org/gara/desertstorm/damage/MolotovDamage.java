package org.gara.desertstorm.damage;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class MolotovDamage extends DamageSource {
    public MolotovDamage() {
        super("molotov");
        this.setScaledWithDifficulty();
    }

    public Text getDeathMessage(LivingEntity livingEntity) {
        return new TranslatableText("death.desertstom.molotov",
                livingEntity.getDisplayName());
    }
}