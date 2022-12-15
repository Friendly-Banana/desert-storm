package org.gara.desertstorm.client.model;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import org.gara.desertstorm.entity.RollingBarrel;

@Environment(EnvType.CLIENT)
public class BarrelModel extends EntityModel<RollingBarrel> {
	private final ModelPart base;

	public BarrelModel(ModelPart modelPart) {
		this.base = modelPart.getChild(EntityModelPartNames.CUBE);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild(EntityModelPartNames.CUBE, ModelPartBuilder.create().uv(0, 0).cuboid(-9, 8, -8, 18, 16, 16), ModelTransform.NONE);
		return TexturedModelData.of(modelData, 128, 64);
	}

	@Override
	public void setAngles(RollingBarrel entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		ImmutableList.of(this.base).forEach((modelRenderer) -> modelRenderer.render(matrices, vertices, light, overlay, red, green, blue, alpha));
	}
}