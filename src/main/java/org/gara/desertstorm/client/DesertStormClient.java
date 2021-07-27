package org.gara.desertstorm.client;

import org.gara.desertstorm.DesertStorm;
import org.gara.desertstorm.Utils;
import org.gara.desertstorm.entities.SandWitherRenderer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;

@Environment(EnvType.CLIENT)
public class DesertStormClient implements ClientModInitializer {
	public static final ModelLayerLocation MODEL_CUBE_LAYER = new ModelLayerLocation(Utils.NewIdentifier("cube"),
			"main");

	@Override
	public void onInitializeClient() {
		/*
		 * Registers our Cube Entity's renderer, which provides a model and texture for
		 * the entity.
		 *
		 * Entity Renderers can also manipulate the model before it renders based on
		 * entity context (EndermanEntityRenderer#render).
		 */
		EntityRendererRegistry.INSTANCE.register(DesertStorm.SAND_WITHER, (context) -> {
			return new SandWitherRenderer(context);
		});
	}
}
