package org.gara.desertstorm.item.cocktail;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.gara.desertstorm.DesertStorm;

import java.util.List;

public class CocktailItem extends PotionItem {
    private static final MutableText EMPTY_TEXT = (new TranslatableText("effect.none")).formatted(Formatting.GRAY);

    public CocktailItem(String id, Settings properties) {
        super(properties);
    }

    @Override
    public String getTranslationKey(ItemStack itemStack) {
        return CocktailUtil.getCocktail(itemStack).finishTranslationKey(this.getTranslationKey());
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World level, List<Text> list, TooltipContext tooltipFlag) {
        list.add(new TranslatableText(getTranslationKey(itemStack)).formatted(Formatting.WHITE));
        TranslatableText mutableText;
        List<StatusEffectInstance> effects = CocktailUtil.getCocktail(itemStack).getEffects();
        if (effects.isEmpty()) {
            list.add(EMPTY_TEXT);
        }
        for (StatusEffectInstance statusEffectInstance : effects) {
            mutableText = new TranslatableText(statusEffectInstance.getTranslationKey());
            mutableText = new TranslatableText("potion.withDuration", mutableText, StatusEffectUtil.durationToString(statusEffectInstance, 1));
            list.add(mutableText.formatted(statusEffectInstance.getEffectType().getCategory().getFormatting()));
        }
        TranslatableText tip = new TranslatableText(getTranslationKey(itemStack) + ".tooltip");
        if (!tip.getString().isEmpty()) {
            list.add(tip.formatted(Formatting.WHITE));
        }
    }

    @Override
    public ItemStack getDefaultStack() {
        return CocktailUtil.setCocktail(super.getDefaultStack(), Cocktails.EMPTY);
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (group == DesertStorm.ITEM_TAB) {
            for (Cocktail cocktail : DesertStorm.COCKTAIL_REGISTRY) {
                stacks.add(CocktailUtil.setCocktail(new ItemStack(this), cocktail));
            }
        }
    }
}