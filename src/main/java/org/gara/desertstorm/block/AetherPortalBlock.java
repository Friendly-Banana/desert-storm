package org.gara.desertstorm.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.gara.desertstorm.DesertStorm;

public class AetherPortalBlock extends CustomPortalBlock {
    public AetherPortalBlock() {
        super(FabricBlockSettings.of(Material.PORTAL).noCollision().ticksRandomly().strength(-1.0f).sounds(BlockSoundGroup.GLASS).luminance(state -> 13));
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return DesertStorm.AETHER_PORTAL_ITEM.getDefaultStack();
    }

    @Override
    public Block getPortalBase(World world, BlockPos pos) {
        return Blocks.GLOWSTONE;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction.Axis axis = ctx.getPlayerLookDirection().getAxis();
        return this.getDefaultState().with(AXIS, axis.isHorizontal() ? axis : Direction.Axis.Z);
    }
}