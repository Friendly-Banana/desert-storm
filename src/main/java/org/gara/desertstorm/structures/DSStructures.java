package org.gara.desertstorm.structures;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.structure.PlainsVillageData;
import net.minecraft.structure.StructurePiecesList;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;
import org.gara.desertstorm.Utils;

import java.util.Random;

public class DSStructures {
    private static final Registry<ConfiguredStructureFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;
    /**
     * Registers the structure itself and sets what its path is. In this case, the
     * structure will have the Identifier of desertstorm:run_down_house.
     * <p>
     * It is always a good idea to register your Structures so that other mods and datapacks can
     * use them too directly from the registries. It's great for mod/datapacks compatibility.
     */
    public static StructureFeature<StructurePoolFeatureConfig> RUN_DOWN_HOUSE = new RunDownHouseStructure(StructurePoolFeatureConfig.CODEC);
    public static StructureFeature<StructurePoolFeatureConfig> KONG_LEVEL = new KongLevelStructure(StructurePoolFeatureConfig.CODEC);
    public static StructureFeature<StructurePoolFeatureConfig> TOMB = new TombStructure(StructurePoolFeatureConfig.CODEC);
    /**
     * Static instance of our configured structure, so we can reference it and add it to biomes easily.
     */
    public static ConfiguredStructureFeature<?, ?> CONFIGURED_RUN_DOWN_HOUSE = registerStructure("run_down_house", RUN_DOWN_HOUSE, 10, 5, 399117345);
    public static ConfiguredStructureFeature<?, ?> CONFIGURED_KONG_LEVEL = registerStructure("kong_level", KONG_LEVEL, 50, 10, 123456789);
    public static ConfiguredStructureFeature<?, ?> CONFIGURED_TOMB = registerStructure("tomb", TOMB, 30, 7, 456456);

    /**
     * @param spacing    average distance apart in chunks between spawn attempts
     * @param separation minimum distance apart in chunks between spawn attempts. MUST BE LESS THAN @spacing
     * @param salt       this modifies the seed of the structure so no two structures always spawn over each-other. Make this large and unique.
     */
    private static ConfiguredStructureFeature<?, ?> registerStructure(String id, StructureFeature<StructurePoolFeatureConfig> structure, int spacing, int separation, int salt) {
        /* This is Fabric API's builder for structures.
         It has many options to make sure your structure will spawn and work properly.
         Give it your structure and the identifier you want for it. */
        FabricStructureBuilder.create(Utils.NewIdentifier(id), structure).step(GenerationStep.Feature.SURFACE_STRUCTURES)
                /* Generation stage for when to generate the structure. there are 10 stages you can pick from!
                   This surface structure stage places the structure before plants and ores are generated. */
                .defaultConfig(new StructureConfig(spacing, separation, salt))
                /* Always set this or else re-entering SuperFlat worldtype will crash.
                   Getting structures to spawn in Superflat is a bit buggy right now so don't focus too much on this. */
                .enableSuperflat()
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
        ConfiguredStructureFeature<?, ?> conf_structure = structure
                // Dummy StructurePoolFeatureConfig values for now. We will modify the pool at runtime since we cannot get json pool files here at mod init.
                // You can create and register your pools in code, pass in the code create pool here, and delete both newConfig and newContext in RunDownHouseStructure's createPiecesGenerator.
                // Note: StructurePoolFeatureConfig only takes 0 - 7 size so that's another reason why we are going to bypass that "codec" by changing size at runtime to get higher sizes.
                .configure(new StructurePoolFeatureConfig(() -> PlainsVillageData.STRUCTURE_POOLS, 0));
        /*
          Registers the configured structure which is what gets added to the biomes.
          You can use the same identifier for the configured structure as the regular structure
          because the two fo them are registered to different registries.

          We can register configured structures at any time before a world is clicked on and made.
          But the best time to register configured features by code is honestly to do it in onInitialize.
         */
        Registry.register(registry, Utils.NewIdentifier("configured_" + id), conf_structure);
        return conf_structure;
    }

    public static void addStructuresToBiomes() {
        /*
         * This is the API you will use to add anything to any biome.
         * This includes spawns, changing the biome's looks, messing with its
         * surfacebuilders,
         * adding carvers, spawning new features... etc
         *
         * Make sure you give this an identifier to make it clear later what mod did a
         * change and why.
         * It'll help people look to see if your mod was removing something from biomes.
         * The biome modifier identifier might also be used by modpacks to disable mod's
         * modifiers too for customization.
         */
        BiomeModifications.create(Utils.NewIdentifier("run_down_house_addition"))
                .add(// Describes what we are doing. Since we are adding a structure, we choose
                        // ADDITIONS.
                        ModificationPhase.ADDITIONS,
                        // Add our structure to all biomes including other modded biomes.
                        // You can filter to certain biomes based on stuff like temperature, scale,
                        // precipitation, mod id.
                        BiomeSelectors.includeByKey(BiomeKeys.BIRCH_FOREST, BiomeKeys.OLD_GROWTH_BIRCH_FOREST),
                        // context is basically the biome itself. This is where you do the changes to the biome.
                        // Here, we will add our ConfiguredStructureFeature to the biome.
                        context -> context.getGenerationSettings()
                                .addBuiltInStructure(DSStructures.CONFIGURED_RUN_DOWN_HOUSE));
        BiomeModifications.create(Utils.NewIdentifier("kong_level_addition")).add(ModificationPhase.ADDITIONS,
                BiomeSelectors.categories(Biome.Category.JUNGLE),
                context -> context.getGenerationSettings().addBuiltInStructure(DSStructures.CONFIGURED_KONG_LEVEL));
        BiomeModifications.create(Utils.NewIdentifier("tomb_addition")).add(ModificationPhase.ADDITIONS,
                BiomeSelectors.categories(Biome.Category.DESERT),
                context -> context.getGenerationSettings().addBuiltInStructure(DSStructures.CONFIGURED_TOMB));
    }

    public static void mossyCobblePostPlacement(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, StructurePiecesList children) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        BlockBox blockBox = children.getBoundingBox();
        for (int x = blockBox.getMinX(); x <= blockBox.getMaxX(); ++x) {
            for (int z = blockBox.getMinZ(); z <= blockBox.getMaxZ(); ++z) {
                for (int y = blockBox.getMinY(); y <= blockBox.getMaxY(); ++y) {
                    mutable.set(x, y, z);
                    if (world.getBlockState(mutable).isOf(Blocks.COBBLESTONE) && random.nextFloat() < 0.4f)
                        world.setBlockState(mutable, Blocks.MOSSY_COBBLESTONE.getDefaultState(), Block.NOTIFY_LISTENERS);
                }
            }
        }
    }
}
