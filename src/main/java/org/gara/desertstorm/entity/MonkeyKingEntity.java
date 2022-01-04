package org.gara.desertstorm.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MonkeyKingEntity extends MonkeyEntity {
    private final static int maxHealth = 100;
    private final ServerBossBar bossEvent;

    public MonkeyKingEntity(EntityType<? extends MonkeyEntity> entityType, World world) {
        super(entityType, world);
        this.bossEvent = (ServerBossBar) (new ServerBossBar(this.getDisplayName(), BossBar.Color.GREEN, BossBar.Style.PROGRESS))
                .setDarkenSky(true).setDragonMusic(true);
    }

    @Override
    public void tick() {
        super.tick();
        this.bossEvent.setPercent(this.getHealth() / maxHealth);
        if (age % (3 * 20) == 0) {
            PlayerEntity player = this.world.getClosestPlayer(this, 12);
            if (player != null) {
                RollingBarrel barrel = new RollingBarrel(this, this.world, this.getPos().lerp(player.getPos(), 0.1D));
                this.world.spawnEntity(barrel);
            }
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compoundTag) {
        super.readCustomDataFromNbt(compoundTag);
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
    }

    @Override
    public void setCustomName(@Nullable Text component) {
        super.setCustomName(component);
        this.bossEvent.setName(this.getDisplayName());
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity serverPlayer) {
        super.onStartedTrackingBy(serverPlayer);
        this.bossEvent.addPlayer(serverPlayer);
        serverPlayer.sendMessage(Text.of("Zeig mir, was der Hammer hängt!"), false);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity serverPlayer) {
        super.onStoppedTrackingBy(serverPlayer);
        this.bossEvent.removePlayer(serverPlayer);
    }

    public static DefaultAttributeContainer.Builder createMonkeyKingAttributes() {
        return MonkeyEntity.createMonkeyAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, maxHealth).add(EntityAttributes.GENERIC_ARMOR, 10);
    }
}