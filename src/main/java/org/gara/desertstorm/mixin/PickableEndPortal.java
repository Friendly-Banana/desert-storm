package org.gara.desertstorm.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.gara.desertstorm.DesertStorm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndPortalBlock.class)
public class PickableEndPortal {
	@Inject(method = "getPickStack", at = @At("HEAD"), cancellable = true)
	private void pickItem(BlockView world, BlockPos pos, BlockState state, CallbackInfoReturnable<ItemStack> cir) {
		cir.setReturnValue(DesertStorm.END_PORTAL_ITEM.getDefaultStack());
	}
}
