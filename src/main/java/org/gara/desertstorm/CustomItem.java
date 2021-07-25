package org.gara.desertstorm;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class CustomItem extends Item {
    public static String identifier;

    public CustomItem(String id, Properties properties) {
        super(properties);
        identifier = id;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Level level, List<Component> list,
            TooltipFlag tooltipFlag) {
                // default white text
                list.add((Utils.GetTooltip(identifier)).withStyle(ChatFormatting.WHITE));
    }
}