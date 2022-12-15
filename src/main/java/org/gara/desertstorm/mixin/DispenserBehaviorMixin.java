package org.gara.desertstorm.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.event.GameEvent;
import org.gara.desertstorm.SandWitherThings;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(DispenserBehavior.class)
public interface DispenserBehaviorMixin {
	@ModifyArg(method = "registerDefaults", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/item/Items;SKELETON_SKULL:Lnet/minecraft/item/Item;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Lnet/minecraft/block/DispenserBlock;registerBehavior(Lnet/minecraft/item/ItemConvertible;Lnet/minecraft/block/dispenser/DispenserBehavior;)V", ordinal = 0))
	private static DispenserBehavior changeSkeletonSkullBehavior(DispenserBehavior behavior) {
		return new FallibleItemDispenserBehavior() {
			@Override
			protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
				ServerWorld world = pointer.getWorld();
				Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
				BlockPos blockPos = pointer.getPos().offset(direction);
				if (world.isAir(blockPos) && SandWitherThings.canDispense(world, blockPos, stack)) {
					world.setBlockState(blockPos, Blocks.SKELETON_SKULL.getDefaultState().with(SkullBlock.ROTATION, direction.getAxis() == Direction.Axis.Y ? 0 : direction.getOpposite().getHorizontal() * 4), Block.NOTIFY_ALL);
					world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
					BlockEntity blockEntity = world.getBlockEntity(blockPos);
					if (blockEntity instanceof SkullBlockEntity) {
						SandWitherThings.onPlaced(world, blockPos, (SkullBlockEntity) blockEntity);
					}
					stack.decrement(1);
					this.setSuccess(true);
				} else {
					this.setSuccess(ArmorItem.dispenseArmor(pointer, stack));
				}
				return stack;
			}
		};
	}
}
