package org.gara.desertstorm.mixin;

import com.google.common.collect.ImmutableList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Features;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.CocoaDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.LeaveVineDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TrunkVineDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

@Mixin(Features.class)
public class CoconutTrees {
	@ModifyConstant(method = "<clinit>", constant = @Constant(ordinal = 6), require = 1)
	private ConfiguredFeature<TreeConfiguration, ?> injected(ConfiguredFeature<TreeConfiguration, ?> value) {
		return register("jungle_tree", Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(
				new SimpleStateProvider(Blocks.JUNGLE_LOG.defaultBlockState()), new StraightTrunkPlacer(4, 8, 0),
				new SimpleStateProvider(Blocks.JUNGLE_LEAVES.defaultBlockState()),
				new SimpleStateProvider(Blocks.JUNGLE_SAPLING.defaultBlockState()),
				new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1)))
						.decorators(ImmutableList.of(CoconutDecorator.INSTANCE, new CocoaDecorator(0.2F),
								TrunkVineDecorator.INSTANCE, LeaveVineDecorator.INSTANCE))
						.ignoreVines().build()));
	}

	private static <FC extends FeatureConfiguration> ConfiguredFeature<FC, ?> register(String id,
			ConfiguredFeature<FC, ?> configuredFeature) {
		return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id, configuredFeature);
	}
}