package org.gara.desertstorm.mixin;

import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.ItemTags;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
@SuppressWarnings({"ConstantConditions"})
public class VillagerFollow {
	@Final
	@Shadow
	protected GoalSelector goalSelector;

	@Inject(method = "initGoals", at = @At(value = "HEAD"))
	private void addEmeraldTempt(CallbackInfo ci) {
		if ((Object) this instanceof VillagerEntity villagerEntity) {
			float villagerRunSpeed = 0.5F;
			goalSelector.add(0, new TemptGoal(villagerEntity, villagerRunSpeed, Ingredient.ofItems(Items.EMERALD), false));
			goalSelector.add(0, new TemptGoal(villagerEntity, villagerRunSpeed, Ingredient.fromTag(ItemTags.BEDS), false));
		}
	}
}