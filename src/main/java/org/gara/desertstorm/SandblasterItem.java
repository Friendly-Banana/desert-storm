package org.gara.desertstorm;

import net.fabricmc.fabric.impl.object.builder.FabricEntityType;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SandblasterItem extends CustomTool {
    public SandblasterItem(Properties properties) {
        super("sandblaster", SandblasterMaterial.INSTANCE, properties);
    }

    @Override
    public void onAttack(Player player, InteractionHand hand) {
        Level level = player.level;
        if (!level.isClientSide) {
            FallingBlockEntity sandEntity = FabricEntityType.FALLING_BLOCK.create(level);
            sandEntity.setPos(player.getEyePosition());
            level.addFreshEntity(sandEntity);
            sandEntity.time = 1;
            sandEntity.setDeltaMovement(player.getLookAngle());
            player.getCooldowns().addCooldown(this, 10);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return true;
    }
}