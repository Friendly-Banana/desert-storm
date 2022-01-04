package org.gara.desertstorm.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import org.gara.desertstorm.client.DesertStormClient;
import org.gara.desertstorm.client.model.TornadoModel;
import org.gara.desertstorm.entity.Tornado;


@Environment(EnvType.CLIENT)
public class TornadoFeatureRenderer extends FeatureRenderer<Tornado, TornadoModel> {
    private final EntityModel<Tornado> model;

    public TornadoFeatureRenderer(FeatureRendererContext<Tornado, TornadoModel> context, EntityModelLoader loader) {
        super(context);
        this.model = new TornadoModel(loader.getModelPart(DesertStormClient.TORNADO_OUTER_MODEL_LAYER));
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light, Tornado tornado, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (tornado.isInvisible() && !minecraftClient.hasOutline(tornado)) {
            return;
        }
        VertexConsumer vertexConsumer = minecraftClient.hasOutline(tornado) && tornado.isInvisible() ? vertexConsumerProvider.getBuffer(RenderLayer.getOutline(this.getTexture(tornado))) : vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(this.getTexture(tornado)));
        this.getContextModel().copyStateTo(this.model);
        this.model.animateModel(tornado, limbAngle, limbDistance, tickDelta);
        this.model.setAngles(tornado, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
        this.model.render(matrices, vertexConsumer, light, LivingEntityRenderer.getOverlay(tornado, 0.0f), 1.0f, 1.0f, 1.0f, 1.0f);

    }
}
