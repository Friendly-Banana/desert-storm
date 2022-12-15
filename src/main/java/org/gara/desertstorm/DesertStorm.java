package org.gara.desertstorm;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.item.*;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;
import org.gara.desertstorm.block.DSBlocks;
import org.gara.desertstorm.entity.DSEntities;
import org.gara.desertstorm.item.*;
import software.bernie.geckolib3.GeckoLib;

import java.util.ArrayList;
import java.util.List;


public class DesertStorm implements ModInitializer {
	public static final ItemGroup ITEM_TAB;
	// gamerules
	public static final GameRules.Key<GameRules.BooleanRule> DROP_PLAYER_HEADS = GameRuleRegistry.register("dropPlayerHeads", GameRules.Category.DROPS, GameRuleFactory.createBooleanRule(true));
	public static final GameRules.Key<GameRules.BooleanRule> FLYING_PIGS = GameRuleRegistry.register("flyingPigs", GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(true));
	public static final GameRules.Key<GameRules.IntRule> EVOKER_RABBIT_CHANCE = GameRuleRegistry.register("evokerRabbitChance", GameRules.Category.MOBS, GameRuleFactory.createIntRule(30, 0, 100));
	public static final GameRules.Key<GameRules.IntRule> SANDBLASTER_COOLDOWN = GameRuleRegistry.register("sandblasterCooldown", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(10, 0, 100));
	// items
	private static final List<Item> items = new ArrayList<>(List.of(Items.BARRIER, Items.DEBUG_STICK, Items.COMMAND_BLOCK, Items.COMMAND_BLOCK_MINECART, Items.STRUCTURE_BLOCK, Items.JIGSAW));
	public static final Item SANDBLASTER_ITEM = registerItem("sandblaster", new SandblasterItem(new FabricItemSettings().group(ItemGroup.COMBAT)));
	public static final Item TORNADO_CLEANER_ITEM = registerItem("tornado_cleaner", new HammerItem(new FabricItemSettings().group(ItemGroup.COMBAT)));
	public static final Item BARREL_HAMMER_ITEM = registerItem("barrel_hammer", new HammerItem(new FabricItemSettings().group(ItemGroup.COMBAT)));
	public static final SandstarItem SANDSTAR_ITEM = registerCustomItem(new SandstarItem(new FabricItemSettings().group(ItemGroup.MISC).fireproof().rarity(Rarity.UNCOMMON)));
	public static final BatteryItem BATTERY_ITEM = registerCustomItem(new BatteryItem(new FabricItemSettings().group(ItemGroup.TOOLS)));
	public static final BananaItem BANANA_ITEM = registerCustomItem(new BananaItem(new FabricItemSettings().group(ItemGroup.FOOD).food(BananaItem.FOOD_PROPERTIES)));
	public static final BlockItem NETHER_PORTAL_ITEM = registerItem("nether_portal", new BlockItem(Blocks.NETHER_PORTAL, new FabricItemSettings()));
	public static final BlockItem END_PORTAL_ITEM = registerItem("end_portal", new BlockItem(Blocks.END_PORTAL, new FabricItemSettings()));
	public static final BlockItem END_GATEWAY_ITEM = registerItem("end_gateway", new BlockItem(Blocks.END_GATEWAY, new FabricItemSettings()));
	public static final BlockItem AETHER_PORTAL_ITEM = registerItem("aether_portal", new BlockItem(DSBlocks.AETHER_PORTAL_BLOCK, new FabricItemSettings()));
	public static final BlockItem COCONUT_ITEM = registerItem("coconut", new BlockItem(DSBlocks.COCONUT_BLOCK, new FabricItemSettings()));
	public static final BlockItem LIGHTNING_TRAP_ITEM = registerItem("lightning_trap", new BlockItem(DSBlocks.LIGHTNING_TRAP_BLOCK, new FabricItemSettings().group(ItemGroup.COMBAT)));
	// spawn eggs
	public static final SpawnEggItem MONKEY_SPAWN_EGG = registerItem("monkey_spawn_egg", new SpawnEggItem(DSEntities.MONKEY, 0x9a3c00, 0, new FabricItemSettings()));
	public static final SpawnEggItem MONKEY_KING_SPAWN_EGG = registerItem("monkey_king_spawn_egg", new SpawnEggItem(DSEntities.MONKEY_KING, 0x5a1c00, 0, new FabricItemSettings()));
	public static final SpawnEggItem BARREL_SPAWN_EGG = registerItem("barrel_spawn_egg", new SpawnEggItem(DSEntities.ROLLING_BARREL, 0xfc0000, 0, new FabricItemSettings()));
	public static final SpawnEggItem SANDWORM_SPAWN_EGG = registerItem("sandworm_spawn_egg", new SpawnEggItem(DSEntities.SANDWORM, 0xEDEBCB, 0, new FabricItemSettings()));
	public static final SpawnEggItem SAND_WITHER_SPAWN_EGG = registerItem("sand_wither_spawn_egg", new SpawnEggItem(DSEntities.SAND_WITHER, 0xffe900, 0, new FabricItemSettings()));
	public static final SpawnEggItem ILLUSIONER_SPAWN_EGG = registerItem("illusioner_spawn_egg", new SpawnEggItem(EntityType.ILLUSIONER, 0x125A96, 0, new FabricItemSettings()));
	// needed for banana drops
	private static final Identifier LARGE_FERN_LOOT_TABLE_ID = Blocks.LARGE_FERN.getLootTableId();

	static {
		// after registering so every item is in the list
		ITEM_TAB = FabricItemGroupBuilder.create(Utils.NewIdentifier("item_tab")).icon(Items.SAND::getDefaultStack).appendItems(itemStacks -> itemStacks.addAll(items.stream().map(Item::getDefaultStack).toList())).build();
	}

	private static <T extends CustomItem> T registerCustomItem(T item) {
		return registerItem(item.identifier, item);
	}

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
		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, original, source) -> {
			if (source.isBuiltin() && LARGE_FERN_LOOT_TABLE_ID.equals(id)) {
				LootPool.Builder poolBuilder = LootPool.builder().rolls(UniformLootNumberProvider.create(0, 3)).with(ItemEntry.builder(BANANA_ITEM));
				original.pool(poolBuilder);
			}
		});

		// Aether Portal
		CustomPortalBuilder.beginPortal().lightWithWater().frameBlock(Blocks.GLOWSTONE).destDimID(new Identifier("the_nether")).returnDim(new Identifier("the_end"), false).customPortalBlock(DSBlocks.AETHER_PORTAL_BLOCK).tintColor(18, 90, 150).registerPortal();

		GeckoLib.initialize();
	}
}
