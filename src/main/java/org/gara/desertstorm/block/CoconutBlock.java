package org.gara.desertstorm.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class CoconutBlock extends FallingBlock {
	private static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);
	private static final double FALL_CHANCE = 0.01;
	private static final double FALL_CHANCE_NEAR_PLAYER = 0.1;

	public CoconutBlock() {
		super(FabricBlockSettings.of(Material.WOOD).strength(2.0f).nonOpaque());
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		super.neighborUpdate(state, world, pos, block, fromPos, notify);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState blockState, BlockView blockGetter, BlockPos blockPos, ShapeContext collisionContext) {
		return SHAPE;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (random.nextDouble() <= (world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 10, true) == null ? FALL_CHANCE : FALL_CHANCE_NEAR_PLAYER)) {
			super.scheduledTick(state, world, pos, random);
		}
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		BlockState blockState = world.getBlockState(pos.up());
		boolean hanging = blockState.isIn(BlockTags.LEAVES) || blockState.isOf(Blocks.CHAIN);
		if (!hanging) {
			// fall down
			super.scheduledTick(state, world, pos, random);
		}
	}

	@Override
	protected void configureFallingBlockEntity(FallingBlockEntity entity) {
		entity.setHurtEntities(2.0F, 40);
	}
}