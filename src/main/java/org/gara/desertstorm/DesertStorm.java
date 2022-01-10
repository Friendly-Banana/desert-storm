package org.gara.desertstorm;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.*;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import org.gara.desertstorm.block.AetherPortalBlock;
import org.gara.desertstorm.block.CoconutBlock;
import org.gara.desertstorm.block.LightningTrapBlock;
import org.gara.desertstorm.block.LightningTrapBlockEntity;
import org.gara.desertstorm.entity.*;
import org.gara.desertstorm.item.*;
import org.gara.desertstorm.structures.DSStructures;

import java.util.ArrayList;
import java.util.List;


public class DesertStorm implements ModInitializer {
    // all custom items
    private static final List<Item> items = new ArrayList<>();
    public static final ItemGroup ITEM_TAB = FabricItemGroupBuilder.create(Utils.NewIdentifier("item_tab")).icon(Items.SAND::getDefaultStack).build();
    // Blocks
    public static final AetherPortalBlock AETHER_PORTAL_BLOCK = registerBlock("aether_portal", new AetherPortalBlock());
    public static final CoconutBlock COCONUT_BLOCK = registerBlock("coconut", new CoconutBlock());
    public static final LightningTrapBlock LIGHTNING_TRAP_BLOCK = registerBlock("lightning_trap",
            new LightningTrapBlock());
    // Entities
    public static final EntityType<MonkeyEntity> MONKEY = Registry.register(Registry.ENTITY_TYPE,
            Utils.NewIdentifier("monkey"),
            FabricEntityTypeBuilder.createLiving().defaultAttributes(MonkeyEntity::createMonkeyAttributes)
                    .entityFactory(MonkeyEntity::new).dimensions(EntityDimensions.fixed(1.5f, 1.0f)).build());    // Entities
    public static final EntityType<MonkeyKingEntity> MONKEY_KING = Registry.register(Registry.ENTITY_TYPE,
            Utils.NewIdentifier("monkey_king"),
            FabricEntityTypeBuilder.createLiving().defaultAttributes(MonkeyKingEntity::createMonkeyKingAttributes)
                    .spawnGroup(SpawnGroup.MONSTER).entityFactory(MonkeyKingEntity::new).fireImmune()
                    .dimensions(EntityDimensions.fixed(2f, 1.5f)).build());
    public static final EntityType<SandWither> SAND_WITHER = Registry.register(Registry.ENTITY_TYPE,
            Utils.NewIdentifier("sand_wither"),
            FabricEntityTypeBuilder.createLiving().defaultAttributes(SandWither::createWitherAttributes)
                    .spawnGroup(SpawnGroup.CREATURE).entityFactory(SandWither::new)
                    .dimensions(EntityDimensions.fixed(1f, 3.5f)).build());
    public static final EntityType<RollingBarrel> ROLLING_BARREL = Registry.register(Registry.ENTITY_TYPE,
            Utils.NewIdentifier("rolling_barrel"), FabricEntityTypeBuilder
                    .createMob().defaultAttributes(RollingBarrel::createBarrelAttributes).spawnGroup(SpawnGroup.MONSTER)
                    .entityFactory((EntityType.EntityFactory<RollingBarrel>) RollingBarrel::new)
                    .dimensions(EntityDimensions.fixed(1.1f, 1)).fireImmune().build());
    public static final EntityType<Tornado> TORNADO = Registry.register(Registry.ENTITY_TYPE,
            Utils.NewIdentifier("tornado"), FabricEntityTypeBuilder
                    .createMob().entityFactory(Tornado::new).spawnGroup(SpawnGroup.AMBIENT)
                    .dimensions(Tornado.dimensions).fireImmune()
                    .defaultAttributes(Tornado::createTornadoAttributes).build());
    public static final EntityType<Sandstorm> SANDSTORM = Registry.register(Registry.ENTITY_TYPE,
            Utils.NewIdentifier("sandstorm"), FabricEntityTypeBuilder.create(SpawnGroup.MISC, Sandstorm::new).dimensions(Sandstorm.dimensions)
                    .fireImmune().build());
    // Gamerules
    public static final GameRules.Key<GameRules.BooleanRule> DROP_PLAYER_HEADS = GameRuleRegistry
            .register("dropPlayerHeads", GameRules.Category.DROPS, GameRuleFactory.createBooleanRule(true));
    public static final GameRules.Key<GameRules.BooleanRule> FLYING_PIGS = GameRuleRegistry.register("flyingPigs",
            GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(true));
    public static final GameRules.Key<GameRules.IntRule> EVOKER_RABBIT_CHANCE = GameRuleRegistry
            .register("evokerRabbitChance", GameRules.Category.MOBS, GameRuleFactory.createIntRule(1, 0, 100));
    // Items
    public static final Item SANDBLASTER_ITEM = registerItem("sandblaster",
            new SandblasterItem(new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item TORNADO_CLEANER_ITEM = registerItem("tornado_cleaner",
            new HammerItem(new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item BARREL_HAMMER_ITEM = registerItem("barrel_hammer",
            new HammerItem(new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final SandstarItem SANDSTAR_ITEM = registerCustomItem(
            new SandstarItem(new FabricItemSettings().group(ItemGroup.MISC).fireproof().rarity(Rarity.UNCOMMON)));
    public static final BatteryItem BATTERY_ITEM = registerCustomItem(
            new BatteryItem(new FabricItemSettings()));
    public static final BananaItem BANANA_ITEM = registerCustomItem(
            new BananaItem(new FabricItemSettings().group(ItemGroup.FOOD).food(BananaItem.FOOD_PROPERTIES)));
    public static final BlockItem NETHER_PORTAL_ITEM = registerItem("nether_portal",
            new BlockItem(Blocks.NETHER_PORTAL, new FabricItemSettings()));
    public static final BlockItem END_PORTAL_ITEM = registerItem("end_portal",
            new BlockItem(Blocks.END_PORTAL, new FabricItemSettings()));
    public static final BlockItem END_GATEWAY_ITEM = registerItem("end_gateway",
            new BlockItem(Blocks.END_GATEWAY, new FabricItemSettings()));
    public static final BlockItem AETHER_PORTAL_ITEM = registerItem("aether_portal",
            new BlockItem(AETHER_PORTAL_BLOCK, new FabricItemSettings()));
    public static final BlockItem COCONUT_ITEM = registerItem("coconut",
            new BlockItem(COCONUT_BLOCK, new FabricItemSettings()));

    public static final BlockItem LIGHTNING_TRAP_ITEM = registerItem("lightning_trap",
            new BlockItem(LIGHTNING_TRAP_BLOCK, new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final BlockEntityType<LightningTrapBlockEntity> TRAP_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, Utils.NewIdentifier("lightning_trap_block_entity"), FabricBlockEntityTypeBuilder.create(LightningTrapBlockEntity::new, LIGHTNING_TRAP_BLOCK).build());
    // Spawneggs
    public static final SpawnEggItem MONKEY_SPAWN_EGG = registerItem("monkey_spawn_egg",
            new SpawnEggItem(MONKEY, 0x9a3c00, 0,
                    new FabricItemSettings()));
    public static final SpawnEggItem MONKEY_KING_SPAWN_EGG = registerItem("monkey_king_spawn_egg",
            new SpawnEggItem(MONKEY_KING, 0x5a1c00, 0,
                    new FabricItemSettings()));
    public static final SpawnEggItem SAND_WITHER_SPAWN_EGG = registerItem("sand_wither_spawn_egg",
            new SpawnEggItem(SAND_WITHER, 0xffe900, 0, new FabricItemSettings()));
    public static final SpawnEggItem ILLUSIONER_SPAWN_EGG = registerItem("illusioner_spawn_egg",
            new SpawnEggItem(EntityType.ILLUSIONER, 0x125A96, 0, new FabricItemSettings()));
    // needed for banana drops
    private static final Identifier LARGE_FERN_LOOT_TABLE_ID = Blocks.LARGE_FERN.getLootTableId();

    static {
        items.addAll(List.of(Items.BARRIER, Items.DEBUG_STICK, Items.COMMAND_BLOCK, Items.COMMAND_BLOCK_MINECART, Items.STRUCTURE_BLOCK, Items.JIGSAW));
        ITEM_TAB.appendStacks(DefaultedList.copyOf(null, items.stream().map(Item::getDefaultStack).toArray(ItemStack[]::new)));
    }

    private static <T extends CustomItem> T registerCustomItem(T item) {
        return registerItem(item.identifier, item);
    }

    private static <T extends Item> T registerItem(String id, T item) {
        items.add(item);
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

        // drop bananas
        LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, table, setter) -> {
            if (LARGE_FERN_LOOT_TABLE_ID.equals(id)) {
                FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                        .rolls(UniformLootNumberProvider.create(0, 3))
                        .with(ItemEntry.builder(DesertStorm.BANANA_ITEM));
                table.pool(poolBuilder);
            }
        });

        /*
         * We set up and register our structures here.
         * You should always register your stuff to prevent mod compatibility issue down
         * the line.
         */
        DSStructures.registerStructures();

        /*
         * This is the API you will use to add anything to any biome.
         * This includes spawns, changing the biome's looks, messing with its
         * surfacebuilders,
         * adding carvers, spawning new features... etc
         *
         * Make sure you give this an identifier to make it clear later what mod did a
         * change and why.
         * It'll help people look to see if your mod was removing something from biomes.
         * The biome modifier identifier might also be used by modpacks to disable mod's
         * modifiers too for customization.
         */
        BiomeModifications.create(Utils.NewIdentifier("run_down_house_addition"))
                .add(// Describes what we are doing. Since we are adding a structure, we choose
                        // ADDITIONS.
                        ModificationPhase.ADDITIONS,
                        // Add our structure to all biomes including other modded biomes.
                        // You can filter to certain biomes based on stuff like temperature, scale,
                        // precipitation, mod id.
                        BiomeSelectors.includeByKey(BiomeKeys.BIRCH_FOREST, BiomeKeys.OLD_GROWTH_BIRCH_FOREST),
                        // context is basically the biome itself. This is where you do the changes to
                        // the biome.
                        // Here, we will add our ConfiguredStructureFeature to the biome.
                        context -> context.getGenerationSettings()
                                .addBuiltInStructure(DSStructures.CONFIGURED_RUN_DOWN_HOUSE));
        BiomeModifications.create(Utils.NewIdentifier("kong_level_addition")).add(ModificationPhase.ADDITIONS,
                BiomeSelectors.categories(Biome.Category.JUNGLE),
                context -> context.getGenerationSettings().addBuiltInStructure(DSStructures.CONFIGURED_KONG_LEVEL));

        // Aether Portal
        CustomPortalBuilder.beginPortal()
                .frameBlock(Blocks.GLOWSTONE)
                .lightWithWater()
                .destDimID(new Identifier("the_nether"))
                .returnDim(new Identifier("the_end"), false)
                .customPortalBlock(AETHER_PORTAL_BLOCK)
                .tintColor(18, 90, 150)
                .registerPortal();
    }


}
