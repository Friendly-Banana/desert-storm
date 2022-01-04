package org.gara.desertstorm.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import org.gara.desertstorm.entity.Tornado;

import java.util.Arrays;

@Environment(EnvType.CLIENT)
public class TornadoModel extends SinglePartEntityModel<Tornado> {
    public static final String EYE_OF_THE_STORM = "eye_of_the_storm";
    private static final int SLICES_COUNT = 8;
    private final ModelPart root;
    private final ModelPart[] slices = new ModelPart[SLICES_COUNT];
    private final boolean outerModel;

    public TornadoModel(ModelPart root, boolean outerModel) {
        this.root = root;
        this.outerModel = outerModel;
        if (outerModel)
            Arrays.setAll(this.slices, index -> root.getChild(getSliceName(index)));
    }

    private static String getSliceName(int index) {
        return "cube" + index;
    }

    private static ModelPartBuilder quadraticCentered(float width, float height, float offsetY, ModelPartBuilder modelPartBuilder) {
        return modelPartBuilder.cuboid(-width / 2, offsetY - height / 2, -width / 2, width, height, width);
    }

    public static TexturedModelData getInnerTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(EYE_OF_THE_STORM, quadraticCentered(4, Tornado.dimensions.height * 16, -16, ModelPartBuilder.create().uv(0, 16)), ModelTransform.NONE);
        return TexturedModelData.of(modelData, 64, 32);
    }

    public static TexturedModelData getOuterTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        float sliceHeight = 16 * Tornado.dimensions.height / SLICES_COUNT;
        for (int i = 0; i < SLICES_COUNT; ++i) {
            // pixel per block * percent of full size
            float sliceSize = 16f * (i + 1) / SLICES_COUNT;
            modelPartData.addChild(getSliceName(i), quadraticCentered(Tornado.dimensions.width * sliceSize, sliceHeight,
                    29 - Tornado.dimensions.height * sliceSize, ModelPartBuilder.create().uv(0, 16)), ModelTransform.rotation(0, i * 17.5f, 0));
        }
        return TexturedModelData.of(modelData, 64, 32);
    }

    @Override
    public void animateModel(Tornado entity, float limbAngle, float limbDistance, float tickDelta) {
        this.root.setAngles(0, (float) Math.toRadians(entity.age), 0);
        /*if (outerModel)
            for (int i = 0; i < SLICES_COUNT; ++i) {
                this.slices[i].setAngles(0, (float) Math.toRadians(i * 15 + entity.age), 0);
            }
        else
            this.root.getChild(EYE_OF_THE_STORM).setAngles(0, (float) Math.toRadians(entity.age), 0);*/
    }

    @Override
    public void setAngles(Tornado entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }
}