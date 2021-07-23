package org.gara.desertstorm;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.potion.*;
import net.minecraft.block.Material;

public class DesertStorm implements ModInitializer {

	// Items
	public static final ToolItem SANDBLASTER_ITEM = new SandblasterItem(SandblasterMaterial.INSTANCE, 1, -2.8F,
			new FabricItemSettings().group(ItemGroup.COMBAT));

	public static final SandstarItem SANDSTAR_ITEM = new SandstarItem(
			new FabricItemSettings().group(ItemGroup.MISC).fireproof().rarity(Rarity.UNCOMMON).maxCount(64));

	// Blocks
	public static final LightningTrapBlock LIGHTNING_TRAP_BLOCK = new LightningTrapBlock(
			FabricBlockSettings.of(Material.STONE).hardness(4.0f));
	public static final BlockItem LIGHTNING_TRAP_ITEM = new BlockItem(LIGHTNING_TRAP_BLOCK, new Item.Settings().group(ItemGroup.COMBAT));


	public static final ItemGroup GENERAL_ITEM_GROUP = FabricItemGroupBuilder.create(Utils.NewIdentifier("items"))
			.icon(() -> new ItemStack(Items.SAND)).appendItems(stacks -> {
				stacks.add(new ItemStack(SANDBLASTER_ITEM));
				stacks.add(new ItemStack(SANDSTAR_ITEM));
				stacks.add(new ItemStack(LIGHTNING_TRAP_BLOCK));
				stacks.add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER));
			}).build();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		// Items
		Registry.register(Registry.ITEM, Utils.NewIdentifier("sandblaster"), SANDBLASTER_ITEM);
		Registry.register(Registry.ITEM, Utils.NewIdentifier("sandstar"), SANDSTAR_ITEM);

		// Blocks
		Registry.register(Registry.BLOCK, Utils.NewIdentifier("lightning_trap"), LIGHTNING_TRAP_BLOCK);
		Registry.register(Registry.ITEM, Utils.NewIdentifier("lightning_trap"), LIGHTNING_TRAP_ITEM);
	}
}
