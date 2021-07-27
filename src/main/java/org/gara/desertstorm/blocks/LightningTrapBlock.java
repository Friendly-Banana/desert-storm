package org.gara.desertstorm.blocks;

import org.gara.desertstorm.DesertStorm;
import org.gara.desertstorm.Utils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class LightningTrapBlock extends Block implements EntityBlock {

    public static final BooleanProperty CHARGED = BooleanProperty.create("charged");

    public LightningTrapBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(CHARGED, false));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos arg0, BlockState arg1) {
        return new LightningTrapBlockEntity(arg0, arg1);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player,
            InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClientSide && !blockState.getValue(CHARGED) && player.isHolding(DesertStorm.BATTERY_ITEM)) {
            player.playSound(SoundEvents.RESPAWN_ANCHOR_CHARGE, 1, 1);
            level.setBlockAndUpdate(blockPos, blockState.setValue(CHARGED, true));
            // Set Owner
            level.getBlockEntity(blockPos, DesertStorm.TRAP_BLOCK_ENTITY)
                    .ifPresent((LightningTrapBlockEntity blockEntity) -> {
                        blockEntity.SetOwner(player.getGameProfile());
                    });
        }

        player.awardStat(Stats.ITEM_USED.get(Item.byBlock(this)));
        if (Utils.IsSurvival(player)) {
            player.getItemInHand(interactionHand).shrink(1);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void stepOn(Level level, BlockPos blockPos, BlockState blockState, Entity entity) {
        if (!level.isClientSide && blockState.getValue(CHARGED)) {
            LightningTrapBlockEntity blockEntity = level.getBlockEntity(blockPos, DesertStorm.TRAP_BLOCK_ENTITY).get();
            // Only react for survival
            if (entity instanceof Player) {
                if (!Utils.IsSurvival((Player) entity))
                    return;
            }
            // Summoning the Lighting Bolt at the block
            LightningBolt lightningBolt = (LightningBolt) EntityType.LIGHTNING_BOLT.create(level);
            lightningBolt.moveTo(entity.getPosition(0));
            lightningBolt.setCause((ServerPlayer) level.getPlayerByUUID(blockEntity.GetOwner().getId()));
            level.addFreshEntity(lightningBolt);
            level.setBlockAndUpdate(blockPos, blockState.setValue(CHARGED, false));
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CHARGED);
    }
}