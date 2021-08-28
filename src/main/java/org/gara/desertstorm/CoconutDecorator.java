package org.gara.desertstorm;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

import com.mojang.serialization.Codec;

import org.gara.desertstorm.mixin.PublicTreeDecoratorType;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

public class CoconutDecorator extends TreeDecorator {
    public static final CoconutDecorator INSTANCE = new CoconutDecorator();
    public static final Codec<CoconutDecorator> CODEC = Codec.unit(() -> {
        return INSTANCE;
    });

    protected TreeDecoratorType<?> type() {
        return PublicTreeDecoratorType.invokeRegister("coconut", CODEC);
    }

    public void place(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer,
            Random random, List<BlockPos> list, List<BlockPos> list2) {
        Utils.Log(list);
        list.forEach((blockPos) -> {
            if (random.nextInt(3) == 0) {
                Utils.Log("Coconut: " + blockPos.toString());
                if (Feature.isAir(levelSimulatedReader, blockPos)) {
                    biConsumer.accept(blockPos, DesertStorm.COCONUT_BLOCK.defaultBlockState());
                }
            }
        });
    }
}
