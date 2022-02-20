package org.gara.desertstorm.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.gara.desertstorm.SandWitherThings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
@SuppressWarnings({"ConstantConditions"})
public abstract class BlockMixin {
    @Inject(method = "onPlaced", at = @At(value = "TAIL"))
    private void spawnSandWither(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
        // make sure we do this only for the right blocks
        if ((Object) this instanceof SkullBlock skullBlock && skullBlock.getSkullType() == SkullBlock.Type.SKELETON) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof SkullBlockEntity) {
                SandWitherThings.onPlaced(world, pos, (SkullBlockEntity) blockEntity);
            }
        }
    }

    @Inject(method = "getPlacementState", cancellable = true, at = @At(value = "TAIL"))
    private void placeNetherPortals(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {
        if ((Object) this instanceof NetherPortalBlock) {
            Direction.Axis axis = ctx.getPlayerLookDirection().getAxis();
            cir.setReturnValue(((Block) (Object) this).getDefaultState().with(NetherPortalBlock.AXIS, axis.isHorizontal() ? axis : Direction.Axis.Z));
        }
    }
}