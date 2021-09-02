package org.gara.desertstorm.client;

import org.gara.desertstorm.DesertStorm;
import org.gara.desertstorm.Utils;
import org.gara.desertstorm.client.renderer.SandWitherRenderer;
import org.gara.desertstorm.client.renderer.TornadoRenderer;
import org.gara.desertstorm.entities.MonkeyModel;
import org.gara.desertstorm.entities.MonkeyRenderer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;	

@Environment(EnvType.CLIENT)
public class DesertStormClient implements ClientModInitializer {
	public static final EntityModelLayer MONKEY_MODEL_LAYER = new EntityModelLayer(Utils.NewIdentifier("cube"), "main");

	@Override
	public void onInitializeClient() {
		/*
		 * Registers our Entities' renderer, which provide a model and texture for the
		 * entities.
		 *
		 * Entity Renderers can also manipulate the model before it renders based on
		 * entity context (EndermanEntityRenderer#render).
		 */
		EntityRendererRegistry.register(DesertStorm.MONKEY, (context) -> {
			return new MonkeyRenderer(context);
		});
		EntityModelLayerRegistry.registerModelLayer(MONKEY_MODEL_LAYER, MonkeyModel::getTexturedModelData);
		EntityRendererRegistry.register(DesertStorm.SAND_WITHER, (context) -> {
			return new SandWitherRenderer(context);
		});
		EntityRendererRegistry.register(DesertStorm.TORNADO, (context) -> {
			return new TornadoRenderer(context);
		});
		ScreenRegistry.register(DesertStorm.MIXER_SCREEN_HANDLER, MixerScreen::new);
	}
}
