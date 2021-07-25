package org.gara.desertstorm;

import net.minecraft.world.item.ItemStack;

public class SandstarItem extends CustomItem {

    public SandstarItem(Properties properties) {
        super("sandstar", properties);
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return true;
    }
}