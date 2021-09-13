package org.gara.desertstorm.item.cocktail;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.gara.desertstorm.DesertStorm;

import java.util.List;

public class CocktailItem extends PotionItem {
    public final String identifier;

    public CocktailItem(String id, Settings properties) {
        super(properties);
        identifier = id;
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return CocktailUtil.getCocktail(stack).finishTranslationKey(this.getTranslationKey());
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World level, List<Text> list, TooltipContext tooltipFlag) {
        super.appendTooltip(itemStack, level, list, tooltipFlag);
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
        if (this.isIn(group)) {
            for (Cocktail cocktail : DesertStorm.COCKTAIL_REGISTRY) {
                if (cocktail != Cocktails.EMPTY) {
                    stacks.add(CocktailUtil.setCocktail(new ItemStack(this), cocktail));
                }
            }
        }
    }
}