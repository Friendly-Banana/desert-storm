package org.gara.desertstorm.item;

import net.fabricmc.fabric.impl.object.builder.FabricEntityType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SandblasterItem extends CustomTool {
    public SandblasterItem(Settings properties) {
        super("sandblaster", SandblasterMaterial.INSTANCE, properties);
    }

    @Override
    public TypedActionResult<ItemStack> use(World level, PlayerEntity player, Hand interactionHand) {
        int slot = player.getInventory().getSlotWithStack(new ItemStack(Items.SAND));
        // Creative, Infinity or Sand
        if (player.isCreative() || slot != -1) {
            FallingBlockEntity sandEntity = FabricEntityType.FALLING_BLOCK.create(level);
            sandEntity.setPosition(player.getEyePos());
            level.spawnEntity(sandEntity);
            sandEntity.timeFalling = 1;
            sandEntity.setVelocity(player.getRotationVector().add(0, 1, 0));
            player.getItemCooldownManager().set(this, 10);
            if (!(player.isCreative() || EnchantmentHelper.getLevel(Enchantments.INFINITY,
                    player.getStackInHand(interactionHand)) >= 0)) {
                player.getInventory().getStack(slot).decrement(1);
            }

            player.incrementStat(Stats.USED.getOrCreateStat(this));
        }

        return TypedActionResult.success(player.getStackInHand(interactionHand), level.isClient());
    }
}