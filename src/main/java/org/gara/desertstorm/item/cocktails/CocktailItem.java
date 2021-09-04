package org.gara.desertstorm.item.cocktails;

import java.util.Iterator;
import java.util.List;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class CocktailItem extends PotionItem {
    public final String identifier;
    public CocktailItem(String id, Settings properties) {
        super(properties);
        identifier = id;
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
        return PotionUtil.setPotion(super.getDefaultStack(), Cocktails.EMPTY);
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            Iterator<Potion> cocktails = Registry.POTION.iterator();
   
            while (cocktails.hasNext()) {
                Potion cocktail = cocktails.next();
               if (cocktail != Cocktails.EMPTY) {
                  stacks.add(PotionUtil.setPotion(new ItemStack(this), cocktail));
               }
            }
         }
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
       return PotionUtil.getPotion(stack).finishTranslationKey(this.getTranslationKey() + ".effect.");
    }
}