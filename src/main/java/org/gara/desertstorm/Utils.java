package org.gara.desertstorm;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Utils {
    public static final String MOD_ID = "desertstorm";
    public static final int GOLD = 0xffd700, SAND = 0xffe11f, ICE = 0x80e5ef;
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private static int counter = 0;

    public static void Debug() {
        counter++;
        LOGGER.debug(StackWalker.getInstance().walk(stream -> stream.skip(1).findFirst().get()).getMethodName() + ": " + counter);
    }

    public static void Log(Object... objects) {
        LOGGER.info(Arrays.stream(objects).map(Object::toString).collect(Collectors.joining(", ")));
    }

    public static <T> T NotNull(@Nullable T one, T fallback) {
        return one != null ? one : fallback;
    }

    public static TranslatableText GetTooltip(String itemName, Object... objects) {
        return new TranslatableText("item." + MOD_ID + "." + itemName + ".tooltip", objects);
    }

    public static Identifier NewIdentifier(String name) {
        return new Identifier(MOD_ID, name);
    }

    public static boolean IsSurvival(PlayerEntity player) {
        return !player.isCreative() && !player.isSpectator();
    }

    /**
     * prevent dropping at 1 and despawning at 0
     */
    public static void SetTimeFallingToMax(FallingBlockEntity fallingBlockEntity) {
        fallingBlockEntity.timeFalling = -2147483648;
    }

    public static <T extends Entity> T CreateAndTeleport(EntityType<T> entityType, World world, Vec3i pos) {
        return CreateAndTeleport(entityType, world, Vec3d.of(pos));
    }

    public static <T extends Entity> T CreateAndTeleport(EntityType<T> entityType, World world, Vec3d pos) {
        T entity = entityType.create(world);
        assert entity != null;
        entity.refreshPositionAfterTeleport(pos);
        return entity;
    }
}