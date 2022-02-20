package org.gara.desertstorm;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.item.*;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;
import org.gara.desertstorm.block.DSBlocks;
import org.gara.desertstorm.entity.DSEntities;
import org.gara.desertstorm.item.*;
import org.gara.desertstorm.structures.DSStructures;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class DesertStorm implements ModInitializer {
    // all custom items
    private static final List<Item> items = new ArrayList<>(List.of(Items.BARRIER, Items.DEBUG_STICK, Items.COMMAND_BLOCK, Items.COMMAND_BLOCK_MINECART, Items.STRUCTURE_BLOCK, Items.JIGSAW));
    public static final ItemGroup ITEM_TAB;
    public static final Item SANDBLASTER_ITEM = registerItem("sandblaster",
            new SandblasterItem(new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item TORNADO_CLEANER_ITEM = registerItem("tornado_cleaner",
            new HammerItem(new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item BARREL_HAMMER_ITEM = registerItem("barrel_hammer",
            new HammerItem(new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final SandstarItem SANDSTAR_ITEM = registerCustomItem(
            new SandstarItem(new FabricItemSettings().group(ItemGroup.MISC).fireproof().rarity(Rarity.UNCOMMON)));
    public static final BatteryItem BATTERY_ITEM = registerCustomItem(
            new BatteryItem(new FabricItemSettings().group(ItemGroup.TOOLS)));
    public static final BananaItem BANANA_ITEM = registerCustomItem(
            new BananaItem(new FabricItemSettings().group(ItemGroup.FOOD).food(BananaItem.FOOD_PROPERTIES)));
    public static final BlockItem NETHER_PORTAL_ITEM = registerItem("nether_portal",
            new BlockItem(Blocks.NETHER_PORTAL, new FabricItemSettings()));
    public static final BlockItem END_PORTAL_ITEM = registerItem("end_portal",
            new BlockItem(Blocks.END_PORTAL, new FabricItemSettings()));
    public static final BlockItem END_GATEWAY_ITEM = registerItem("end_gateway",
            new BlockItem(Blocks.END_GATEWAY, new FabricItemSettings()));
    public static final BlockItem AETHER_PORTAL_ITEM = registerItem("aether_portal",
            new BlockItem(DSBlocks.AETHER_PORTAL_BLOCK, new FabricItemSettings()));
    public static final BlockItem COCONUT_ITEM = registerItem("coconut",
            new BlockItem(DSBlocks.COCONUT_BLOCK, new FabricItemSettings()));
    public static final BlockItem LIGHTNING_TRAP_ITEM = registerItem("lightning_trap",
            new BlockItem(DSBlocks.LIGHTNING_TRAP_BLOCK, new FabricItemSettings().group(ItemGroup.COMBAT)));
    // Spawneggs
    public static final SpawnEggItem MONKEY_SPAWN_EGG = registerItem("monkey_spawn_egg",
            new SpawnEggItem(DSEntities.MONKEY, 0x9a3c00, 0,
                    new FabricItemSettings()));
    public static final SpawnEggItem MONKEY_KING_SPAWN_EGG = registerItem("monkey_king_spawn_egg",
            new SpawnEggItem(DSEntities.MONKEY_KING, 0x5a1c00, 0,
                    new FabricItemSettings()));
    public static final SpawnEggItem SAND_WITHER_SPAWN_EGG = registerItem("sand_wither_spawn_egg",
            new SpawnEggItem(DSEntities.SAND_WITHER, 0xffe900, 0, new FabricItemSettings()));
    public static final SpawnEggItem ILLUSIONER_SPAWN_EGG = registerItem("illusioner_spawn_egg",
            new SpawnEggItem(EntityType.ILLUSIONER, 0x125A96, 0, new FabricItemSettings()));

    static {
        // do as last thing so every item is in the list
        ITEM_TAB = FabricItemGroupBuilder.create(Utils.NewIdentifier("item_tab")).icon(Items.SAND::getDefaultStack).appendItems(itemStacks -> itemStacks.addAll(items.stream().map(Item::getDefaultStack).collect(Collectors.toList()))).build();
    }

    private static <T extends CustomItem> T registerCustomItem(T item) {
        return registerItem(item.identifier, item);
    }

    // Gamerules
    public static final GameRules.Key<GameRules.BooleanRule> DROP_PLAYER_HEADS = GameRuleRegistry
            .register("dropPlayerHeads", GameRules.Category.DROPS, GameRuleFactory.createBooleanRule(true));
    public static final GameRules.Key<GameRules.BooleanRule> FLYING_PIGS = GameRuleRegistry.register("flyingPigs",
            GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(true));
    public static final GameRules.Key<GameRules.IntRule> EVOKER_RABBIT_CHANCE = GameRuleRegistry
            .register("evokerRabbitChance", GameRules.Category.MOBS, GameRuleFactory.createIntRule(30, 0, 100));
    // needed for banana drops
    private static final Identifier LARGE_FERN_LOOT_TABLE_ID = Blocks.LARGE_FERN.getLootTableId();

    private static <T extends Item> T registerItem(String id, T item) {
        T i = Registry.register(Registry.ITEM, Utils.NewIdentifier(id), item);
        items.add(i);
        return i;
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
                        .with(ItemEntry.builder(BANANA_ITEM));
                table.pool(poolBuilder);
            }
        });

        // We set up and register our structures here.
        DSStructures.addStructuresToBiomes();

        // Aether Portal
        CustomPortalBuilder.beginPortal()
                .lightWithWater()
                .frameBlock(Blocks.GLOWSTONE)
                .destDimID(new Identifier("the_nether"))
                .returnDim(new Identifier("the_end"), false)
                .customPortalBlock(DSBlocks.AETHER_PORTAL_BLOCK)
                .tintColor(18, 90, 150)
                .registerPortal();
    }


}
