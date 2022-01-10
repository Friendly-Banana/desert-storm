package org.gara.desertstorm;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import org.gara.desertstorm.mixin.PublicTreeDecoratorType;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class CoconutDecorator extends TreeDecorator {
    public static final CoconutDecorator INSTANCE = new CoconutDecorator();
    public static final Codec<CoconutDecorator> CODEC = Codec.unit(() -> INSTANCE);
    public static final TreeDecoratorType<CoconutDecorator> DECORATOR_TYPE = PublicTreeDecoratorType.invokeRegister("coconut", CODEC);

    protected TreeDecoratorType<?> getType() {
        return DECORATOR_TYPE;
    }

    public void generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random,
                         List<BlockPos> logPositions, List<BlockPos> leavesPositions) {
        leavesPositions.forEach((blockPos) -> {
            if (random.nextInt(50) == 0) {
                blockPos = blockPos.down();
                if (Feature.isAir(world, blockPos)) {
                    replacer.accept(blockPos, DesertStorm.COCONUT_BLOCK.getDefaultState());
                }
            }
        });
    }
}
