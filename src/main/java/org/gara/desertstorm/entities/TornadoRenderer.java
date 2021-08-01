package org.gara.desertstorm.entities;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;

/*
 * A renderer is used to provide an entity model, shadow size, and texture.
 */
@Environment(EnvType.CLIENT)
public class TornadoRenderer extends EntityRenderer<Tornado> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/entity/tornado.png");

    public TornadoRenderer(Context context) {
        super(context);
    }

    @Override
    public void render(Tornado entity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource,
            int i) {
        // TODO Auto-generated method stub
        super.render(entity, f, g, poseStack, multiBufferSource, i);
    }

    @Override
    public ResourceLocation getTextureLocation(Tornado entity) {
        return TEXTURE_LOCATION;
    }

}
