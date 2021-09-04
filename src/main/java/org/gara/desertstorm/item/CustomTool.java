package org.gara.desertstorm.item;

import java.util.List;

import org.gara.desertstorm.Utils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.Vanishable;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class CustomTool extends ToolItem implements Vanishable/* , AttackAction */ {
    public final String identifier;

    public CustomTool(String id, ToolMaterial tier, Settings properties) {
        super(tier, properties);
        identifier = id;
    }

    public void OnLeftClick(PlayerEntity player, Hand hand) {
        Utils.Log("OnLeftClick: " + identifier);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World level, List<Text> list, TooltipContext tooltipFlag) {
        // default white text
        TranslatableText tip = Utils.GetTooltip(this.identifier);
        if (!tip.getString().isEmpty()) {
            list.add(tip.formatted(Formatting.WHITE));
        }
    }
}