package org.gara.desertstorm.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.WitherEntityModel;
import org.gara.desertstorm.entity.SandWither;

@Environment(EnvType.CLIENT)
public class SandWitherModel extends WitherEntityModel<SandWither> {
	public SandWitherModel(ModelPart modelPart) {
		super(modelPart);
	}
}