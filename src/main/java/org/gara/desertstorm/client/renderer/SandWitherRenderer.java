package org.gara.desertstorm.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.WitherEntityRenderer;

/*
 * A renderer is used to provide an entity model, shadow size, and texture.
 */
@Environment(EnvType.CLIENT)
public class SandWitherRenderer extends WitherEntityRenderer {
    public SandWitherRenderer(Context context) {
        super(context);
    }
}
