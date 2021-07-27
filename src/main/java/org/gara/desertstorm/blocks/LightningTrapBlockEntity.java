package org.gara.desertstorm.blocks;

import com.mojang.authlib.GameProfile;

import org.gara.desertstorm.DesertStorm;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LightningTrapBlockEntity extends BlockEntity {
    private GameProfile owner;
    private static final String ownerKey = "Owner";

    public LightningTrapBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(DesertStorm.TRAP_BLOCK_ENTITY, blockPos, blockState);
    }

    // Serialize the BlockEntity
    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        if (owner != null) {
            compoundTag.putUUID(ownerKey, owner.getId());
        }
        return super.save(compoundTag);
    }

    // Deserialize the BlockEntity
    @Override
    public void load(CompoundTag compoundTag) {
        owner = new GameProfile(compoundTag.getUUID(ownerKey), null);
    }

    public void SetOwner(GameProfile gameProfile) {
        owner = gameProfile;
    }

    public GameProfile GetOwner() {
        return owner;
    }
}