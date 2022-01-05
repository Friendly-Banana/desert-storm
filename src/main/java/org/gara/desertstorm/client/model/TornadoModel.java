package org.gara.desertstorm.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import org.gara.desertstorm.entity.Tornado;

@Environment(EnvType.CLIENT)
public class TornadoModel extends SinglePartEntityModel<Tornado> {
    public static final String EYE_OF_THE_STORM = "eye_of_the_storm";
    private static final int SLICES_COUNT = 10;
    /**
     * pixel for full block / amount of slices
     */
    public static final float PIXELS_PER_SLICE = 16f / SLICES_COUNT;
    private final ModelPart root;
    private final boolean innerModel;

    public TornadoModel(ModelPart root, boolean innerModel) {
        this.root = root;
        this.innerModel = innerModel;
    }

    private static String getSliceName(int index) {
        return "cube" + index;
    }

    private static ModelPartBuilder quadraticCentered(float width, float height, float offsetY, ModelPartBuilder modelPartBuilder) {
        return modelPartBuilder.cuboid(-width / 2, offsetY, -width / 2, width, height, width);
    }

    public static TexturedModelData getInnerTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(EYE_OF_THE_STORM, quadraticCentered(1, 16, 8,
                ModelPartBuilder.create().uv(32, 0)), ModelTransform.NONE);
        return TexturedModelData.of(modelData, 64, 32);
    }

    public static TexturedModelData getOuterTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        for (int i = 0; i < SLICES_COUNT; ++i) {
            modelPartData.addChild(getSliceName(i), quadraticCentered(PIXELS_PER_SLICE * (SLICES_COUNT - i), PIXELS_PER_SLICE, PIXELS_PER_SLICE * i + 8, ModelPartBuilder.create().uv(0, 0)), ModelTransform.rotation(0, i * 360f / SLICES_COUNT, 0));
        }
        return TexturedModelData.of(modelData, 64, 32);
    }

    @Override
    public void animateModel(Tornado entity, float limbAngle, float limbDistance, float tickDelta) {
        this.root.setAngles(0, (float) Math.toRadians(entity.age), 0);
    }

    @Override
    public void setAngles(Tornado entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        if (innerModel)
            this.root.getChild(EYE_OF_THE_STORM).setAngles(headPitch, headYaw, 0);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }
}