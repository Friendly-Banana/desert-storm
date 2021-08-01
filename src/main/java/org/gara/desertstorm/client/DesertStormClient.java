package org.gara.desertstorm.client;

import org.gara.desertstorm.DesertStorm;
import org.gara.desertstorm.entities.SandWitherRenderer;
import org.gara.desertstorm.entities.TornadoRenderer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class DesertStormClient implements ClientModInitializer {

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
		EntityRendererRegistry.INSTANCE.register(DesertStorm.TORNADO, (context) -> {
			return new TornadoRenderer(context);
		});
	}
}
