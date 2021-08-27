package org.gara.desertstorm.mixin;

import com.mojang.serialization.Codec;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

@Mixin(TreeDecoratorType.class)
public interface PublicTreeDecoratorType {
    @Invoker("register")
    static <P extends TreeDecorator> TreeDecoratorType<P> invokeRegister(String string, Codec<P> codec) {
        throw new AssertionError();
    }
}