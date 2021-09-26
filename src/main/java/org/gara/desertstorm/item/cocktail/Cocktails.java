package org.gara.desertstorm.item.cocktail;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.gara.desertstorm.DamageSources;
import org.gara.desertstorm.DesertStorm;
import org.gara.desertstorm.Utils;
import org.jetbrains.annotations.Nullable;

public final class Cocktails {
    public static final Cocktail EMPTY;
    public static final Cocktail RADIOACTIVE_COCKTAIL;
    public static final Cocktail DISLOCATOR;
    public static final Cocktail SUNRISE;
    public static final Cocktail SUNSET;
    public static final Cocktail ICED;
    public static final Cocktail HOT_COCOA;
    public static final Cocktail HEALTHY_SMOOTHIE;
    public static final Cocktail MULTIVITAMIN;
    public static final Cocktail MIDAS_SPECIAL;
    public static final Cocktail MOLOTOV;
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
        // Cocktails
        EMPTY = registerCocktail(new Cocktail("empty"));
        // Special Effects
        RADIOACTIVE_COCKTAIL = registerCocktail(
                new Cocktail("radioactive", new StatusEffectInstance(RANDOM, 1)));
        DISLOCATOR = registerCocktail(
                new Cocktail("dislocator", new StatusEffectInstance(TELEPORTER, 1)));
        ICED = registerCocktail(
                new Cocktail("iced", new StatusEffectInstance(FREEZING, 170)));
        MIDAS_SPECIAL = registerCocktail(new Cocktail("midas_special",
                new StatusEffectInstance(MIDAS_TOUCH, 600)));
        MOLOTOV = registerCocktail(new Cocktail("molotov",
                new StatusEffectInstance(MOLOTOV_THROWN, 1)));
        // Minecraft Effects
        SUNRISE = registerCocktail(new Cocktail("sunrise",
                new StatusEffectInstance(StatusEffects.GLOWING, 1800)));
        SUNSET = registerCocktail(new Cocktail("sunset",
                new StatusEffectInstance(StatusEffects.ABSORPTION, 3600),
                new StatusEffectInstance(StatusEffects.REGENERATION, 1800)));
        HOT_COCOA = registerCocktail(new Cocktail("hot_cocoa",
                new StatusEffectInstance(StatusEffects.SPEED, 600),
                new StatusEffectInstance(StatusEffects.HASTE, 600)));
        HEALTHY_SMOOTHIE = registerCocktail(new Cocktail("healthy_smoothie",
                new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 10, 5)));
        MULTIVITAMIN = registerCocktail(new Cocktail("multivitamin",
                new StatusEffectInstance(StatusEffects.NIGHT_VISION, 3600),
                new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 10, 5)));
    }

    private static <T extends StatusEffect> T registerEffect(String id, T entry) {
        return Registry.register(Registry.STATUS_EFFECT, Utils.NewIdentifier(id), entry);
    }

    private static Cocktail registerCocktail(Cocktail cocktail) {
        return Registry.register(DesertStorm.COCKTAIL_REGISTRY, Utils.NewIdentifier(cocktail.name), cocktail);
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
            super(StatusEffectCategory.HARMFUL, Utils.GOLD);
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
                world.createExplosion(attacker, DamageSources.MOLOTOV, null, target.getX(), target.getY(), target.getZ(), 1.5F, true, Explosion.DestructionType.NONE);
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
            Utils.Debug();
            if (!target.isSpectator()) {
                Utils.Debug();
                int i = 0;
                StatusEffect statusEffect;
                do {
                    Utils.Log(i);
                    ++i;
                    statusEffect = Registry.STATUS_EFFECT.get(target.getRandom().nextInt());
                } while (i < 30 && target.hasStatusEffect(statusEffect) && !target.addStatusEffect(new StatusEffectInstance(statusEffect, 30 * 20), attacker));
                Utils.Log(i);
            }
        }
    }
}