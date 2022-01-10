package org.gara.desertstorm.mixin;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.function.MaterialPredicate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.gara.desertstorm.DesertStorm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(Block.class)
@SuppressWarnings({"ConstantConditions"})
public abstract class BlockMixin {
    private static final BlockPattern witherBossPattern;
    private static final BlockPattern witherDispenserPattern;

    private static final Predicate<CachedBlockPosition> MATCHES_AIR = CachedBlockPosition.matchesBlockState(MaterialPredicate.create(Material.AIR));

    static {
        witherBossPattern = BlockPatternBuilder.start().aisle("^^^", "###", "~#~").where('#', pos -> pos.getBlockState().isOf(Blocks.SANDSTONE) || pos.getBlockState().isOf(Blocks.RED_SANDSTONE)).where('^', pos -> pos.getBlockState().isOf(Blocks.SKELETON_SKULL) || pos.getBlockState().isOf(Blocks.SKELETON_WALL_SKULL)).where('~', MATCHES_AIR).build();
        witherDispenserPattern = BlockPatternBuilder.start().aisle("   ", "###", "~#~").where('#', pos -> pos.getBlockState().isOf(Blocks.SANDSTONE) || pos.getBlockState().isOf(Blocks.RED_SANDSTONE)).where('~', MATCHES_AIR).build();
    }

    private static boolean canDispense(World world, BlockPos pos, ItemStack stack) {
        if (stack.isOf(Items.SKELETON_SKULL) && pos.getY() >= world.getBottomY() + 2 && world.getDifficulty() != Difficulty.PEACEFUL && !world.isClient) {
            return witherDispenserPattern.searchAround(world, pos) != null;
        }
        return false;
    }

    @Inject(method = "onPlaced", at = @At(value = "TAIL"))
    private void spawnSandWither(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
        if ((Object) this instanceof SkullBlock skullBlock && skullBlock.getSkullType() == SkullBlock.Type.SKELETON) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (world.isClient) {
                return;
            }
            BlockState blockState = blockEntity.getCachedState();
            boolean bl = blockState.isOf(Blocks.SKELETON_SKULL) || blockState.isOf(Blocks.SKELETON_WALL_SKULL);
            if (!bl || pos.getY() < world.getBottomY() || world.getDifficulty() == Difficulty.PEACEFUL) {
                return;
            }
            BlockPattern.Result result = witherBossPattern.searchAround(world, pos);
            if (result == null) {
                return;
            }
            for (int i = 0; i < witherBossPattern.getWidth(); ++i) {
                for (int j = 0; j < witherBossPattern.getHeight(); ++j) {
                    CachedBlockPosition cachedBlockPosition = result.translate(i, j, 0);
                    world.setBlockState(cachedBlockPosition.getBlockPos(), Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
                    world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, cachedBlockPosition.getBlockPos(), Block.getRawIdFromState(cachedBlockPosition.getBlockState()));
                }
            }
            WitherEntity wither = DesertStorm.SAND_WITHER.create(world);
            BlockPos bottomBlock = result.translate(1, 2, 0).getBlockPos();
            wither.refreshPositionAndAngles((double) bottomBlock.getX() + 0.5, (double) bottomBlock.getY() + 0.55, (double) bottomBlock.getZ() + 0.5, result.getForwards().getAxis() == Direction.Axis.X ? 0.0f : 90.0f, 0.0f);
            wither.bodyYaw = result.getForwards().getAxis() == Direction.Axis.X ? 0.0f : 90.0f;
            wither.onSummoned();
            for (ServerPlayerEntity serverPlayerEntity : world.getNonSpectatingEntities(ServerPlayerEntity.class, wither.getBoundingBox().expand(50.0))) {
                Criteria.SUMMONED_ENTITY.trigger(serverPlayerEntity, wither);
            }
            world.spawnEntity(wither);
            // destroy blocks used to create the wither
            for (int i = 0; i < witherBossPattern.getWidth(); ++i) {
                for (int j = 0; j < witherBossPattern.getHeight(); ++j) {
                    world.updateNeighbors(result.translate(i, j, 0).getBlockPos(), Blocks.AIR);
                }
            }
        }
    }
}