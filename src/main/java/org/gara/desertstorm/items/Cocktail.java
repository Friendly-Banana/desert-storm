package org.gara.desertstorm.items;

import java.util.List;

import org.gara.desertstorm.Utils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class Cocktail extends PotionItem {
    public final String identifier;
    public Cocktail(String id, Properties properties) {
        super(properties);
        identifier = id;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Level level, List<Component> list, TooltipFlag tooltipFlag) {
        // default white text
        TranslatableComponent tip = Utils.GetTooltip(this.identifier);
        if (!tip.getString().isEmpty()) {
            list.add(tip.withStyle(ChatFormatting.WHITE));
        }
    }
}