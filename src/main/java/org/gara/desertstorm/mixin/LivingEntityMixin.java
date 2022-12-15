package org.gara.desertstorm.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SkullItem;
import net.minecraft.util.math.BlockPos;
import org.gara.desertstorm.DesertStorm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
@SuppressWarnings({"ConstantConditions"})
public abstract class LivingEntityMixin {
	@Inject(method = "getGroup", at = @At("HEAD"), cancellable = true)
	private void illagerLikeRabbits(CallbackInfoReturnable<EntityGroup> cir) {
		if ((Object) this instanceof RabbitEntity rabbit && rabbit.getRabbitType() == RabbitEntity.KILLER_BUNNY_TYPE) {
			cir.setReturnValue(EntityGroup.ILLAGER);
		}
	}

	@Inject(method = "onKilledBy", at = @At("TAIL"))
	private void dropPlayerHeads(LivingEntity adversary, CallbackInfo ci) {
		if ((Object) this instanceof PlayerEntity entity && entity.world.getGameRules().getBoolean(DesertStorm.DROP_PLAYER_HEADS)) {
			if (adversary instanceof PlayerEntity) {
				boolean couldPlaceAsBlock = false;
				BlockPos blockPos = entity.getBlockPos();
				BlockState blockState = Blocks.PLAYER_HEAD.getDefaultState();
				if (entity.world.getBlockState(blockPos).isAir() && blockState.canPlaceAt(entity.world, blockPos)) {
					entity.world.setBlockState(blockPos, blockState, Block.NOTIFY_ALL);
					// set block entity owner
					if (entity.world.getBlockEntity(blockPos) instanceof SkullBlockEntity skullBlockEntity) {
						skullBlockEntity.setOwner(new GameProfile(entity.getUuid(), entity.getEntityName()));
						couldPlaceAsBlock = true;
					} else {
						entity.world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
					}
				}
				if (!couldPlaceAsBlock) {
					ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
					stack.getOrCreateNbt().putString(SkullItem.SKULL_OWNER_KEY, entity.getEntityName());
					entity.world.spawnEntity(new ItemEntity(entity.world, entity.getX(), entity.getY(), entity.getZ(), stack));
				}
			}
		}
	}
}
