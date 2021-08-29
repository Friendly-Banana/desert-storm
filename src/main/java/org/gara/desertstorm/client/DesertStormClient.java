package org.gara.desertstorm.client;

import org.gara.desertstorm.DesertStorm;
import org.gara.desertstorm.entities.SandWitherRenderer;
import org.gara.desertstorm.entities.TornadoRenderer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry ;

@Environment(EnvType.CLIENT)
public class DesertStormClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		/*
		 * Registers our Entities' renderer, which provide a model and texture for
		 * the entities.
		 *
		 * Entity Renderers can also manipulate the model before it renders based on
		 * entity context (EndermanEntityRenderer#render).
		 */
		EntityRendererRegistry.register(DesertStorm.SAND_WITHER, (context) -> {
			return new SandWitherRenderer(context);
		});
		EntityRendererRegistry.register(DesertStorm.TORNADO, (context) -> {
			return new TornadoRenderer(context);
		});
	}
}
