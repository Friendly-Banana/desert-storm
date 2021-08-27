package org.gara.desertstorm.items;

import java.util.List;

import org.gara.desertstorm.Utils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class CustomTool extends TieredItem implements Vanishable/* , AttackAction */ {
    public final String identifier;

    public CustomTool(String id, Tier tier, Properties properties) {
        super(tier, properties);
        identifier = id;
    }

    public void OnLeftClick(Player player, InteractionHand hand) {
        Utils.Log("OnLeftClick: " + identifier);
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