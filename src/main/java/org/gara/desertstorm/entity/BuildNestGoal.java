package org.gara.desertstorm.entity;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Random;

public class BuildNestGoal extends Goal {
    private final SpiderEntity spider;

    public BuildNestGoal(SpiderEntity spider) {
        this.spider = spider;
    }

    @Override
    public boolean canStart() {
        if (!this.spider.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
            return false;
        }
        if (this.spider.hasPassengers()) {
            return false;
        }
        return this.spider.getRandom().nextInt(toGoalTicks(20)) <= this.spider.world.getDifficulty().getId();
    }

    @Override
    public void tick() {
        Random random = this.spider.getRandom();
        World world = this.spider.world;
        int i = MathHelper.floor(this.spider.getX() - 1.0 + random.nextDouble() * 2.0);
        int j = MathHelper.floor(this.spider.getY() + random.nextDouble() * 2.0);
        int k = MathHelper.floor(this.spider.getZ() - 1.0 + random.nextDouble() * 2.0);
        BlockPos blockPos = new BlockPos(i, j, k);
        if (this.spider.age % (3 * 20) == 0 && canPlaceAt(world, blockPos)) {
            world.setBlockState(blockPos, Blocks.COBWEB.getDefaultState(), Block.NOTIFY_ALL);
            world.emitGameEvent(this.spider, GameEvent.BLOCK_PLACE, blockPos);
        }
    }

    private boolean canPlaceAt(World world, BlockPos pos) {
        return world.getBlockState(pos).isAir() && world.getOtherEntities(this.spider, Box.from(Vec3d.of(pos))).isEmpty();
    }
}

