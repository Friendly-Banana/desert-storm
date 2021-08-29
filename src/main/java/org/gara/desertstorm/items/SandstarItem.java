package org.gara.desertstorm.items;

import net.minecraft.item.ItemStack;

public class SandstarItem extends CustomItem {

    public SandstarItem(Settings properties) {
        super("sandstar", properties);
    }

    @Override
    // make shiny
    public boolean hasGlint(ItemStack itemStack) {
        return true;
    }
}