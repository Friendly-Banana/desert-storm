package org.gara.desertstorm;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.function.MaterialPredicate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.gara.desertstorm.entity.SandWither;

import java.util.function.Predicate;

public class SandWitherThings {
    public static final BlockPattern BLOCK_PATTERN;
    public static final BlockPattern DISPENSER_PATTERN;
    private static final Predicate<CachedBlockPosition> MATCHES_AIR = CachedBlockPosition.matchesBlockState(MaterialPredicate.create(Material.AIR));

    static {
        BLOCK_PATTERN = BlockPatternBuilder.start().aisle("^^^", "###", "~#~").where('#', pos -> pos.getBlockState().isOf(Blocks.SANDSTONE) || pos.getBlockState().isOf(Blocks.RED_SANDSTONE)).where('^', pos -> pos.getBlockState().isOf(Blocks.SKELETON_SKULL) || pos.getBlockState().isOf(Blocks.SKELETON_WALL_SKULL)).where('~', MATCHES_AIR).build();
        DISPENSER_PATTERN = BlockPatternBuilder.start().aisle("   ", "###", "~#~").where('#', pos -> pos.getBlockState().isOf(Blocks.SANDSTONE) || pos.getBlockState().isOf(Blocks.RED_SANDSTONE)).where('~', MATCHES_AIR).build();
    }

    public static boolean canDispense(World world, BlockPos pos, ItemStack stack) {
        if (stack.isOf(Items.SKELETON_SKULL) && pos.getY() >= world.getBottomY() + 2 && world.getDifficulty() != Difficulty.PEACEFUL && !world.isClient) {
            return SandWitherThings.DISPENSER_PATTERN.searchAround(world, pos) != null;
        }
        return false;
    }

    public static void onPlaced(World world, BlockPos pos, SkullBlockEntity blockEntity) {
        if (world.isClient) {
            return;
        }
        BlockState blockState = blockEntity.getCachedState();
        boolean bl = blockState.isOf(Blocks.SKELETON_SKULL) || blockState.isOf(Blocks.SKELETON_WALL_SKULL);
        if (!bl || pos.getY() < world.getBottomY() || world.getDifficulty() == Difficulty.PEACEFUL) {
            return;
        }
        BlockPattern.Result result = BLOCK_PATTERN.searchAround(world, pos);
        if (result == null) {
            return;
        }
        for (int i = 0; i < BLOCK_PATTERN.getWidth(); ++i) {
            for (int j = 0; j < BLOCK_PATTERN.getHeight(); ++j) {
                CachedBlockPosition cachedBlockPosition = result.translate(i, j, 0);
                world.setBlockState(cachedBlockPosition.getBlockPos(), Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, cachedBlockPosition.getBlockPos(), Block.getRawIdFromState(cachedBlockPosition.getBlockState()));
            }
        }
        SandWither wither = DesertStorm.SAND_WITHER.create(world);
        BlockPos bottomBlock = result.translate(1, 2, 0).getBlockPos();
        wither.refreshPositionAndAngles(bottomBlock.getX() + 0.5, bottomBlock.getY() + 0.55, bottomBlock.getZ() + 0.5, result.getForwards().getAxis() == Direction.Axis.X ? 0.0f : 90.0f, 0.0f);
        wither.bodyYaw = result.getForwards().getAxis() == Direction.Axis.X ? 0.0f : 90.0f;
        wither.onSummoned();
        for (ServerPlayerEntity serverPlayerEntity : world.getNonSpectatingEntities(ServerPlayerEntity.class, wither.getBoundingBox().expand(50.0))) {
            Criteria.SUMMONED_ENTITY.trigger(serverPlayerEntity, wither);
        }
        world.spawnEntity(wither);
        // destroy blocks used to create the wither
        for (int i = 0; i < BLOCK_PATTERN.getWidth(); ++i) {
            for (int j = 0; j < BLOCK_PATTERN.getHeight(); ++j) {
                world.updateNeighbors(result.translate(i, j, 0).getBlockPos(), Blocks.AIR);
            }
        }
    }
}
