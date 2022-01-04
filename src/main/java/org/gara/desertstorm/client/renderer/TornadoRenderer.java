package org.gara.desertstorm.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import org.gara.desertstorm.Utils;
import org.gara.desertstorm.client.DesertStormClient;
import org.gara.desertstorm.client.model.TornadoModel;
import org.gara.desertstorm.entity.Tornado;

/*
 * A renderer is used to provide an entity model, shadow size, and texture.
 */
@Environment(EnvType.CLIENT)
public class TornadoRenderer extends MobEntityRenderer<Tornado, TornadoModel> {

    public TornadoRenderer(Context context) {
        super(context, new TornadoModel(context.getPart(DesertStormClient.TORNADO_INNER_MODEL_LAYER), false), 1.5f);
        this.addFeature(new TornadoFeatureRenderer(this, context.getModelLoader()));
    }

    @Override
    public Identifier getTexture(Tornado entity) {
        return Utils.NewIdentifier("textures/entity/tornado.png");
    }
}