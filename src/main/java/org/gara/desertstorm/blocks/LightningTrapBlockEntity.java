package org.gara.desertstorm.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.gara.desertstorm.DesertStorm;

public class LightningTrapBlockEntity extends BlockEntity {

    public LightningTrapBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(DesertStorm.TRAP_BLOCK_ENTITY, blockPos, blockState);
    }
}