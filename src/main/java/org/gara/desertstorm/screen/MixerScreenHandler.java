package org.gara.desertstorm.screen;

import org.gara.desertstorm.DesertStorm;
import org.gara.desertstorm.item.cocktails.CocktailRecipeRegistry;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;

public class MixerScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;

    // This constructor gets called on the client when the server wants it to open
    // the screenHandler.
    // The client will call the other constructor with an empty Inventory and the
    // screenHandler will automatically sync this empty inventory with the inventory
    // on the server.
    public MixerScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(6), new ArrayPropertyDelegate(2));
    }

    // This constructor gets called from the BlockEntity on the server without
    // calling the other constructor first, the server knows the inventory of the
    // container and can therefore directly provide it as an argument. This
    // inventory will
    // then be synced to the client.
    public MixerScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory,
            PropertyDelegate propertyDelegate) {
        super(DesertStorm.MIXER_SCREEN_HANDLER, syncId);
        checkSize(inventory, 6);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        // some inventories do custom logic when a player opens it.
        inventory.onOpen(playerInventory.player);

        // This will place the slot in the correct locations for a 3x2 Grid. The slots
        // exist on both server and client!
        // This will not render the background of the slots however, this is the
        // Screen's job
        int y;
        int x;
        // Our inventory (Input + Output Slots)
        for (y = 0; y < 2; ++y) {
            for (x = 0; x < 3; ++x) {
                this.addSlot(new Slot(inventory, x + y * 3, 56 + x * 24, 13 + y * 48));
            }
        }

        // The player's inventory
        for (y = 0; y < 3; ++y) {
            for (x = 0; x < 9; ++x) {
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }
        // The player's hotbar
        for (y = 0; y < 9; ++y) {
            this.addSlot(new Slot(playerInventory, y, 8 + y * 18, 142));
        }
    }

    // Shift + Player Inv Slot
    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    public int getProgress() {
        return this.propertyDelegate.get(0);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    static class CocktailSlot extends Slot {
        public CocktailSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        public boolean canInsert(ItemStack stack) {
            return stack.isOf(Items.GLASS_BOTTLE);
        }

        public int getMaxItemCount() {
            return 1;
        }

        public void onTakeItem(PlayerEntity player, ItemStack stack) {
            Potion cocktail = PotionUtil.getPotion(stack);
            if (player instanceof ServerPlayerEntity) {
                Criteria.BREWED_POTION.trigger((ServerPlayerEntity) player, cocktail);
            }

            super.onTakeItem(player, stack);
        }
    }

    static class IngredientSlot extends Slot {
        public IngredientSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        public boolean canInsert(ItemStack stack) {
            return CocktailRecipeRegistry.isValidIngredient(stack);
        }

        public int getMaxItemCount() {
            return 64;
        }
    }
}
