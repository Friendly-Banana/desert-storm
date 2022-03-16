package org.gara.desertstorm.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Shadow
    public abstract BlockState getDefaultState();

    @Inject(method = "onPlaced", at = @At(value = "TAIL"))
    void handeOnPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
    }

    @Inject(method = "getPlacementState", cancellable = true, at = @At(value = "HEAD"))
    void handleGetPlacementState(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {
    }
}