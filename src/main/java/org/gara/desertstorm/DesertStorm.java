package org.gara.desertstorm;

import org.gara.desertstorm.blocks.*;
import org.gara.desertstorm.client.MixerScreenHandler;
import org.gara.desertstorm.entities.SandWither;
import org.gara.desertstorm.entities.Sandstorm;
import org.gara.desertstorm.entities.Tornado;
import org.gara.desertstorm.items.BananaItem;
import org.gara.desertstorm.items.BatteryItem;
import org.gara.desertstorm.items.SandblasterItem;
import org.gara.desertstorm.items.SandstarItem;
import org.gara.desertstorm.items.cocktails.CocktailItem;
import org.gara.desertstorm.items.cocktails.Cocktails;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.*;
import net.minecraft.potion.PotionUtil;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class DesertStorm implements ModInitializer {
	public static final ScreenHandlerType<MixerScreenHandler> MIXER_SCREEN_HANDLER;
	
	// Items
	public static final SandblasterItem SANDBLASTER_ITEM;

	public static final SandstarItem SANDSTAR_ITEM;

	public static final BatteryItem BATTERY_ITEM;
	public static final BananaItem BANANA_ITEM;

	// Cocktails
	public static final CocktailItem COCKTAIL;

	// Blocks
	public static final CoconutBlock COCONUT_BLOCK;
	public static final BlockItem COCONUT_ITEM;

	public static final MixerBlock MIXER_BLOCK;
	public static final BlockItem MIXER_ITEM;
	public static BlockEntityType<MixerBlockEntity> MIXER_BLOCK_ENTITY;

	public static final LightningTrapBlock LIGHTNING_TRAP_BLOCK;
	public static final BlockItem LIGHTNING_TRAP_ITEM;
	public static BlockEntityType<LightningTrapBlockEntity> TRAP_BLOCK_ENTITY;

	// Entities
	public static final EntityType<SandWither> SAND_WITHER;

	public static final SpawnEggItem SAND_WITHER_SPAWN_EGG;

	public static final EntityType<Sandstorm> SANDSTORM;

	public static final EntityType<Tornado> TORNADO;

	public static final ItemGroup ITEM_TAB;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		// Items
		Registry.register(Registry.ITEM, Utils.NewIdentifier(COCKTAIL.identifier), COCKTAIL);
		Registry.register(Registry.ITEM, Utils.NewIdentifier(SANDBLASTER_ITEM.identifier), SANDBLASTER_ITEM);
		Registry.register(Registry.ITEM, Utils.NewIdentifier(SANDSTAR_ITEM.identifier), SANDSTAR_ITEM);
		Registry.register(Registry.ITEM, Utils.NewIdentifier(BATTERY_ITEM.identifier), BATTERY_ITEM);
		Registry.register(Registry.ITEM, Utils.NewIdentifier(BANANA_ITEM.identifier), BANANA_ITEM);
		Registry.register(Registry.ITEM, Utils.NewIdentifier("sand_wither_spawn_egg"), SAND_WITHER_SPAWN_EGG);

		// Blocks
		Registry.register(Registry.BLOCK, Utils.NewIdentifier("coconut"), COCONUT_BLOCK);
		Registry.register(Registry.ITEM, Utils.NewIdentifier("coconut"), COCONUT_ITEM);

		Registry.register(Registry.BLOCK, Utils.NewIdentifier("lightning_trap"), LIGHTNING_TRAP_BLOCK);
		Registry.register(Registry.ITEM, Utils.NewIdentifier("lightning_trap"), LIGHTNING_TRAP_ITEM);
		TRAP_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "desertstorm:lightning_trap_block_entity",
				FabricBlockEntityTypeBuilder.create(LightningTrapBlockEntity::new, LIGHTNING_TRAP_BLOCK).build());

		FabricDefaultAttributeRegistry.register(SAND_WITHER, SandWither.createWitherAttributes());
	}

	static {
		MIXER_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(Utils.NewIdentifier("mixer"), MixerScreenHandler::new);

		BATTERY_ITEM = new BatteryItem(new FabricItemSettings().group(ItemGroup.MISC));
		BANANA_ITEM = new BananaItem(new FabricItemSettings().group(ItemGroup.MISC).food(BananaItem.FOOD_PROPERTIES));
		SANDSTAR_ITEM = new SandstarItem(
				new FabricItemSettings().group(ItemGroup.MISC).fireproof().rarity(Rarity.UNCOMMON));
		SANDBLASTER_ITEM = new SandblasterItem(new FabricItemSettings().group(ItemGroup.COMBAT));

		COCKTAIL = new CocktailItem("cocktail", new FabricItemSettings().group(ItemGroup.BREWING));
		COCONUT_BLOCK = new CoconutBlock(
				FabricBlockSettings.of(Material.WOOD).strength(2.0f).breakByTool(FabricToolTags.AXES));
		COCONUT_ITEM = new BlockItem(COCONUT_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS));

		MIXER_BLOCK = new MixerBlock(FabricBlockSettings.of(Material.METAL));
		MIXER_ITEM = new BlockItem(MIXER_BLOCK, new FabricItemSettings().group(ItemGroup.BREWING));

		LIGHTNING_TRAP_BLOCK = new LightningTrapBlock(
				FabricBlockSettings.of(Material.STONE).strength(4.0f).breakByTool(FabricToolTags.PICKAXES));
		LIGHTNING_TRAP_ITEM = new BlockItem(LIGHTNING_TRAP_BLOCK, new FabricItemSettings().group(ItemGroup.COMBAT));

		SAND_WITHER = Registry.register(Registry.ENTITY_TYPE, Utils.NewIdentifier("sand_wither"),
				FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, SandWither::new)
						.dimensions(EntityDimensions.fixed(1f, 3.5f)).build());

		// #FFE900, #000000 in decomal
		SAND_WITHER_SPAWN_EGG = new SpawnEggItem(SAND_WITHER, 16771328, 0,
				new FabricItemSettings().group(ItemGroup.MISC));
		SANDSTORM = Registry.register(Registry.ENTITY_TYPE, Utils.NewIdentifier("sandstorm"), FabricEntityTypeBuilder
				.create(SpawnGroup.MISC, Sandstorm::new).dimensions(EntityDimensions.fixed(1, 1)).fireImmune().build());

		// fcking Java: can't infer Type T, needs extra line
		FabricEntityTypeBuilder<Tornado> tornadoBuilder = (FabricEntityTypeBuilder.create(SpawnGroup.MISC,
				Tornado::new));
		TORNADO = Registry.register(Registry.ENTITY_TYPE, Utils.NewIdentifier("tornado"),
				tornadoBuilder.dimensions(Tornado.dimensions).fireImmune().build());

		ITEM_TAB = FabricItemGroupBuilder.create(Utils.NewIdentifier("items")).icon(() -> new ItemStack(Items.SAND))
				.appendItems(stacks -> {
					stacks.add(new ItemStack(SANDBLASTER_ITEM));
					stacks.add(new ItemStack(SANDSTAR_ITEM));
					stacks.add(new ItemStack(BATTERY_ITEM));
					stacks.add(new ItemStack(BANANA_ITEM));
					stacks.add(new ItemStack(COCONUT_BLOCK));
					stacks.add(new ItemStack(LIGHTNING_TRAP_BLOCK));
					stacks.add(new ItemStack(SAND_WITHER_SPAWN_EGG));
					stacks.add(new ItemStack(COCKTAIL));
					stacks.add(PotionUtil.setPotion(new ItemStack(Items.POTION), Cocktails.HOT_COCOA));
					stacks.add(ItemStack.EMPTY);
					stacks.add(new ItemStack(Items.SPAWNER));
					stacks.add(new ItemStack(Items.BARRIER));
					stacks.add(new ItemStack(Items.DEBUG_STICK));
					stacks.add(new ItemStack(Items.COMMAND_BLOCK));
					stacks.add(new ItemStack(Items.STRUCTURE_BLOCK));
				}).build();
	}
}
