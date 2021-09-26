package org.gara.desertstorm.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import org.gara.desertstorm.Utils;
import org.gara.desertstorm.client.DesertStormClient;
import org.gara.desertstorm.client.model.MonkeyModel;
import org.gara.desertstorm.entity.MonkeyEntity;

@Environment(EnvType.CLIENT)
public class MonkeyRenderer extends MobEntityRenderer<MonkeyEntity, MonkeyModel> {

    public MonkeyRenderer(EntityRendererFactory.Context context) {
        super(context, new MonkeyModel(context.getPart(DesertStormClient.MONKEY_MODEL_LAYER)), 0.5f);
    }

    @Override
    public Identifier getTexture(MonkeyEntity entity) {
        return Utils.NewIdentifier("textures/entity/monkey.png");
    }
}