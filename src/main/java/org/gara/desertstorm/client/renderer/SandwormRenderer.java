package org.gara.desertstorm.client.renderer;

import net.minecraft.client.render.entity.EntityRendererFactory;
import org.gara.desertstorm.client.model.SandwormModel;
import org.gara.desertstorm.entity.Sandworm;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class SandwormRenderer extends GeoEntityRenderer<Sandworm> {
	public SandwormRenderer(EntityRendererFactory.Context renderManager) {
		super(renderManager, new SandwormModel());
	}
}
