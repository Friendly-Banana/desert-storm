package org.gara.desertstorm.mixin;

import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.gara.desertstorm.SandWitherThings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkullBlock.class)
public abstract class SkullBlockMixin extends BlockMixin {
	@Shadow
	public abstract SkullBlock.SkullType getSkullType();

	@Override
	void handeOnPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
		if (getSkullType() == SkullBlock.Type.SKELETON) {
			if (world.getBlockEntity(pos) instanceof SkullBlockEntity skullBlockEntity) {
				SandWitherThings.onPlaced(world, pos, skullBlockEntity);
			}
		}
	}
}
