package org.gara.desertstorm;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class CustomTool extends TieredItem implements Vanishable { // AttackAction
    public static String identifier;

    public CustomTool(String id, Tier tier, Properties properties) {
        super(tier, properties);
        identifier = id;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Level level, List<Component> list,
            TooltipFlag tooltipFlag) {     
                // default white text
                list.add((Utils.GetTooltip(identifier)).withStyle(ChatFormatting.WHITE));
    }

    public void onAttack(Player player, InteractionHand hand)
    {
        Utils.Log("onAttack: " + identifier);
    }
}