package org.gara.desertstorm.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.render.RenderLayer;
import org.gara.desertstorm.DesertStorm;
import org.gara.desertstorm.client.renderer.SandWitherRenderer;
import org.gara.desertstorm.client.renderer.TornadoRenderer;
import org.gara.desertstorm.screen.MixerScreen;

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
        EntityRendererRegistry.register(DesertStorm.SAND_WITHER, SandWitherRenderer::new);
        EntityRendererRegistry.register(DesertStorm.TORNADO, TornadoRenderer::new);
        ScreenRegistry.register(DesertStorm.MIXER_SCREEN_HANDLER, MixerScreen::new);
        BlockRenderLayerMap.INSTANCE.putBlock(DesertStorm.MIXER_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(DesertStorm.COCONUT_BLOCK, RenderLayer.getCutout());
    }
}
