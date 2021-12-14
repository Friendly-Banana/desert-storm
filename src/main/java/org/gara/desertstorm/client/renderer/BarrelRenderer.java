package org.gara.desertstorm.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import org.gara.desertstorm.Utils;
import org.gara.desertstorm.client.DesertStormClient;
import org.gara.desertstorm.client.model.BarrelModel;
import org.gara.desertstorm.entity.RollingBarrel;

/*
 * A renderer is used to provide an entity model, shadow size, and texture.
 */
@Environment(EnvType.CLIENT)
public class BarrelRenderer extends MobEntityRenderer<RollingBarrel, BarrelModel> {
    private static final Identifier TEXTURE_LOCATION = Utils.NewIdentifier("textures/entity/barrel.png");

    public BarrelRenderer(Context context) {
        super(context, new BarrelModel(context.getPart(DesertStormClient.BARREL_MODEL_LAYER)), 0.5f);
    }


    @Override
    public Identifier getTexture(RollingBarrel entity) {
        return TEXTURE_LOCATION;
    }
}
