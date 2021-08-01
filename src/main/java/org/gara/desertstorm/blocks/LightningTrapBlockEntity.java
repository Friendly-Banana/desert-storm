package org.gara.desertstorm.blocks;

import org.gara.desertstorm.DesertStorm;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LightningTrapBlockEntity extends BlockEntity {

    public LightningTrapBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(DesertStorm.TRAP_BLOCK_ENTITY, blockPos, blockState);
    }
}