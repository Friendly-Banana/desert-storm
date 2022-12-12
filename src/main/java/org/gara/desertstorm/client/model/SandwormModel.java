package org.gara.desertstorm.client.model;

import net.minecraft.util.Identifier;
import org.gara.desertstorm.Utils;
import org.gara.desertstorm.entity.Sandworm;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SandwormModel extends AnimatedGeoModel<Sandworm> {
	@Override
	public Identifier getModelLocation(Sandworm sandworm) {
		return Utils.NewIdentifier("geo/sandworm.geo");
	}

	@Override
	public Identifier getTextureLocation(Sandworm sandworm) {
		return Utils.NewIdentifier("textures/item/sandworm.png");
	}

	@Override
	public Identifier getAnimationFileLocation(Sandworm sandworm) {
		return Utils.NewIdentifier("animations/sandworm.animation.json");
	}

}
