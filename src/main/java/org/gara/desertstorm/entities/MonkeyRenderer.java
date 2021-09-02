package org.gara.desertstorm.entities;

import org.gara.desertstorm.Utils;
import org.gara.desertstorm.client.DesertStormClient;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class MonkeyRenderer extends MobEntityRenderer<Monkey, MonkeyModel> {
 
    public MonkeyRenderer(EntityRendererFactory.Context context) {
        super(context, new MonkeyModel(context.getPart(DesertStormClient.MONKEY_MODEL_LAYER)), 0.5f);
    }
 
    @Override
    public Identifier getTexture(Monkey entity) {
        return Utils.NewIdentifier("textures/entity/monkey.png");
    }
}