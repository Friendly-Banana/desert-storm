package org.gara.desertstorm;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LightningTrapBlock extends Block {

    public static final IntProperty CHARGE_LEVEL = IntProperty.of("charged", 0, 4);

    public LightningTrapBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(CHARGE_LEVEL, 0));
    }
    
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        player.playSound(SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, 1, 1);
        world.setBlockState(pos, state.with(CHARGE_LEVEL, Math.min(4, world.getBlockState(pos).get(CHARGE_LEVEL) + 1)));
        return ActionResult.SUCCESS;
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (world.getBlockState(pos).get(CHARGE_LEVEL) > 0){
            //Summoning the Lighting Bolt at the block
            LightningEntity lightningEntity = (LightningEntity) EntityType.LIGHTNING_BOLT.create(world);
            lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(pos));
            world.spawnEntity(lightningEntity);
            world.setBlockState(pos, state.with(CHARGE_LEVEL, world.getBlockState(pos).get(CHARGE_LEVEL) - 1));
        }
        super.onSteppedOn(world, pos, state, entity);
    }


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(CHARGE_LEVEL);
    }
}