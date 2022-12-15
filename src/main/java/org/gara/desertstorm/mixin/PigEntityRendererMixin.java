package org.gara.desertstorm.mixin;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PigEntityRenderer;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.entity.passive.PigEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PigEntityRenderer.class)
public abstract class PigEntityRendererMixin extends LivingEntityRenderer<PigEntity, PigEntityModel<PigEntity>> {
	public PigEntityRendererMixin(EntityRendererFactory.Context ctx, PigEntityModel<PigEntity> model, float shadowRadius) {
		super(ctx, model, shadowRadius);
	}

	@Inject(method = "<init>", at = @At(value = "TAIL"))
	private void addElytraFeatureRenderer(EntityRendererFactory.Context context, CallbackInfo ci) {
		this.addFeature(new ElytraFeatureRenderer<>(this, context.getModelLoader()));
	}
}
