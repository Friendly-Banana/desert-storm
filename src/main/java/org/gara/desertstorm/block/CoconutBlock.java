package org.gara.desertstorm.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

import java.util.Random;

public class CoconutBlock extends FallingBlock {
    // minX, minY, minZ, maxX, maxY, maxZ
    private static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);

    public CoconutBlock() {
        super(FabricBlockSettings.of(Material.WOOD).strength(2.0f).breakByTool(FabricToolTags.AXES).nonOpaque());
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