package org.gara.desertstorm.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class LightningTrapBlockEntity extends BlockEntity {

    public LightningTrapBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(DSBlocks.TRAP_BLOCK_ENTITY, blockPos, blockState);
    }
}