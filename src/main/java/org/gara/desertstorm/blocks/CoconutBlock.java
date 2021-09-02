package org.gara.desertstorm.blocks;

import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

public class CoconutBlock extends FallingBlock {
    // minX, minY, minZ, maxX, maxY, maxZ
    private static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);

    public CoconutBlock(Settings properties) {
        super(properties);
    }

    @Override
    public boolean canPlaceAt(BlockState blockState, WorldView levelReader, BlockPos blockPos) {
        // can only be placed beneath leaves or on Ground
        return levelReader.getBlockState(blockPos.up()).isIn(BlockTags.LEAVES)
                || levelReader.getBlockState(blockPos.down()).isSolidBlock(levelReader, blockPos.down());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockGetter, BlockPos blockPos,
            ShapeContext collisionContext) {
        return SHAPE;
    }

    @Override
    protected void configureFallingBlockEntity(FallingBlockEntity entity) {
        entity.setHurtEntities(2.0F, 40);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockState blockState = world.getBlockState(pos.up());
        boolean hanging = blockState.isIn(BlockTags.LEAVES) || blockState.isOf(Blocks.CHAIN);
        if (!hanging) {
            // check falling down
            super.scheduledTick(state, world, pos, random);
        }
    }
}