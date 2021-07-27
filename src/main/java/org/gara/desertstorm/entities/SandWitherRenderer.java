package org.gara.desertstorm.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.WitherBossRenderer;

/*
 * A renderer is used to provide an entity model, shadow size, and texture.
 */
@Environment(EnvType.CLIENT)
public class SandWitherRenderer extends WitherBossRenderer {
    public SandWitherRenderer(Context context) {
        super(context);
    }
}
