package org.gara.desertstorm.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.ItemTags;
import net.minecraft.world.World;

@Mixin(VillagerEntity.class)
public class VillagerFollowEmeralds extends MobEntity {	
	protected VillagerFollowEmeralds(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "initGoals()V")
	void addEmeraldTempt(CallbackInfo ci) {
		goalSelector.add(0, new TemptGoal(((VillagerEntity) (Object) this), 1.5D, Ingredient.ofItems(Items.EMERALD), false));
		goalSelector.add(0, new TemptGoal(((VillagerEntity) (Object) this), 1.5D, Ingredient.fromTag(ItemTags.BEDS), false));
	}
}
