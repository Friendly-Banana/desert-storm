package org.gara.desertstorm.structures;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.PostPlacementProcessor;
import net.minecraft.structure.StructureGeneratorFactory;
import net.minecraft.structure.StructurePiecesGenerator;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;
import org.gara.desertstorm.Utils;
import org.gara.desertstorm.entity.DSEntities;

import java.util.Optional;

public class KongLevelStructure extends StructureFeature<StructurePoolFeatureConfig> {
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
    public static final Pool<SpawnSettings.SpawnEntry> STRUCTURE_MONSTERS = Pool.of(
            new SpawnSettings.SpawnEntry(EntityType.ZOMBIE, 100, 2, 5)
    );
    public static final Pool<SpawnSettings.SpawnEntry> STRUCTURE_CREATURES = Pool.of(
            new SpawnSettings.SpawnEntry(DSEntities.MONKEY, 50, 3, 7),
            new SpawnSettings.SpawnEntry(EntityType.BAT, 10, 2, 15)
    );

    public KongLevelStructure(Codec<StructurePoolFeatureConfig> configCodec) {
        super(configCodec, KongLevelStructure::createPiecesGenerator, PostPlacementProcessor.EMPTY);
    }


    private static boolean isFeatureChunk(StructureGeneratorFactory.Context<StructurePoolFeatureConfig> context) {
        BlockPos spawnXZPosition = context.chunkPos().getCenterAtY(0);
        int landHeight = context.chunkGenerator().getHeightInGround(spawnXZPosition.getX(), spawnXZPosition.getZ(), Heightmap.Type.WORLD_SURFACE_WG, context.world());
        VerticalBlockSample columnOfBlocks = context.chunkGenerator().getColumnSample(spawnXZPosition.getX(), spawnXZPosition.getZ(), context.world());
        BlockState topBlock = columnOfBlocks.getState(landHeight);
        return topBlock.getFluidState().isEmpty();
    }

    public static Optional<StructurePiecesGenerator<StructurePoolFeatureConfig>> createPiecesGenerator(StructureGeneratorFactory.Context<StructurePoolFeatureConfig> context) {
        if (!isFeatureChunk(context)) {
            return Optional.empty();
        }
        StructurePoolFeatureConfig newConfig = new StructurePoolFeatureConfig(() -> context.registryManager().get(Registry.STRUCTURE_POOL_KEY)
                .get(Utils.NewIdentifier("kong/start_pool")), 5);

        StructureGeneratorFactory.Context<StructurePoolFeatureConfig> newContext = new StructureGeneratorFactory.Context<>(
                context.chunkGenerator(),
                context.biomeSource(),
                context.seed(),
                context.chunkPos(),
                newConfig,
                context.world(),
                context.validBiome(),
                context.structureManager(),
                context.registryManager()
        );

        BlockPos blockpos = context.chunkPos().getCenterAtY(0);
        Optional<StructurePiecesGenerator<StructurePoolFeatureConfig>> structurePiecesGenerator =
                StructurePoolBasedGenerator.generate(
                        newContext, PoolStructurePiece::new, blockpos, false, true);

        // I used to debug and quickly find out if the structure is spawning or not and where it is.
        // This is returning the coordinates of the center starting piece.
        if (structurePiecesGenerator.isPresent()) {
            Utils.Log("Kong Level " + blockpos);
        }
        return structurePiecesGenerator;
    }
}