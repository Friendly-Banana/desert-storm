package org.gara.desertstorm.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import org.gara.desertstorm.Utils;
import org.gara.desertstorm.block.DSBlocks;
import org.gara.desertstorm.client.model.BarrelModel;
import org.gara.desertstorm.client.model.MonkeyModel;
import org.gara.desertstorm.client.model.TornadoModel;
import org.gara.desertstorm.client.renderer.*;
import org.gara.desertstorm.entity.DSEntities;

@Environment(EnvType.CLIENT)
public class DesertStormClient implements ClientModInitializer {
    public static final EntityModelLayer MONKEY_MODEL_LAYER = registerMain("monkey");
    public static final EntityModelLayer BARREL_MODEL_LAYER = registerMain("barrel");
    public static final EntityModelLayer TORNADO_INNER_MODEL_LAYER = registerMain("tornado");
    public static final EntityModelLayer TORNADO_OUTER_MODEL_LAYER = new EntityModelLayer(Utils.NewIdentifier("tornado"), "outer");

    public static EntityModelLayer registerMain(String name) {
        return new EntityModelLayer(Utils.NewIdentifier(name), "main");
    }

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
        EntityModelLayerRegistry.registerModelLayer(TORNADO_INNER_MODEL_LAYER, TornadoModel::getInnerTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(TORNADO_OUTER_MODEL_LAYER, TornadoModel::getOuterTexturedModelData);

        EntityRendererRegistry.register(DSEntities.MONKEY, MonkeyRenderer::new);
        EntityRendererRegistry.register(DSEntities.MONKEY_KING, MonkeyRenderer::new);
        EntityRendererRegistry.register(DSEntities.ROLLING_BARREL, BarrelRenderer::new);
        EntityRendererRegistry.register(DSEntities.SAND_WITHER, SandWitherRenderer::new);
        EntityRendererRegistry.register(DSEntities.TORNADO, TornadoRenderer::new);
        EntityRendererRegistry.register(DSEntities.SANDSTORM, SandstormRenderer::new);
		EntityRendererRegistry.register(DSEntities.SANDWORM, SandwormRenderer::new);

        BlockRenderLayerMap.INSTANCE.putBlock(DSBlocks.COCONUT_BLOCK, RenderLayer.getCutout());
    }
}
