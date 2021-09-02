package org.gara.desertstorm.blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.gara.desertstorm.DesertStorm;
import org.gara.desertstorm.Utils;
import org.gara.desertstorm.client.MixerScreenHandler;
import org.gara.desertstorm.items.cocktails.CocktailRecipeRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

public class MixerBlockEntity extends LockableContainerBlockEntity implements SidedInventory {
    private static final int[] INPUT_SLOTS = { 0, 1, 2 };
    private static final int[] OUTPUT_SLOTS = new int[] { 3, 4, 5 };
    private Item cocktailMixing;
    /** Remaining Time till {@link #cocktailMixing} is finished */
    int mixTime;
    private boolean[] slotsEmptyLastTick;
    private final DefaultedList<ItemStack> inventory;
    protected final PropertyDelegate propertyDelegate;

    public MixerBlockEntity(BlockPos pos, BlockState state) {
        super(DesertStorm.MIXER_BLOCK_ENTITY, pos, state);
        this.inventory = DefaultedList.ofSize(6, ItemStack.EMPTY);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                // switch?
                return MixerBlockEntity.this.mixTime;
            }

            public void set(int index, int value) {
                MixerBlockEntity.this.mixTime = value;
            }

            public int size() {
                return 1;
            }
        };
    }

    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    // TODO tick method
    public static void tick(World world, BlockPos pos, BlockState state, MixerBlockEntity blockEntity) {
        boolean weCanCraft = canCraft(blockEntity.inventory);
        boolean stillMixing = blockEntity.mixTime > 0;
        ItemStack itemStack2 = (ItemStack) blockEntity.inventory.get(3);
        if (stillMixing) {
            --blockEntity.mixTime;
            boolean finished = blockEntity.mixTime == 0;
            if (finished && weCanCraft) {
                craft(world, pos, blockEntity.inventory);
                markDirty(world, pos, state);
            } else if (!weCanCraft || !itemStack2.isOf(blockEntity.cocktailMixing)) {
                blockEntity.mixTime = 0;
                markDirty(world, pos, state);
            }
        } else if (weCanCraft) {
            blockEntity.mixTime = 300;
            blockEntity.cocktailMixing = itemStack2.getItem();
            markDirty(world, pos, state);
        }

        boolean[] bls = blockEntity.getGlassBottles();
        if (!Arrays.equals(bls, blockEntity.slotsEmptyLastTick)) {
            blockEntity.slotsEmptyLastTick = bls;
            BlockState blockState = state;
            if (!(state.getBlock() instanceof MixerBlock)) {
                return;
            }

            for (int i = 0; i < MixerBlock.BOTTLE_PROPERTIES.length; ++i) {
                blockState = (BlockState) blockState.with(MixerBlock.BOTTLE_PROPERTIES[i], bls[i]);
            }

            world.setBlockState(pos, blockState, Block.NOTIFY_LISTENERS);
        }
    }

    /**
     * Cleans slots up
     * 
     * @param slots All slots of an inventory
     * @return only Slots containing Items
     */
    private static List<ItemStack> onlyFilledSlots(DefaultedList<ItemStack> slots) {
        List<ItemStack> items = new ArrayList<ItemStack>(3);
        for (ItemStack itemStack : slots) {
            if (!itemStack.isEmpty()) {
                items.add(itemStack);
            }
        }
        return items;
    }

    private static boolean canCraft(DefaultedList<ItemStack> slots) {
        if (slots.isEmpty())
            return false;
        else {
            return CocktailRecipeRegistry.hasRecipe(onlyFilledSlots(slots));
        }
    }

    private static void craft(World world, BlockPos pos, DefaultedList<ItemStack> slots) {
        ItemStack result = CocktailRecipeRegistry.craft(onlyFilledSlots(slots));
        for (int i : OUTPUT_SLOTS) {
            slots.set(i, result);
        }

        for (int i : INPUT_SLOTS) {
            ItemStack itemStack = slots.get(i);
            itemStack.decrement(1);
            // Bottles can remain
            if (itemStack.getItem().hasRecipeRemainder()) {
                ItemStack itemStack2 = new ItemStack(itemStack.getItem().getRecipeRemainder());
                // Ingredient used up
                if (itemStack.isEmpty()) {
                    itemStack = itemStack2;
                    // drop Item
                } else {
                    ItemScatterer.spawn(world, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(),
                            itemStack2);
                }
            }
        }

        world.syncWorldEvent(WorldEvents.BREWING_STAND_BREWS, pos, 0);
    }

    private boolean[] getGlassBottles() {
        boolean[] bottles = new boolean[3];
        for (int i = 0; i < 3; i++) {
            bottles[i] = !this.inventory.get(OUTPUT_SLOTS[i]).isEmpty();
        }

        return bottles;
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        // We provide *this* to the screenHandler as our class Implements Inventory
        // Only the Server has the Inventory at the start, this will be synced to the
        // client in the ScreenHandler
        return new MixerScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
        return nbt;
    }

    // SidedInventory
    @Override
    public boolean isValid(int slot, ItemStack stack) {
        if (ArrayUtils.contains(INPUT_SLOTS, slot)) {
            return CocktailRecipeRegistry.isValidIngredient(stack);
        } else {
            return stack.isOf(Items.GLASS_BOTTLE) && this.getStack(slot).isEmpty();
        }
    }

    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        Iterator<ItemStack> var1 = this.inventory.iterator();

        ItemStack itemStack;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            itemStack = (ItemStack) var1.next();
        } while (itemStack.isEmpty());

        return false;
    }

    @Override
    public ItemStack getStack(int slot) {
        if (slot < inventory.size()) {
            return inventory.get(slot);
        }
        Utils.Log("ArrayIndexOutOfBoundsException", slot);
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(this.inventory, slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.inventory, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (slot >= 0 && slot < this.inventory.size()) {
            this.inventory.set(slot, stack);
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (this.world.getBlockEntity(this.pos) != this) {
            return false;
        } else {
            return !(player.squaredDistanceTo((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
                    (double) this.pos.getZ() + 0.5D) > 64.0D);
        }
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return side == Direction.DOWN ? OUTPUT_SLOTS : INPUT_SLOTS;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction dir) {
        return this.isValid(slot, stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return ArrayUtils.contains(OUTPUT_SLOTS, slot) ? stack.isOf(Items.GLASS_BOTTLE) : true;
    }
}