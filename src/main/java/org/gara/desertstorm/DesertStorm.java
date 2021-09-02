package org.gara.desertstorm;

import org.gara.desertstorm.blocks.*;
import org.gara.desertstorm.client.MixerScreenHandler;
import org.gara.desertstorm.entities.Monkey;
import org.gara.desertstorm.entities.SandWither;
import org.gara.desertstorm.entities.Sandstorm;
import org.gara.desertstorm.entities.Tornado;
import org.gara.desertstorm.items.*;
import org.gara.desertstorm.items.cocktails.CocktailItem;
import org.gara.desertstorm.items.cocktails.CocktailRecipeRegistry;
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
import net.minecraft.block.Block;
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
        public static final EntityType<Monkey> MONKEY;

        public static final SpawnEggItem MONKEY_SPAWN_EGG;

        public static final EntityType<SandWither> SAND_WITHER;

        public static final SpawnEggItem SAND_WITHER_SPAWN_EGG;

        public static final EntityType<Sandstorm> SANDSTORM;

        public static final EntityType<Tornado> TORNADO;

        public static final ItemGroup ITEM_TAB;

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
                CocktailRecipeRegistry.registerDefaults();

                FabricDefaultAttributeRegistry.register(SAND_WITHER, SandWither.createWitherAttributes());
                FabricDefaultAttributeRegistry.register(MONKEY, Monkey.createMonkeyAttributes());
        }

        static {
                MIXER_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(Utils.NewIdentifier("mixer"),
                                MixerScreenHandler::new);

                BATTERY_ITEM = registerCustomItem(new BatteryItem(new FabricItemSettings().group(ItemGroup.MISC)));
                BANANA_ITEM = registerCustomItem(new BananaItem(
                                new FabricItemSettings().group(ItemGroup.MISC).food(BananaItem.FOOD_PROPERTIES)));
                SANDSTAR_ITEM = registerCustomItem(new SandstarItem(
                                new FabricItemSettings().group(ItemGroup.MISC).fireproof().rarity(Rarity.UNCOMMON)));
                SANDBLASTER_ITEM = registerItem("sandblaster",
                                new SandblasterItem(new FabricItemSettings().group(ItemGroup.COMBAT)));

                COCKTAIL = registerItem("cocktail",
                                new CocktailItem("cocktail", new FabricItemSettings().group(ItemGroup.BREWING)));
                COCONUT_BLOCK = registerBlock("coconut", new CoconutBlock(
                                FabricBlockSettings.of(Material.WOOD).strength(2.0f).breakByTool(FabricToolTags.AXES)));
                COCONUT_ITEM = registerItem("coconut",
                                new BlockItem(COCONUT_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS)));

                MIXER_BLOCK = Registry.register(Registry.BLOCK, Utils.NewIdentifier("mixer"),
                                new MixerBlock(FabricBlockSettings.of(Material.METAL)));
                MIXER_ITEM = registerItem("mixer",
                                new BlockItem(MIXER_BLOCK, new FabricItemSettings().group(ItemGroup.BREWING)));
                MIXER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "desertstorm:mixer_block_entity",
                                FabricBlockEntityTypeBuilder.create(MixerBlockEntity::new, MIXER_BLOCK).build());

                LIGHTNING_TRAP_BLOCK = registerBlock("lightning_trap", new LightningTrapBlock(FabricBlockSettings
                                .of(Material.STONE).strength(4.0f).breakByTool(FabricToolTags.PICKAXES)));
                LIGHTNING_TRAP_ITEM = registerItem("lightning_trap",
                                new BlockItem(LIGHTNING_TRAP_BLOCK, new FabricItemSettings().group(ItemGroup.COMBAT)));
                TRAP_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                                "desertstorm:lightning_trap_block_entity", FabricBlockEntityTypeBuilder
                                                .create(LightningTrapBlockEntity::new, LIGHTNING_TRAP_BLOCK).build());

                MONKEY = Registry.register(Registry.ENTITY_TYPE, Utils.NewIdentifier("monkey"),
                                FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, Monkey::new)
                                                .dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build());
                MONKEY_SPAWN_EGG = new SpawnEggItem(MONKEY, 10107904, 0,
                                new FabricItemSettings().group(ItemGroup.MISC));

                SAND_WITHER = Registry.register(Registry.ENTITY_TYPE, Utils.NewIdentifier("sand_wither"),
                                FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, SandWither::new)
                                                .dimensions(EntityDimensions.fixed(1f, 3.5f)).build());
                // #FFE900, #000000 in decimal
                SAND_WITHER_SPAWN_EGG = registerItem("sand_wither_spawn_egg", new SpawnEggItem(SAND_WITHER, 16771328, 0,
                                new FabricItemSettings().group(ItemGroup.MISC)));
                SANDSTORM = Registry.register(Registry.ENTITY_TYPE, Utils.NewIdentifier("sandstorm"),
                                FabricEntityTypeBuilder.create(SpawnGroup.MISC, Sandstorm::new)
                                                .dimensions(EntityDimensions.fixed(1, 1)).fireImmune().build());

                // fcking Java: can't infer Type T, needs extra line
                final FabricEntityTypeBuilder<Tornado> tornadoBuilder = (FabricEntityTypeBuilder
                                .create(SpawnGroup.AMBIENT, Tornado::new));
                TORNADO = Registry.register(Registry.ENTITY_TYPE, Utils.NewIdentifier("tornado"),
                                tornadoBuilder.dimensions(Tornado.dimensions).fireImmune().build());

                ITEM_TAB = FabricItemGroupBuilder.create(Utils.NewIdentifier("items"))
                                .icon(() -> new ItemStack(Items.SAND)).appendItems(stacks -> {
                                        stacks.add(new ItemStack(SANDBLASTER_ITEM));
                                        stacks.add(new ItemStack(SANDSTAR_ITEM));
                                        stacks.add(new ItemStack(BATTERY_ITEM));
                                        stacks.add(new ItemStack(BANANA_ITEM));
                                        stacks.add(new ItemStack(COCONUT_BLOCK));
                                        stacks.add(new ItemStack(LIGHTNING_TRAP_BLOCK));
                                        stacks.add(new ItemStack(SAND_WITHER_SPAWN_EGG));
                                        stacks.add(new ItemStack(COCKTAIL));
                                        stacks.add(PotionUtil.setPotion(new ItemStack(Items.POTION),
                                                        Cocktails.HOT_COCOA));
                                        stacks.add(ItemStack.EMPTY);
                                        stacks.add(new ItemStack(Items.SPAWNER));
                                        stacks.add(new ItemStack(Items.BARRIER));
                                        stacks.add(new ItemStack(Items.DEBUG_STICK));
                                        stacks.add(new ItemStack(Items.COMMAND_BLOCK));
                                        stacks.add(new ItemStack(Items.STRUCTURE_BLOCK));
                                }).build();
        }
}
