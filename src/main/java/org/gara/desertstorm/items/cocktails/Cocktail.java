package org.gara.desertstorm.items.cocktails;

import org.gara.desertstorm.mixin.PotionAccessor;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;

public class Cocktail extends Potion {

    public Cocktail(String name, StatusEffectInstance... statusEffectInstances) {
        super(name, statusEffectInstances);
    }

    public String getName()
    {
        return ((PotionAccessor)this).getBaseName();
    }
}