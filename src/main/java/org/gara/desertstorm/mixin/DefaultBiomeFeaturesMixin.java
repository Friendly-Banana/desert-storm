package org.gara.desertstorm.mixin;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import org.gara.desertstorm.entity.DSEntities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DefaultBiomeFeatures.class)
public abstract class DefaultBiomeFeaturesMixin {
    @Inject(method = "addDesertMobs", at = @At("HEAD"))
    private static void spawnSandstomsInDesert(SpawnSettings.Builder builder, CallbackInfo ci) {
        builder.spawn(SpawnGroup.AMBIENT, new SpawnSettings.SpawnEntry(DSEntities.SANDSTORM, 1, 1, 1));
    }
}
