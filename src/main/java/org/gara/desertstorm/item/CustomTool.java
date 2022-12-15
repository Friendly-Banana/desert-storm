package org.gara.desertstorm.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.Vanishable;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.gara.desertstorm.Utils;

import java.util.List;

public class CustomTool extends ToolItem implements Vanishable {
	public final String identifier;

	public CustomTool(String id, ToolMaterial tier, Settings properties) {
		super(tier, properties);
		identifier = id;
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