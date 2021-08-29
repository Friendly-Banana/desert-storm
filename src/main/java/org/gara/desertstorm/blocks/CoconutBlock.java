package org.gara.desertstorm.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.ShapeContext;
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
        return levelReader.getBlockState(blockPos.up()).isIn(BlockTags.LEAVES) || levelReader.getBlockState(blockPos.down()).isSolidBlock(levelReader, blockPos.down());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockGetter, BlockPos blockPos, ShapeContext collisionContext) {
        return SHAPE;
     }
}