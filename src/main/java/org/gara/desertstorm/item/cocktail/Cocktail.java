package org.gara.desertstorm.item.cocktail;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.util.Identifier;
import org.gara.desertstorm.DesertStorm;

public class Cocktail extends Potion {
    public final String name;

    public Cocktail(String name, StatusEffectInstance... statusEffectInstances) {
        super(name, statusEffectInstances);
        this.name = name;
    }

    public static Cocktail byId(String id) {
        return DesertStorm.COCKTAIL_REGISTRY.get(Identifier.tryParse(id));
    }

    @Override
    public String finishTranslationKey(String prefix) {
        return prefix + "." + name;
    }
}