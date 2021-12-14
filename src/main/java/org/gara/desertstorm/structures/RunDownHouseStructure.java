package org.gara.desertstorm.structures;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.structure.MarginedStructureStart;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;
import org.gara.desertstorm.Utils;

public class RunDownHouseStructure extends StructureFeature<DefaultFeatureConfig> {
    /**
     * This method allows us to have mobs that spawn naturally over time in our structure.
     * No other mobs will spawn in the structure of the same entity classification.
     * The reason you want to match the classifications is so that your structure's mob
     * will contribute to that classification's cap. Otherwise, it may cause a runaway
     * spawning of the mob that will never stop.
     * <p>
     * NOTE: getDefaultSpawnList is for monsters only and getDefaultCreatureSpawnList is
     * for creatures only. If you want to add entities of another classification,
     * use the StructureSpawnListGatherEvent to add water_creatures, water_ambient,
     * ambient, or misc mobs. Use that event to add/remove mobs from structures
     * that are not your own.
     */
    private static final Pool<SpawnSettings.SpawnEntry> STRUCTURE_MONSTERS = Pool.of(
            new SpawnSettings.SpawnEntry(EntityType.ILLUSIONER, 100, 4, 9),
            new SpawnSettings.SpawnEntry(EntityType.VINDICATOR, 100, 4, 9)
    );
    private static final Pool<SpawnSettings.SpawnEntry> STRUCTURE_CREATURES = Pool.of(
            new SpawnSettings.SpawnEntry(EntityType.SHEEP, 30, 10, 15),
            new SpawnSettings.SpawnEntry(EntityType.RABBIT, 100, 1, 2)
    );


    public RunDownHouseStructure(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    /**
     * This is how the worldgen code knows what to call when it
     * is time to create the pieces of the structure for generation.
     */
    @Override
    public StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
        return RunDownHouseStructure.Start::new;
    }

    @Override
    public Pool<SpawnSettings.SpawnEntry> getMonsterSpawns() {
        return STRUCTURE_MONSTERS;
    }

    @Override
    public Pool<SpawnSettings.SpawnEntry> getCreatureSpawns() {
        return STRUCTURE_CREATURES;
    }


    /*
     * This is where extra checks can be done to determine if the structure can spawn here.
     * This only needs to be overridden if you're adding additional spawn conditions.
     *
     * Fun fact, if you set your structure separation/spacing to be 0/1, you can use
     * shouldStartAt to return true only if certain chunk coordinates are passed in
     * which allows you to spawn structures only at certain coordinates in the world.
     *
     * Notice how the biome is also passed in. Though, you are not going to do any biome
     * checking here as you should've added this structure to the biomes you
     * wanted already with the biome load event.
     *
     * Basically, this method is used for determining if the land is at a suitable height,
     * if certain other structures are too close or not, or some other restrictive condition.
     *
     * For example, Pillager Outposts added a check to make sure it cannot spawn within 10 chunk of a Village.
     * (Bedrock Edition seems to not have the same check)
     *
     *
     * Also, please for the love of god, do not do dimension checking here.
     * If you do and another mod's dimension is trying to spawn your structure,
     * the locate command will make minecraft hang forever and break the game.
     *
     * Instead, use the removeStructureSpawningFromSelectedDimension method in
     * StructureTutorialMain class. If you check for the dimension there and do not add your
     * structure's spacing into the chunk generator, the structure will not spawn in that dimension!
     */
    @Override
    protected boolean shouldStartAt(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long seed, ChunkRandom chunkRandom, ChunkPos chunkPos, Biome biome, ChunkPos chunkPos2, DefaultFeatureConfig featureConfig, HeightLimitView heightLimitView) {
        BlockPos centerOfChunk = new BlockPos(chunkPos.x * 16, 0, chunkPos.z * 16);

        // Grab height of land. Will stop at first non-air block.
        int landHeight = chunkGenerator.getHeightInGround(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Type.WORLD_SURFACE_WG, heightLimitView);

        // Grabs column of blocks at given position. In overworld, this column will be made of stone, water, and air.
        // In nether, it will be netherrack, lava, and air. End will only be endstone and air. It depends on what block
        // the chunk generator will place for that dimension.
        VerticalBlockSample columnOfBlocks = chunkGenerator.getColumnSample(centerOfChunk.getX(), centerOfChunk.getZ(), heightLimitView);

        // Combine the column of blocks with land height and you get the top block itself which you can test.
        BlockState topBlock = columnOfBlocks.getState(centerOfChunk.up(landHeight));

        // Now we test to make sure our structure is not spawning on water or other fluids.
        // You can do height check instead too to make it spawn at high elevations.
        return topBlock.getFluidState().isEmpty(); //landHeight > 100;
    }

    /**
     * Handles calling up the structure's pieces class and height that structure will spawn at.
     */
    public static class Start extends MarginedStructureStart<DefaultFeatureConfig> {
        public Start(StructureFeature<DefaultFeatureConfig> structureIn, ChunkPos chunkPos, int referenceIn, long seedIn) {
            super(structureIn, chunkPos, referenceIn, seedIn);
        }

        @Override
        public void init(DynamicRegistryManager dynamicRegistryManager, ChunkGenerator chunkGenerator, StructureManager structureManager, ChunkPos chunkPos, Biome biome, DefaultFeatureConfig defaultFeatureConfig, HeightLimitView heightLimitView) {

            // Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
            int x = chunkPos.x * 16;
            int z = chunkPos.z * 16;

            /*
             * We pass this into method_30419 to tell it where to generate the structure.
             * If method_30419's last parameter is true, blockpos's Y value is ignored and the
             * structure will spawn at terrain height instead. Set that parameter to false to
             * force the structure to spawn at blockpos's Y value instead. You got options here!
             */
            BlockPos.Mutable centerPos = new BlockPos.Mutable(x, 0, z);

            /*
             * If you are doing Nether structures, you'll probably want to spawn your structure on top of ledges.
             * Best way to do that is to use getColumnSample to grab a column of blocks at the structure's x/z position.
             * Then loop through it and look for land with air above it and set blockpos's Y value to it.
             * Make sure to set the final boolean in StructurePoolBasedGenerator.method_30419 to false so
             * that the structure spawns at blockpos's y value instead of placing the structure on the Bedrock roof!
             */
            //VerticalBlockSample blockView = chunkGenerator.getColumnSample(blockpos.getX(), blockpos.getZ(), heightLimitView);

            StructurePoolFeatureConfig structureSettingsAndStartPool = new StructurePoolFeatureConfig(() -> dynamicRegistryManager.get(Registry.STRUCTURE_POOL_KEY)
                    // The path to the starting Template Pool JSON file to read.
                    //
                    // Note, this is "desertstorm:run_down_house/start_pool" which means
                    // the game will automatically look into the following path for the template pool:
                    // "resources/data/desertstorm/worldgen/template_pool/run_down_house/start_pool.json"
                    // This is why your pool files must be in "data/<modid>/worldgen/template_pool/<the path to the pool here>"
                    // because the game automatically will check in worldgen/template_pool for the pools.
                    .get(Utils.NewIdentifier("run_down_house/start_pool")),

                    // How many pieces outward from center can a recursive jigsaw structure spawn.
                    // Our structure is only 1 piece outward and isn't recursive so any value of 1 or more doesn't change anything.
                    // However, I recommend you keep this a decent value like 10 so people can use datapacks to add additional pieces to your structure easily.
                    // But don't make it too large for recursive structures like villages or you'll crash server due to hundreds of pieces attempting to generate!
                    10);

            // All a structure has to do is call this method to turn it into a jigsaw based structure!
            StructurePoolBasedGenerator.generate(
                    dynamicRegistryManager,
                    structureSettingsAndStartPool,
                    PoolStructurePiece::new,
                    chunkGenerator,
                    structureManager,
                    centerPos, // Position of the structure. Y value is ignored if last parameter is set to true.
                    this, // The class instance that holds the list that will be populated with the jigsaw pieces after this method.
                    this.random,
                    false, // Special boundary adjustments for villages. It's... hard to explain. Keep this false and make your pieces not be partially intersecting.
                    // Either not intersecting or fully contained will make children pieces spawn just fine. It's easier that way.
                    true, // Place at heightmap (top land). Set this to false for structure to be place at the passed in blockpos's Y value instead.
                    // Definitely keep this false when placing structures in the nether as otherwise, heightmap placing will put the structure on the Bedrock roof.
                    heightLimitView);

            // **THE FOLLOWING LINE IS OPTIONAL**
            //
            // Right here, you can do interesting stuff with the pieces in this.pieces such as offset the
            // centerpiece by 50 blocks up for no reason, remove repeats of a piece or add a new piece so
            // only 1 of that piece exists, etc. But you do not have access to the piece's blocks as this list
            // holds just the piece's size and positions. Blocks will be placed much later by the game.
            //
            // In this case, we do `piece.offset` to raise pieces up by 1 block so that the house is not right on
            // the surface of water or sunken into land a bit. NOTE: land added by Structure.JIGSAW_STRUCTURES
            // will also be moved alongside the piece. If you do not want this land, do not add your structure to the
            // Structure.JIGSAW_STRUCTURES field and now your pieces can be set on the regular terrain instead.
            this.children.forEach(piece -> piece.translate(0, 1, 0));

            // Since by default, the start piece of a structure spawns with its corner at centerPos
            // and will randomly rotate around that corner, we will center the piece on centerPos instead.
            // This is so that our structure's start piece is now centered on the water check done in shouldStartAt.
            // Whatever the offset done to center the start piece, that offset is applied to all other pieces
            // so the entire structure is shifted properly to the new spot.
            Vec3i structureCenter = this.children.get(0).getBoundingBox().getCenter();
            int xOffset = centerPos.getX() - structureCenter.getX();
            int zOffset = centerPos.getZ() - structureCenter.getZ();
            for (StructurePiece structurePiece : this.children) {
                structurePiece.translate(xOffset, 0, zOffset);
            }

            // Sets the bounds of the structure once you are finished.
            this.setBoundingBoxFromChildren();

            // I use to debug and quickly find out if the structure is spawning or not and where it is.
            // This is returning the coordinates of the center starting piece.
            //Utils.Log("Rundown House at ", this.children.get(0).getBoundingBox().getMinX(), this.children.get(0).getBoundingBox().getMinY(), this.children.get(0).getBoundingBox().getMinZ());
        }

    }
}