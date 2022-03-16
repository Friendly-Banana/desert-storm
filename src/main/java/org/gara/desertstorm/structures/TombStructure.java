package org.gara.desertstorm.structures;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.structure.PoolStructurePiece;
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

import java.util.Optional;

public class TombStructure extends StructureFeature<StructurePoolFeatureConfig> {
    public static final Pool<SpawnSettings.SpawnEntry> STRUCTURE_MONSTERS = Pool.of(
            new SpawnSettings.SpawnEntry(EntityType.HUSK, 10, 2, 5),
            new SpawnSettings.SpawnEntry(EntityType.SKELETON, 100, 2, 7)
    );

    public TombStructure(Codec<StructurePoolFeatureConfig> codec) {
        super(codec, TombStructure::createPiecesGenerator);
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
                .get(Utils.NewIdentifier("tomb")), 5);

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
            Utils.Log("Tomb " + blockpos);
        }
        return structurePiecesGenerator;
    }
}
