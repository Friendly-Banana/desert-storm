package org.gara.desertstorm.client.renderer;

import org.gara.desertstorm.entity.Tornado;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

/*
 * A renderer is used to provide an entity model, shadow size, and texture.
 */
@Environment(EnvType.CLIENT)
public class TornadoRenderer extends EntityRenderer<Tornado> {
    private static final Identifier TEXTURE_LOCATION = new Identifier("textures/entity/tornado.png");

    public TornadoRenderer(Context context) {
        super(context);
    }

    @Override
    public void render(Tornado entity, float f, float g, MatrixStack poseStack,
            VertexConsumerProvider multiBufferSource, int i) {
        // do nothing
    }

    @Override
    public Identifier getTexture(Tornado entity) {
        return TEXTURE_LOCATION;
    }

}
