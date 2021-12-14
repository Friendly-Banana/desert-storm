package org.gara.desertstorm;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.*;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import org.gara.desertstorm.block.CoconutBlock;
import org.gara.desertstorm.block.LightningTrapBlock;
import org.gara.desertstorm.block.LightningTrapBlockEntity;
import org.gara.desertstorm.entity.*;
import org.gara.desertstorm.item.*;
import org.gara.desertstorm.structures.DSStructures;

public class DesertStorm implements ModInitializer {
    // Items
    public static final SandblasterItem SANDBLASTER_ITEM;

    public static final SandstarItem SANDSTAR_ITEM;

    public static final BatteryItem BATTERY_ITEM;
    public static final BananaItem BANANA_ITEM;

    public static final SpawnEggItem MONKEY_SPAWN_EGG;
    public static final SpawnEggItem MONKEY_KING_SPAWN_EGG;
    public static final SpawnEggItem SAND_WITHER_SPAWN_EGG;

    // Blocks
    public static final CoconutBlock COCONUT_BLOCK;
    public static final BlockItem COCONUT_ITEM;
    public static final BlockItem END_GATEWAY_ITEM;
    public static final BlockItem END_PORTAL_ITEM;
    public static final BlockItem NETHER_PORTAL_ITEM;

    public static final LightningTrapBlock LIGHTNING_TRAP_BLOCK;
    public static final BlockItem LIGHTNING_TRAP_ITEM;
    public static final BlockEntityType<LightningTrapBlockEntity> TRAP_BLOCK_ENTITY;

    // Entities
    public static final EntityType<MonkeyEntity> MONKEY;
    public static final EntityType<MonkeyKingEntity> MONKEY_KING;
    public static final EntityType<SandWither> SAND_WITHER;
    public static final EntityType<RollingBarrel> ROLLING_BARREL;
    public static final EntityType<Sandstorm> SANDSTORM;
    public static final EntityType<Tornado> TORNADO;

    public static final ItemGroup ITEM_TAB;

    static {
        ITEM_TAB = FabricItemGroupBuilder.create(Utils.NewIdentifier("item_tab")).icon(() -> new ItemStack(Items.SAND)).build();

        BATTERY_ITEM = registerCustomItem(new BatteryItem(new FabricItemSettings().group(ITEM_TAB)));
        BANANA_ITEM = registerCustomItem(
                new BananaItem(new FabricItemSettings().group(ITEM_TAB).food(BananaItem.FOOD_PROPERTIES)));
        SANDSTAR_ITEM = registerCustomItem(
                new SandstarItem(new FabricItemSettings().group(ITEM_TAB).fireproof().rarity(Rarity.UNCOMMON)));
        SANDBLASTER_ITEM = registerItem("sandblaster", new SandblasterItem(new FabricItemSettings().group(ITEM_TAB)));


        NETHER_PORTAL_ITEM = registerItem("nether_portal", new BlockItem(Blocks.NETHER_PORTAL, new FabricItemSettings().group(ITEM_TAB)));
        END_PORTAL_ITEM = registerItem("end_portal", new BlockItem(Blocks.END_PORTAL, new FabricItemSettings().group(ITEM_TAB)));
        END_GATEWAY_ITEM = registerItem("end_gateway", new BlockItem(Blocks.END_GATEWAY, new FabricItemSettings().group(ITEM_TAB)));

        COCONUT_BLOCK = registerBlock("coconut", new CoconutBlock());
        COCONUT_ITEM = registerItem("coconut",
                new BlockItem(COCONUT_BLOCK, new FabricItemSettings().group(ITEM_TAB)));

        LIGHTNING_TRAP_BLOCK = registerBlock("lightning_trap", new LightningTrapBlock());
        LIGHTNING_TRAP_ITEM = registerItem("lightning_trap",
                new BlockItem(LIGHTNING_TRAP_BLOCK, new FabricItemSettings().group(ITEM_TAB)));
        TRAP_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, Utils.NewIdentifier("lightning_trap_block_entity"),
                FabricBlockEntityTypeBuilder.create(LightningTrapBlockEntity::new, LIGHTNING_TRAP_BLOCK).build());

        MONKEY = Registry.register(Registry.ENTITY_TYPE, Utils.NewIdentifier("monkey"),
                FabricEntityTypeBuilder.createLiving().defaultAttributes(MonkeyEntity::createMonkeyAttributes)
                        .entityFactory(MonkeyEntity::new).dimensions(EntityDimensions.fixed(1.5f, 1.0f)).build());
        MONKEY_SPAWN_EGG = registerItem("monkey_spawn_egg", new SpawnEggItem(MONKEY, 0x9a3c00, 0,
                new FabricItemSettings().group(ITEM_TAB)));

        MONKEY_KING = Registry.register(Registry.ENTITY_TYPE, Utils.NewIdentifier("monkey_king"),
                FabricEntityTypeBuilder.createLiving().defaultAttributes(MonkeyKingEntity::createMonkeyKingAttributes)
                        .spawnGroup(SpawnGroup.MONSTER).entityFactory(MonkeyKingEntity::new).fireImmune()
                        .dimensions(EntityDimensions.fixed(1.5f, 1.0f)).build());
        MONKEY_KING_SPAWN_EGG = registerItem("monkey_king_spawn_egg", new SpawnEggItem(MONKEY_KING, 0x5a1c00, 0x000000,
                new FabricItemSettings().group(ITEM_TAB)));

        SAND_WITHER = Registry.register(Registry.ENTITY_TYPE, Utils.NewIdentifier("sand_wither"),
                FabricEntityTypeBuilder.createLiving().defaultAttributes(SandWither::createWitherAttributes)
                        .spawnGroup(SpawnGroup.CREATURE).entityFactory(SandWither::new)
                        .dimensions(EntityDimensions.fixed(1f, 3.5f)).build());
        SAND_WITHER_SPAWN_EGG = registerItem("sand_wither_spawn_egg",
                new SpawnEggItem(SAND_WITHER, 0xffe900, 0, new FabricItemSettings().group(ITEM_TAB)));

        ROLLING_BARREL = Registry.register(Registry.ENTITY_TYPE, Utils.NewIdentifier("rolling_barrel"), FabricEntityTypeBuilder
                .createLiving().defaultAttributes(RollingBarrel::createBarrelAttributes).spawnGroup(SpawnGroup.MONSTER)
                .entityFactory((EntityType.EntityFactory<RollingBarrel>) RollingBarrel::new)
                .dimensions(EntityDimensions.fixed(1, 1)).fireImmune().build());

        SANDSTORM = Registry.register(Registry.ENTITY_TYPE, Utils.NewIdentifier("sandstorm"), FabricEntityTypeBuilder
                .create(SpawnGroup.MISC, Sandstorm::new).dimensions(EntityDimensions.fixed(1, 1)).fireImmune().build());

        // f*cking Java: can't infer Type T, needs extra line
        final FabricEntityTypeBuilder<Tornado> tornadoBuilder = (FabricEntityTypeBuilder.create(SpawnGroup.AMBIENT,
                Tornado::new));
        TORNADO = Registry.register(Registry.ENTITY_TYPE, Utils.NewIdentifier("tornado"),
                tornadoBuilder.dimensions(Tornado.dimensions).fireImmune().build());
    }

    private static <T extends CustomItem> T registerCustomItem(T item) {
        return registerItem(item.identifier, item);
    }

    private static <T extends Item> T registerItem(String id, T item) {
        return Registry.register(Registry.ITEM, Utils.NewIdentifier(id), item);
    }

    private static <T extends Block> T registerBlock(String id, T block) {
        return Registry.register(Registry.BLOCK, Utils.NewIdentifier(id), block);
    }

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        /*
         * We set up and register our structures here.
         * You should always register your stuff to prevent mod compatibility issue down the line.
         */
        DSStructures.registerStructures();

        /*
         * This is the API you will use to add anything to any biome.
         * This includes spawns, changing the biome's looks, messing with its surfacebuilders,
         * adding carvers, spawning new features... etc
         *
         * Make sure you give this an identifier to make it clear later what mod did a change and why.
         * It'll help people look to see if your mod was removing something from biomes.
         * The biome modifier identifier might also be used by modpacks to disable mod's modifiers too for customization.
         */
        BiomeModifications.create(Utils.NewIdentifier("run_down_house_addition"))
                .add(// Describes what we are doing. Since we are adding a structure, we choose ADDITIONS.
                        ModificationPhase.ADDITIONS,
                        // Add our structure to all biomes including other modded biomes.
                        // You can filter to certain biomes based on stuff like temperature, scale, precipitation, mod id.
                        BiomeSelectors.all(),
                        // context is basically the biome itself. This is where you do the changes to the biome.
                        // Here, we will add our ConfiguredStructureFeature to the biome.
                        context -> context.getGenerationSettings().addBuiltInStructure(DSStructures.CONFIGURED_RUN_DOWN_HOUSE));
        BiomeModifications.create(Utils.NewIdentifier("kong_level")).add(ModificationPhase.ADDITIONS, BiomeSelectors.categories(Biome.Category.JUNGLE),
                context -> context.getGenerationSettings().addBuiltInStructure(DSStructures.CONFIGURED_KONG_LEVEL));
    }
}
