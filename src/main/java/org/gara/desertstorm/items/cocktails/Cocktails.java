package org.gara.desertstorm.items.cocktails;

import org.gara.desertstorm.Utils;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.Registry;

public final class Cocktails {
        public static final Cocktail EMPTY;
        public static final Cocktail WATER;
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

        private static StatusEffect register(String id, StatusEffect entry) {
                return Registry.register(Registry.STATUS_EFFECT, id, entry);
        }

        private static Cocktail register(Cocktail potion) {
                return Registry.register(Registry.POTION, Utils.MOD_ID + ":" + potion.getName(), potion);
        }

        static {
                // Custom Effects
                MOLOTOV_THROWN = register("molotov", new StatusEffect(StatusEffectCategory.NEUTRAL, 0) {
                        public void applyUpdateEffect(LivingEntity entity, int amplifier) {
                                if (entity instanceof ServerPlayerEntity && !entity.isSpectator()) {
                                        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) entity;
                                        ServerWorld serverWorld = serverPlayerEntity.getServerWorld();
                                        serverWorld.spawnEntity(null);
                                }

                        }
                });
                MIDAS_TOUCH = register("midas_touch", new StatusEffect(StatusEffectCategory.BENEFICIAL, Utils.GOLD) {
                        @Override
                        public void applyUpdateEffect(LivingEntity entity, int amplifier) {
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
                // Cocktails
                EMPTY = register(new Cocktail("empty", new StatusEffectInstance[0]));
                WATER = register(new Cocktail("water",
                                new StatusEffectInstance[] { new StatusEffectInstance(StatusEffects.GLOWING, 3600) }));
                // Special Effects
                RADIOACTIVE_COCKTAIL = register(new Cocktail("radioactive",
                                new StatusEffectInstance[] { new StatusEffectInstance(StatusEffects.NIGHT_VISION, 3600),
                                                new StatusEffectInstance(StatusEffects.GLOWING, 3600) }));
                DISLOCATOR = register(new Cocktail("dislocator",
                                new StatusEffectInstance[] { new StatusEffectInstance(StatusEffects.GLOWING, 3600) }));
                ICECOLD = register(new Cocktail("icecold",
                                new StatusEffectInstance[] { new StatusEffectInstance(StatusEffects.GLOWING, 3600) }));
                MIDAS_SPECIAL = register(new Cocktail("midas_special",
                                new StatusEffectInstance[] { new StatusEffectInstance(MIDAS_TOUCH, 30*20) }));
                MOLOTOV = register(new Cocktail("molotov", new StatusEffectInstance[] {
                                new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 3600) }));
                // Minecraft Effects
                SUNRISE = register(new Cocktail("sunrise",
                                new StatusEffectInstance[] { new StatusEffectInstance(StatusEffects.GLOWING, 3600) }));
                SUNSET = register(new Cocktail("sunset",
                                new StatusEffectInstance[] { new StatusEffectInstance(StatusEffects.ABSORPTION, 3600),
                                                new StatusEffectInstance(StatusEffects.REGENERATION, 1800) }));
                HOT_COCOA = register(new Cocktail("hot_cocoa",
                                new StatusEffectInstance[] { new StatusEffectInstance(StatusEffects.SPEED, 30),
                                                new StatusEffectInstance(StatusEffects.HASTE, 30) }));
                HEALTHY_SMOOTHIE = register(new Cocktail("healthy_smoothie", new StatusEffectInstance[] {
                                new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 10, 5) }));
                MULTIVITAMINE = register(new Cocktail("multivitamine",
                                new StatusEffectInstance[] { new StatusEffectInstance(StatusEffects.NIGHT_VISION, 3600),
                                                new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 10, 5) }));
        }
}