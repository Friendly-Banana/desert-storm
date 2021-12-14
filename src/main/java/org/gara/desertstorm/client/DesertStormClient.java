package org.gara.desertstorm.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import org.gara.desertstorm.DesertStorm;
import org.gara.desertstorm.Utils;
import org.gara.desertstorm.client.model.BarrelModel;
import org.gara.desertstorm.client.model.MonkeyModel;
import org.gara.desertstorm.client.renderer.BarrelRenderer;
import org.gara.desertstorm.client.renderer.MonkeyRenderer;
import org.gara.desertstorm.client.renderer.SandWitherRenderer;
import org.gara.desertstorm.client.renderer.TornadoRenderer;

@Environment(EnvType.CLIENT)
public class DesertStormClient implements ClientModInitializer {
    public static final EntityModelLayer MONKEY_MODEL_LAYER = new EntityModelLayer(Utils.NewIdentifier("monkey"), "main");
    public static final EntityModelLayer BARREL_MODEL_LAYER = new EntityModelLayer(Utils.NewIdentifier("barrel"), "main");

    @Override
    public void onInitializeClient() {
        /*
         * Registers our Entities' renderer, which provide a model and texture for
         * the entities.
         *
         * Entity Renderers can also manipulate the model before it renders based on
         * entity context (EntityRenderer#render).
         */
        EntityModelLayerRegistry.registerModelLayer(MONKEY_MODEL_LAYER, MonkeyModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(BARREL_MODEL_LAYER, BarrelModel::getTexturedModelData);

        EntityRendererRegistry.register(DesertStorm.MONKEY, MonkeyRenderer::new);
        EntityRendererRegistry.register(DesertStorm.MONKEY_KING, MonkeyRenderer::new);
        EntityRendererRegistry.register(DesertStorm.ROLLING_BARREL, BarrelRenderer::new);
        EntityRendererRegistry.register(DesertStorm.SAND_WITHER, SandWitherRenderer::new);
        EntityRendererRegistry.register(DesertStorm.TORNADO, TornadoRenderer::new);

        BlockRenderLayerMap.INSTANCE.putBlock(DesertStorm.COCONUT_BLOCK, RenderLayer.getCutout());
    }
}
