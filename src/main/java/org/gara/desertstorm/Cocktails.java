package org.gara.desertstorm;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public final class Cocktails {
    public static final Potion RADIOACTIVE;
    public static final Potion DISLOCATOR;
    public static final Potion SUNRISE;
    public static final Potion SUNSET;
    public static final Potion ICED;
    public static final Potion HOT_COCOA;
    public static final Potion HEALTHY_SMOOTHIE;
    public static final Potion MULTIVITAMIN;
    public static final Potion MIDAS_SPECIAL;
    public static final Potion MOLOTOV;
    public static final StatusEffect MIDAS_TOUCH;
    public static final StatusEffect MOLOTOV_THROWN;
    public static final StatusEffect TELEPORTER;
    public static final StatusEffect FREEZING;
    public static final StatusEffect RANDOM;

    static {
        // Custom Effects
        MOLOTOV_THROWN = registerEffect("molotov", new Molotov());
        MIDAS_TOUCH = registerEffect("midas_touch", new MidasTouch());
        TELEPORTER = registerEffect("teleporter", new Teleporter());
        FREEZING = registerEffect("freezing", new Freezing());
        RANDOM = registerEffect("random", new Random());
        RADIOACTIVE = registerPotion("radioactive", new StatusEffectInstance(RANDOM, 1));
        DISLOCATOR = registerPotion("dislocator", new StatusEffectInstance(TELEPORTER, 1));
        ICED = registerPotion("iced", new StatusEffectInstance(FREEZING, 170));
        MIDAS_SPECIAL = registerPotion("midas_special", new StatusEffectInstance(MIDAS_TOUCH, 600));
        MOLOTOV = registerPotion("molotov", new StatusEffectInstance(MOLOTOV_THROWN, 1));
        // Minecraft Effects
        SUNRISE = registerPotion("sunrise", new StatusEffectInstance(StatusEffects.GLOWING, 1800));
        SUNSET = registerPotion("sunset", new StatusEffectInstance(StatusEffects.ABSORPTION, 3600),
                new StatusEffectInstance(StatusEffects.REGENERATION, 1800));
        HOT_COCOA = registerPotion("hot_cocoa", new StatusEffectInstance(StatusEffects.SPEED, 600),
                new StatusEffectInstance(StatusEffects.HASTE, 600));
        HEALTHY_SMOOTHIE = registerPotion("healthy_smoothie", new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 10, 5));
        MULTIVITAMIN = registerPotion("multivitamin", new StatusEffectInstance(StatusEffects.NIGHT_VISION, 3600),
                new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 10, 5));
    }

    private static <T extends StatusEffect> T registerEffect(String id, T entry) {
        return Registry.register(Registry.STATUS_EFFECT, Utils.NewIdentifier(id), entry);
    }

    private static Potion registerPotion(String name, StatusEffectInstance... effects) {
        return Registry.register(Registry.POTION, name, new Potion(effects));
    }

    private static class CustomStatusEffect extends StatusEffect {
        protected CustomStatusEffect(StatusEffectCategory category, int color) {
            super(category, color);
        }

        @Override
        public boolean canApplyUpdateEffect(int duration, int amplifier) {
            int k;
            // REGENERATION 50, WITHER 40, POISON 25, HUNGER always
            if (this == MIDAS_TOUCH) {
                k = 25 >> amplifier;
                if (k > 0) {
                    return duration % k == 0;
                } else {
                    return true;
                }
            }
            return this == FREEZING;
        }
    }

    private static class MidasTouch extends CustomStatusEffect {
        public MidasTouch() {
            super(StatusEffectCategory.BENEFICIAL, Utils.GOLD);
        }

        @Override
        public void applyUpdateEffect(LivingEntity entity, int amplifier) {
            if (entity instanceof ServerPlayerEntity serverPlayerEntity && !entity.isSpectator()) {
                ServerWorld serverWorld = serverPlayerEntity.getServerWorld();
                final BlockPos down = serverPlayerEntity.getBlockPos().down();
                BlockState state = serverWorld.getBlockState(down);
                if (!(state.isAir() || state.hasBlockEntity() || state.getBlock().getBlastResistance() > 30)) {
                    serverWorld.setBlockState(down, Blocks.GOLD_BLOCK.getDefaultState());
                }
            }
        }
    }

    private static class Freezing extends CustomStatusEffect {
        private int freezingCounter = 30;

        public Freezing() {
            super(StatusEffectCategory.HARMFUL, Utils.ICE);
        }

        @Override
        public void applyUpdateEffect(LivingEntity entity, int amplifier) {
            if (!entity.isSpectator()) {
                entity.setFrozenTicks(freezingCounter);
                ++freezingCounter;
            }
        }
    }

    private static class Molotov extends InstantStatusEffect {
        public Molotov() {
            super(StatusEffectCategory.NEUTRAL, 0xFF0000);
        }

        @Override
        public void applyInstantEffect(@Nullable Entity source, @Nullable Entity attacker, LivingEntity target,
                                       int amplifier, double proximity) {
            if (!target.isSpectator()) {
                World world = target.getEntityWorld();
                world.createExplosion(attacker, DamageSources.MOLOTOV, null, target.getX(), target.getY(), target.getZ(), 3F, true, Explosion.DestructionType.BREAK);
            }
        }
    }

    private static class Teleporter extends InstantStatusEffect {
        public Teleporter() {
            super(StatusEffectCategory.NEUTRAL, Utils.GOLD);
        }

        @Override
        public void applyInstantEffect(@Nullable Entity source, @Nullable Entity attacker, LivingEntity target,
                                       int amplifier, double proximity) {
            Items.CHORUS_FRUIT.finishUsing(ItemStack.EMPTY, target.getEntityWorld(), target);
        }
    }

    private static class Random extends InstantStatusEffect {
        public Random() {
            super(StatusEffectCategory.NEUTRAL, Utils.GOLD);
        }

        @Override
        public void applyInstantEffect(@Nullable Entity source, @Nullable Entity attacker, LivingEntity target,
                                       int amplifier, double proximity) {
            if (!target.isSpectator()) {
                int i = 0;
                StatusEffect statusEffect;
                do {
                    ++i;
                    statusEffect = Registry.STATUS_EFFECT.get(target.getRandom().nextInt());
                    Utils.Log(statusEffect);
                } while (i < 5 && target.hasStatusEffect(statusEffect) && !target.addStatusEffect(new StatusEffectInstance(statusEffect, 30 * 20), attacker));
            }
        }
    }
}