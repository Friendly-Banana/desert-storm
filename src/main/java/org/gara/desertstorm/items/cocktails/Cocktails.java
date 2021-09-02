package org.gara.desertstorm.items.cocktails;

import org.gara.desertstorm.Utils;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.*;
import net.minecraft.item.ChorusFruitItem;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.Registry;

public final class Cocktails {
    public static final Cocktail EMPTY;
    public static final Cocktail RADIOACTIVE_COCKTAIL;
    public static final Cocktail DISLOCATOR;
    public static final Cocktail SUNRISE;
    public static final Cocktail SUNSET;
    public static final Cocktail ICECOLD;
    public static final Cocktail HOT_COCOA;
    public static final Cocktail HEALTHY_SMOOTHIE;
    public static final Cocktail MULTIVITAMINE;
    public static final Cocktail MIDAS_SPECIAL;
    public static final Cocktail MOLOTOV;
    public static final StatusEffect MIDAS_TOUCH;
    public static final StatusEffect MOLOTOV_THROWN;
    public static final StatusEffect TELEPORTER;
    public static final StatusEffect FREEZING;
    public static final StatusEffect RANDOM;

    private static <T extends StatusEffect> T registerEffect(String id, T entry) {
        return Registry.register(Registry.STATUS_EFFECT, Utils.NewIdentifier(id), entry);
    }

    private static Cocktail registerCocktail(Cocktail cocktail) {
        return Registry.register(Registry.POTION, Utils.NewIdentifier(cocktail.getName()), cocktail);
    }

    static {
        // Custom Effects
        MOLOTOV_THROWN = registerEffect("molotov", new StatusEffect(StatusEffectCategory.NEUTRAL, 0) {
            public void applyUpdateEffect(LivingEntity entity, int amplifier) {
                if (entity instanceof ServerPlayerEntity && !entity.isSpectator()) {
                    ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) entity;
                    ServerWorld serverWorld = serverPlayerEntity.getServerWorld();
                    serverWorld.spawnEntity(null);
                }

            }
        });
        MIDAS_TOUCH = registerEffect("midas_touch", new StatusEffect(StatusEffectCategory.BENEFICIAL, Utils.GOLD) {
            @Override
            public void applyUpdateEffect(LivingEntity entity, int amplifier) {
                Utils.Debug();
                if (entity instanceof ServerPlayerEntity && !entity.isSpectator()) {
                    ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) entity;
                    ServerWorld serverWorld = serverPlayerEntity.getServerWorld();
                    BlockState state = serverWorld.getBlockState(serverPlayerEntity.getBlockPos());
                    if (!(state.hasBlockEntity() || state.getBlock().getBlastResistance() > 30)) {
                        serverWorld.setBlockState(serverPlayerEntity.getBlockPos(),
                                Blocks.GOLD_BLOCK.getDefaultState());
                    }
                }
            }
        });
        TELEPORTER = registerEffect("teleporter", new InstantStatusEffect(StatusEffectCategory.NEUTRAL, Utils.GOLD) {
            @Override
            public void applyInstantEffect(@Nullable Entity source, @Nullable Entity attacker, LivingEntity target,
                    int amplifier, double proximity) {
                ((ChorusFruitItem) Items.CHORUS_FRUIT).finishUsing(null, target.getEntityWorld(), target);

            }
        });
        FREEZING = registerEffect("freezing", new StatusEffect(StatusEffectCategory.HARMFUL, Utils.GOLD) {
            private int freezingCounter = 30;

            @Override
            public void applyUpdateEffect(LivingEntity entity, int amplifier) {
                if (entity instanceof ServerPlayerEntity && !entity.isSpectator()) {
                    ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) entity;
                    serverPlayerEntity.setFrozenTicks(freezingCounter);
                    ++freezingCounter;
                }
            }
        });
        RANDOM = registerEffect("random", new InstantStatusEffect(StatusEffectCategory.NEUTRAL, Utils.GOLD) {
            @Override
            public void applyInstantEffect(@Nullable Entity source, @Nullable Entity attacker, LivingEntity target,
                    int amplifier, double proximity) {
                if (!target.isSpectator()) {
                    int i = 0;
                    StatusEffect statusEffect;
                    do {
                        ++i;
                        statusEffect = Registry.STATUS_EFFECT.get(target.getRandom().nextInt());
                    } while (i < 30 && target.hasStatusEffect(statusEffect) && !target.addStatusEffect(new StatusEffectInstance(statusEffect, 30 * 20), source));                    
                }

            }
        });
        // Cocktails
        EMPTY = registerCocktail(new Cocktail("empty", new StatusEffectInstance[0]));
        // Special Effects
        RADIOACTIVE_COCKTAIL = registerCocktail(
                new Cocktail("radioactive", new StatusEffectInstance[] { new StatusEffectInstance(RANDOM, 1) }));
        DISLOCATOR = registerCocktail(
                new Cocktail("dislocator", new StatusEffectInstance[] { new StatusEffectInstance(TELEPORTER, 1) }));
        ICECOLD = registerCocktail(
                new Cocktail("icecold", new StatusEffectInstance[] { new StatusEffectInstance(FREEZING, 170) }));
        MIDAS_SPECIAL = registerCocktail(new Cocktail("midas_special",
                new StatusEffectInstance[] { new StatusEffectInstance(MIDAS_TOUCH, 600) }));
        MOLOTOV = registerCocktail(new Cocktail("molotov",
                new StatusEffectInstance[] { new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 200) }));
        // Minecraft Effects
        SUNRISE = registerCocktail(new Cocktail("sunrise",
                new StatusEffectInstance[] { new StatusEffectInstance(StatusEffects.GLOWING, 1800) }));
        SUNSET = registerCocktail(new Cocktail("sunset",
                new StatusEffectInstance[] { new StatusEffectInstance(StatusEffects.ABSORPTION, 3600),
                        new StatusEffectInstance(StatusEffects.REGENERATION, 1800) }));
        HOT_COCOA = registerCocktail(new Cocktail("hot_cocoa",
                new StatusEffectInstance[] { new StatusEffectInstance(StatusEffects.SPEED, 600),
                        new StatusEffectInstance(StatusEffects.HASTE, 600) }));
        HEALTHY_SMOOTHIE = registerCocktail(new Cocktail("healthy_smoothie",
                new StatusEffectInstance[] { new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 10, 5) }));
        MULTIVITAMINE = registerCocktail(new Cocktail("multivitamine",
                new StatusEffectInstance[] { new StatusEffectInstance(StatusEffects.NIGHT_VISION, 3600),
                        new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 10, 5) }));
    }
}