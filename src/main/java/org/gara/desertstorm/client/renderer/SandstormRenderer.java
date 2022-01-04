package org.gara.desertstorm.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.gara.desertstorm.Utils;
import org.gara.desertstorm.entity.Sandstorm;

/*
 * A renderer is used to provide an entity model, shadow size, and texture.
 */
@Environment(EnvType.CLIENT)
public class SandstormRenderer extends EntityRenderer<Sandstorm> {
    public SandstormRenderer(Context context) {
        super(context);
    }

    @Override
    public void render(Sandstorm entity, float f, float g, MatrixStack poseStack,
                       VertexConsumerProvider multiBufferSource, int i) {
        // do nothing
    }

    @Override
    public Identifier getTexture(Sandstorm entity) {
        return Utils.NewIdentifier("textures/entity/sandstorm.png");
    }

}
