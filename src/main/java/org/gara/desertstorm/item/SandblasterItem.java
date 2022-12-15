package org.gara.desertstorm.item;

import net.fabricmc.fabric.impl.object.builder.FabricEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.tag.ItemTags;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.gara.desertstorm.DesertStorm;
import org.gara.desertstorm.Utils;

import java.util.List;

public class SandblasterItem extends CustomTool {
	public SandblasterItem(Settings settings) {
		super("sandblaster", SandblasterMaterial.INSTANCE, settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand interactionHand) {
		// Creative, Infinity or Sand
		if (player.isCreative() || player.getInventory().contains(ItemTags.SAND)) {
			FallingBlockEntity sandEntity = Utils.CreateAndTeleport(FabricEntityType.FALLING_BLOCK, world, player.getEyePos());
			Utils.SetTimeFallingToMax(sandEntity);
			sandEntity.setVelocity(player.getRotationVector());
			world.spawnEntity(sandEntity);
			player.getItemCooldownManager().set(this, world.getGameRules().getInt(DesertStorm.SANDBLASTER_COOLDOWN));
			if (!(player.isCreative() || EnchantmentHelper.getLevel(Enchantments.INFINITY, player.getStackInHand(interactionHand)) >= 0)) {
				for (int i = 0; i < player.getInventory().size(); i++) {
					ItemStack stack = player.getInventory().getStack(i);
					if (stack.isIn(ItemTags.SAND)) {
						stack.decrement(1);
						break;
					}
				}
			}
			player.incrementStat(Stats.USED.getOrCreateStat(this));
		}

		return TypedActionResult.success(player.getStackInHand(interactionHand), world.isClient());
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