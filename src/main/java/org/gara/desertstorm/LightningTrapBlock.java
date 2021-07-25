package org.gara.desertstorm;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class LightningTrapBlock extends Block {


    public static final BooleanProperty CHARGED = BooleanProperty.create("charged");

    public LightningTrapBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(CHARGED, false));
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClientSide && !blockState.getValue(CHARGED) && player.isHolding(DesertStorm.BATTERY_ITEM)) {
            player.playSound(SoundEvents.RESPAWN_ANCHOR_CHARGE, 1, 1);
            level.setBlockAndUpdate(blockPos, blockState.setValue(CHARGED, true));
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
     }

    @Override
    public void stepOn(Level level, BlockPos blockPos, BlockState blockState, Entity entity) {
        if (!level.isClientSide && blockState.getValue(CHARGED)) {
            // Summoning the Lighting Bolt at the block
            LightningBolt lightningBolt = (LightningBolt)EntityType.LIGHTNING_BOLT.create(level);
            lightningBolt.moveTo(entity.getPosition(0));
            level.addFreshEntity(lightningBolt);
            level.setBlockAndUpdate(blockPos, blockState.setValue(CHARGED, false));
        }
    }
}