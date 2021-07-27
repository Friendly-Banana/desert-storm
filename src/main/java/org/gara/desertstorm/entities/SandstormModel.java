package org.gara.desertstorm.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.WitherBossModel;
import net.minecraft.client.model.geom.ModelPart;

@Environment(EnvType.CLIENT)
public class SandstormModel extends WitherBossModel<SandWither> {

    public SandstormModel(ModelPart modelPart) {
        super(modelPart);
    }
}