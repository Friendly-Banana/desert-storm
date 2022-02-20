package org.gara.desertstorm.block;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import org.gara.desertstorm.Utils;

public class DSBlocks {
    public static final CustomPortalBlock AETHER_PORTAL_BLOCK = registerBlock("aether_portal", new AetherPortalBlock());
    public static final CoconutBlock COCONUT_BLOCK = registerBlock("coconut", new CoconutBlock());
    public static final LightningTrapBlock LIGHTNING_TRAP_BLOCK = registerBlock("lightning_trap", new LightningTrapBlock());

    private static <T extends Block> T registerBlock(String id, T block) {
        return Registry.register(Registry.BLOCK, Utils.NewIdentifier(id), block);
    }

    public static final BlockEntityType<LightningTrapBlockEntity> TRAP_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, Utils.NewIdentifier("lightning_trap_block_entity"), FabricBlockEntityTypeBuilder.create(LightningTrapBlockEntity::new, LIGHTNING_TRAP_BLOCK).build());


}
