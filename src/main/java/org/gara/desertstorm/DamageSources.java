package org.gara.desertstorm;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class DamageSources {
    public final static DamageSource MOLOTOV = new CustomDamage("molotov").setExplosive();
    public final static DamageSource BARREL = new CustomDamage("barrel").setExplosive();
    public final static DamageSource TORNADO = new CustomDamage("tornado").setNeutral();

    private static class CustomDamage extends DamageSource {
        public CustomDamage(String name) {
            super(name);
            this.setScaledWithDifficulty();
        }

        public Text getDeathMessage(LivingEntity livingEntity) {
            return new TranslatableText("death.desertstorm." + this.name, livingEntity.getDisplayName());
        }
    }
}
