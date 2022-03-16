package org.gara.desertstorm.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;
import org.gara.desertstorm.Utils;

public class DSEntities {
    public static final EntityType<MonkeyEntity> MONKEY = registerEntity("monkey",
            FabricEntityTypeBuilder.createLiving().defaultAttributes(MonkeyEntity::createMonkeyAttributes)
                    .entityFactory(MonkeyEntity::new).dimensions(EntityDimensions.fixed(1.5f, 1.0f)).build());    // Entities
    public static final EntityType<MonkeyKingEntity> MONKEY_KING = registerEntity("monkey_king",
            FabricEntityTypeBuilder.createLiving().defaultAttributes(MonkeyKingEntity::createMonkeyKingAttributes)
                    .entityFactory(MonkeyKingEntity::new).fireImmune()
                    .dimensions(EntityDimensions.fixed(2f, 1.5f)).build());
    public static final EntityType<SandWither> SAND_WITHER = registerEntity("sand_wither",
            FabricEntityTypeBuilder.createMob().defaultAttributes(SandWither::createWitherAttributes)
                    .entityFactory(SandWither::new)
                    .dimensions(EntityDimensions.fixed(1f, 3.5f)).build());
    public static final EntityType<RollingBarrel> ROLLING_BARREL = registerEntity("rolling_barrel", FabricEntityTypeBuilder
            .createMob().defaultAttributes(RollingBarrel::createBarrelAttributes)
            .entityFactory((EntityType.EntityFactory<RollingBarrel>) RollingBarrel::new)
            .dimensions(EntityDimensions.fixed(1.1f, 1)).fireImmune().build());
    public static final EntityType<Tornado> TORNADO = registerEntity("tornado", FabricEntityTypeBuilder
            .createMob().entityFactory(Tornado::new)
            .dimensions(Tornado.dimensions).fireImmune()
            .defaultAttributes(Tornado::createTornadoAttributes).build());
    public static final EntityType<Sandstorm> SANDSTORM = registerEntity("sandstorm", FabricEntityTypeBuilder.create(SpawnGroup.MISC, Sandstorm::new).dimensions(Sandstorm.dimensions).fireImmune().build());
    public static final EntityType<Sandworm> SANDWORM = registerEntity("sandworm", FabricEntityTypeBuilder.createMob().entityFactory(Sandworm::new).dimensions(EntityDimensions.fixed(0.8f, 0.3f)).defaultAttributes(Sandworm::createAttributes).build());

    private static <T extends Entity> EntityType<T> registerEntity(String name, EntityType<T> entity) {
        return Registry.register(Registry.ENTITY_TYPE, Utils.NewIdentifier(name), entity);
    }
}
