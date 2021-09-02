package org.gara.desertstorm.blocks;

import org.gara.desertstorm.DesertStorm;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class MixerBlock extends BlockWithEntity {
    public static final BooleanProperty[] BOTTLE_PROPERTIES;
    protected static final VoxelShape SHAPE;

    public MixerBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            // This will call the createScreenHandlerFactory method from BlockWithEntity,
            // which will return our blockEntity casted to
            // a NamedScreenHandlerFactory. If your block class does not extend
            // BlockWithEntity, it needs to implement createScreenHandlerFactory.
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);

            if (screenHandlerFactory != null) {
                // With this call the server will request the client to open the appropriate
                // Screenhandler
                player.openHandledScreen(screenHandlerFactory);
                player.incrementStat(Stats.USED.getOrCreateStat(DesertStorm.MIXER_ITEM));
            }
            return ActionResult.CONSUME;
        }
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MixerBlockEntity(pos, state);
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (itemStack.hasCustomName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof MixerBlockEntity) {
                ((MixerBlockEntity) blockEntity).setCustomName(itemStack.getName());
            }
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        // With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need
        // to change that!
        return BlockRenderType.MODEL;
    }

    // This method will drop all items onto the ground when the block is broken
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof MixerBlockEntity) {
                ItemScatterer.spawn(world, pos, ((MixerBlockEntity) blockEntity).getItems());
                // update comparators
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state,
            BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, DesertStorm.MIXER_BLOCK_ENTITY, MixerBlockEntity::tick);
    }

    public static VoxelShape getShape() {
        return SHAPE;
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(BOTTLE_PROPERTIES[0], BOTTLE_PROPERTIES[1], BOTTLE_PROPERTIES[2]);
    }

    static {
        BOTTLE_PROPERTIES = new BooleanProperty[] { Properties.HAS_BOTTLE_0, Properties.HAS_BOTTLE_1,
                Properties.HAS_BOTTLE_2 };
        SHAPE = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 2.0D, 15.0D);
    }
}