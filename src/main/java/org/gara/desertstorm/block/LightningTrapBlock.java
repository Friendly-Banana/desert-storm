package org.gara.desertstorm.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.gara.desertstorm.DesertStorm;
import org.gara.desertstorm.Utils;

public class LightningTrapBlock extends Block implements BlockEntityProvider {

    public static final BooleanProperty CHARGED = BooleanProperty.of("charged");

    public LightningTrapBlock() {
        super(FabricBlockSettings.of(Material.STONE).strength(4.0f));
        setDefaultState(getDefaultState().with(CHARGED, false));
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LightningTrapBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState blockState, World level, BlockPos blockPos, PlayerEntity player,
                              Hand interactionHand, BlockHitResult blockHitResult) {
        if (!blockState.get(CHARGED)
                && (player.isHolding(DesertStorm.BATTERY_ITEM) || player.isHolding(Items.TRIDENT)
                && EnchantmentHelper.hasChanneling(player.getStackInHand(interactionHand)))) {
            player.playSound(SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, 1, 1);
            level.setBlockState(blockPos, blockState.with(CHARGED, true));
            if (Utils.IsSurvival(player)) {
                player.getStackInHand(interactionHand).decrement(1);
            }
            player.incrementStat(Stats.USED.getOrCreateStat(DesertStorm.LIGHTNING_TRAP_ITEM));
            player.incrementStat(Stats.USED.getOrCreateStat(DesertStorm.BATTERY_ITEM));
            return ActionResult.CONSUME;
        }
        return ActionResult.PASS;
    }

    @Override
    public void onSteppedOn(World world, BlockPos blockPos, BlockState blockState, Entity entity) {
        if (blockState.get(CHARGED)) {
            // Only react for survival
            if (entity instanceof PlayerEntity) {
                if (!Utils.IsSurvival((PlayerEntity) entity))
                    return;
            }
            // Summon the Lighting Bolt at the block
            LightningEntity lightningBolt = Utils.CreateAndTeleport(EntityType.LIGHTNING_BOLT, world, entity.getEyePos());
            world.spawnEntity(lightningBolt);
            world.setBlockState(blockPos, blockState.with(CHARGED, false));
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(CHARGED);
    }
}