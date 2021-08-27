package org.gara.desertstorm.items;

import net.minecraft.world.item.ItemStack;

public class SandstarItem extends CustomItem {

    public SandstarItem(Properties properties) {
        super("sandstar", properties);
    }

    @Override
    // make shiny
    public boolean isFoil(ItemStack itemStack) {
        return true;
    }
}