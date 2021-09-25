package org.gara.desertstorm;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class DamageSources {
    public final static DamageSource MOLOTOV = new CustomDamage("molotov");
    public final static DamageSource BARREL = new CustomDamage("barrel");
    public final static DamageSource TORNADO = new CustomDamage("tornado");

    private static class CustomDamage extends DamageSource {
        public CustomDamage(String name) {
            super(name);
            this.setScaledWithDifficulty();
        }

        public Text getDeathMessage(LivingEntity livingEntity) {
            return new TranslatableText("death.desertstorm." + this.name,
                    livingEntity.getDisplayName());
        }
    }
}
