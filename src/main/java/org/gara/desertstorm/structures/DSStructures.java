package org.gara.desertstorm.structures;

import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import org.gara.desertstorm.Utils;

public class DSStructures {

    /**
     * Registers the structure itself and sets what its path is. In this case, the
     * structure will have the Identifier of desertstorm:run_down_house.
     * <p>
     * It is always a good idea to register your Structures so that other mods and datapacks can
     * use them too directly from the registries. It's great for mod/datapacks compatibility.
     */
    public static StructureFeature<DefaultFeatureConfig> RUN_DOWN_HOUSE = new RunDownHouseStructure(DefaultFeatureConfig.CODEC);
    public static StructureFeature<DefaultFeatureConfig> KONG_LEVEL = new KongLevelStructure(DefaultFeatureConfig.CODEC);
    /**
     * Static instance of our configured structure, so we can reference it and add it to biomes easily.
     */
    public static ConfiguredStructureFeature<?, ?> CONFIGURED_RUN_DOWN_HOUSE;
    public static ConfiguredStructureFeature<?, ?> CONFIGURED_KONG_LEVEL;

    private static final Registry<ConfiguredStructureFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;


    /**
     * @param spacing    average distance apart in chunks between spawn attempts
     * @param separation minimum distance apart in chunks between spawn attempts. MUST BE LESS THAN @spacing
     * @param salt       this modifies the seed of the structure so no two structures always spawn over each-other. Make this large and unique.
     */
    private static ConfiguredStructureFeature<?, ?> registerStructure(String id, StructureFeature<DefaultFeatureConfig> structure, int spacing, int separation, int salt) {
        /* This is Fabric API's builder for structures.
         It has many options to make sure your structure will spawn and work properly.
         Give it your structure and the identifier you want for it. */
        FabricStructureBuilder.create(Utils.NewIdentifier(id), structure).step(GenerationStep.Feature.SURFACE_STRUCTURES)
                /* Generation stage for when to generate the structure. there are 10 stages you can pick from!
                   This surface structure stage places the structure before plants and ores are generated. */
                .defaultConfig(new StructureConfig(spacing, separation, salt))
                /* Always set this or else re-entering SuperFlat worldtype will crash.
                   Getting structures to spawn in Superflat is a bit buggy right now so don't focus too much on this. */
                .superflatFeature(structure.configure(FeatureConfig.DEFAULT))
                /*
                 * Whether surrounding land will be modified automatically to conform to the bottom of the structure.
                 * Basically, it adds land at the base of the structure like it does for Villages and Outposts.
                 * Doesn't work well on structure that have pieces stacked vertically or change in heights.
                 *
                 * Note: The air space this method will create will be filled with water if the structure is below sealevel.
                 * This means this is best for structures above sealevel so keep that in mind.
                 */
                .adjustsSurface()
                /* Finally! Now we register our structure and everything above will take effect. */
                .register();
        /*
          Registers the configured structure which is what gets added to the biomes.
          You can use the same identifier for the configured structure as the regular structure
          because the two fo them are registered to different registries.

          We can register configured structures at any time before a world is clicked on and made.
          But the best time to register configured features by code is honestly to do it in onInitialize.
         */
        ConfiguredStructureFeature<?, ?> conf_structure = structure.configure(DefaultFeatureConfig.DEFAULT);
        Registry.register(registry, Utils.NewIdentifier("configured_" + id), conf_structure);
        return conf_structure;
    }

    public static void registerStructures() {
        CONFIGURED_RUN_DOWN_HOUSE = registerStructure("run_down_house", RUN_DOWN_HOUSE, 10, 5, 399117345);
        CONFIGURED_KONG_LEVEL = registerStructure("kong_level", KONG_LEVEL, 50, 10, 123456789);
    }
}
