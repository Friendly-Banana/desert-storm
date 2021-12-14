package org.gara.desertstorm.entity;

import net.fabricmc.fabric.impl.object.builder.FabricEntityType;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.gara.desertstorm.Utils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Sandstorm extends AreaEffectCloudEntity {
    final List<FallingBlockEntity> flyingBlocks;

    public Sandstorm(EntityType<? extends Sandstorm> entityType, World world) {
        super(entityType, world);
        this.setDuration(3 * 60 * 20);
        this.setRadius(5.0F);
        this.setParticleType(ParticleTypes.DRAGON_BREATH);
        this.setColor(Utils.SAND);
        this.addEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 1));
        this.addEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 10));

        flyingBlocks = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            FallingBlockEntity sandEntity = FabricEntityType.FALLING_BLOCK.create(world);
            assert sandEntity != null;
            sandEntity.timeFalling = 1;
            sandEntity.dropItem = false;
            sandEntity.setPosition(this.getLerpedPos(0.1F));
            sandEntity.setNoGravity(true);
            sandEntity.startRiding(this);
            world.spawnEntity(sandEntity);
            flyingBlocks.add(sandEntity);
        }
    }

    @Override
    public void tick() {
        super.tick();
        // keep sand blocks from despawning
        for (FallingBlockEntity block :  flyingBlocks)
        {
            block.timeFalling = 1;
        }
    }
}