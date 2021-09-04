package org.gara.desertstorm.mixin;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.ai.brain.task.FollowMobTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.VillagerTaskListProvider;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.ItemTags;

@Mixin(VillagerTaskListProvider.class)
public class VillagerFollow {
    @ModifyArg(method = "createCoreTasks(Lnet/minecraft/village/VillagerProfessionF);Lcom/google/common/collect/ImmutableList", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;of(Lcom/mojang/datafixers/util/PairLcom/mojang/datafixers/util/PairLcom/mojang/datafixers/util/PairLcom/mojang/datafixers/util/PairLcom/mojang/datafixers/util/PairLcom/mojang/datafixers/util/PairLcom/mojang/datafixers/util/PairLcom/mojang/datafixers/util/PairLcom/mojang/datafixers/util/PairLcom/mojang/datafixers/util/PairLcom/mojang/datafixers/util/PairLcom/mojang/datafixers/util/Pair[Lcom/mojang/datafixers/util/Pair)Lcom/google/common/collect/ImmutableList;", ordinal = 0))
    private static List<Pair<Integer, ? extends Task<? super VillagerEntity>>> addFollowTask(
            List<Pair<Integer, ? extends Task<? super VillagerEntity>>> original) {
        return ImmutableList.<Pair<Integer, ? extends Task<? super VillagerEntity>>>builder().addAll(original)
                .add(Pair.of(0, new FollowMobTask((livingEntity) -> {
                    return livingEntity.isHolding((itemStack) -> {
                        return itemStack.isOf(Items.EMERALD) || Ingredient.fromTag(ItemTags.BEDS).test(itemStack);
                    });
                }, 16))).build();
    }
}