package org.gara.desertstorm;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.material.Material;

public class DesertStorm implements ModInitializer {

	// Items
	public static final Item SANDBLASTER_ITEM = new SandblasterItem(new FabricItemSettings().group(CreativeModeTab.TAB_COMBAT));

	public static final SandstarItem SANDSTAR_ITEM = new SandstarItem(
			new FabricItemSettings().group(CreativeModeTab.TAB_MISC).fireproof().rarity(Rarity.UNCOMMON));

	public static final BatteryItem BATTERY_ITEM = new BatteryItem(new FabricItemSettings().group(CreativeModeTab.TAB_MISC));

	// Blocks
	public static final LightningTrapBlock LIGHTNING_TRAP_BLOCK = new LightningTrapBlock(
			FabricBlockSettings.of(Material.STONE).hardness(4.0f));
	public static final BlockItem LIGHTNING_TRAP_ITEM = new BlockItem(LIGHTNING_TRAP_BLOCK,
			new FabricItemSettings().group(CreativeModeTab.TAB_COMBAT));

	public static final CreativeModeTab ITEM_TAB = FabricItemGroupBuilder.create(Utils.NewIdentifier("items"))
			.icon(() -> new ItemStack(Items.SAND)).appendItems(stacks -> {
				stacks.add(new ItemStack(SANDBLASTER_ITEM));
				stacks.add(new ItemStack(SANDSTAR_ITEM));
				stacks.add(new ItemStack(BATTERY_ITEM));
				stacks.add(new ItemStack(LIGHTNING_TRAP_BLOCK));
				stacks.add(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER));
			}).build();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		// Items
		Registry.register(Registry.ITEM, Utils.NewIdentifier("sandblaster"), SANDBLASTER_ITEM);
		Registry.register(Registry.ITEM, Utils.NewIdentifier("sandstar"), SANDSTAR_ITEM);
		Registry.register(Registry.ITEM, Utils.NewIdentifier("battery"), BATTERY_ITEM);

		// Blocks
		Registry.register(Registry.BLOCK, Utils.NewIdentifier("lightning_trap"), LIGHTNING_TRAP_BLOCK);
		Registry.register(Registry.ITEM, Utils.NewIdentifier("lightning_trap"), LIGHTNING_TRAP_ITEM);
	}
}
