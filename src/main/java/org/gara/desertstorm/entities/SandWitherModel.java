package org.gara.desertstorm.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.WitherBossModel;
import net.minecraft.client.model.geom.ModelPart;

@Environment(EnvType.CLIENT)
public class SandWitherModel extends WitherBossModel<SandWither> {

    public SandWitherModel(ModelPart modelPart) {
        super(modelPart);
    }
}