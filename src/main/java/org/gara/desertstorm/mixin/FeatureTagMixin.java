package org.gara.desertstorm.mixin;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.ConfiguredStructureFeatureTagProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatureKeys;
import org.gara.desertstorm.entity.MonkeyEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConfiguredStructureFeatureTagProvider.class)
public abstract class FeatureTagMixin extends net.minecraft.data.server.AbstractTagProvider<net.minecraft.world.gen.feature.ConfiguredStructureFeature<?, ?>> {
	protected FeatureTagMixin(DataGenerator root, Registry<ConfiguredStructureFeature<?, ?>> registry) {
		super(root, registry);
	}

	@Inject(method = "configure", at = @At("HEAD"))
	private void addMonkeyTarget(CallbackInfo ci) {
		getOrCreateTagBuilder(MonkeyEntity.MONKEY_LOCATED).add(ConfiguredStructureFeatureKeys.VILLAGE_PLAINS).add(ConfiguredStructureFeatureKeys.RUINED_PORTAL_JUNGLE).add(ConfiguredStructureFeatureKeys.JUNGLE_PYRAMID).add(ConfiguredStructureFeatureKeys.VILLAGE_SNOWY);
	}
}
