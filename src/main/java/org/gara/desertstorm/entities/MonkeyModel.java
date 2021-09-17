package org.gara.desertstorm.entities;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.QuadrupedEntityModel;

public class MonkeyModel extends QuadrupedEntityModel<Monkey> {
    protected MonkeyModel(ModelPart root) {
        super(root, true, 10.0F, 4.0F, 2.0F, 2.0F, 24);
    }

    private static ModelPartData modelPartData;

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        modelPartData = modelData.getRoot();
        // negative Z-Achse ist vorne, rechts = positive X-Achse
        float witdh = 6, height = 12, armeVonDerMitte = -6, beineVonMitte = 7.0F;
        modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 6.0F).uv(22, 0), ModelTransform.pivot(0.0F, 4.0F, -8.0F));
        modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create().uv(18, 4).cuboid(-8.0F, -10.0F, -beineVonMitte, 16.0F, 18.0F, 10.0F).uv(52, 0).cuboid(-2.0F, 2.0F, -8.0F, 4.0F, 6.0F, 1.0F), ModelTransform.of(0.0F, 5.0F, 2.0F, 1.5707964F, 0.0F, 0.0F));
        ModelPartBuilder modelPartBuilder = ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, height, 4.0F);
        modelPartData.addChild(EntityModelPartNames.RIGHT_HIND_LEG, modelPartBuilder, ModelTransform.pivot(witdh, 16, beineVonMitte));
        modelPartData.addChild(EntityModelPartNames.RIGHT_HIND_LEG, modelPartBuilder, ModelTransform.pivot(witdh, height, beineVonMitte));
        modelPartData.addChild(EntityModelPartNames.LEFT_HIND_LEG, modelPartBuilder, ModelTransform.pivot(-witdh, height, beineVonMitte));
        modelPartData.addChild(EntityModelPartNames.RIGHT_FRONT_LEG, modelPartBuilder, ModelTransform.pivot(witdh, height, armeVonDerMitte));
        modelPartData.addChild(EntityModelPartNames.LEFT_FRONT_LEG, modelPartBuilder, ModelTransform.pivot(-witdh, height, armeVonDerMitte));
        modelPartData.addChild(EntityModelPartNames.TAIL, modelPartBuilder, ModelTransform.pivot(0, height, 4));
        return TexturedModelData.of(modelData, 64, 32);
    }
}