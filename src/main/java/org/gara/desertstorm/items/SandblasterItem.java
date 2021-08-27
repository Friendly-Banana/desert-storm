package org.gara.desertstorm.items;

import net.fabricmc.fabric.impl.object.builder.FabricEntityType;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class SandblasterItem extends CustomTool {
    public SandblasterItem(Properties properties) {
        super("sandblaster", SandblasterMaterial.INSTANCE, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        int slot = player.getInventory().findSlotMatchingItem(new ItemStack(Items.SAND));
        // Creative, Infinity or Sand
        if (player.isCreative() || slot != -1) {
            FallingBlockEntity sandEntity = FabricEntityType.FALLING_BLOCK.create(level);
            sandEntity.setPos(player.getEyePosition());
            level.addFreshEntity(sandEntity);
            sandEntity.time = 1;
            sandEntity.setDeltaMovement(player.getLookAngle().add(0, 1, 0));
            player.getCooldowns().addCooldown(this, 10);
            if (!(player.isCreative() || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS,
                    player.getItemInHand(interactionHand)) >= 0)) {
                player.getInventory().getItem(slot).shrink(1);
            }
            
            player.awardStat(Stats.ITEM_USED.get(this));
        }

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(interactionHand), level.isClientSide());
    }
}