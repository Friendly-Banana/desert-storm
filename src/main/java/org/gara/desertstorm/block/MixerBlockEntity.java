package org.gara.desertstorm.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
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
import org.apache.commons.lang3.ArrayUtils;
import org.gara.desertstorm.DesertStorm;
import org.gara.desertstorm.Utils;
import org.gara.desertstorm.item.cocktail.CocktailRecipeRegistry;
import org.gara.desertstorm.screen.MixerScreenHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MixerBlockEntity extends LockableContainerBlockEntity implements SidedInventory {
    private static final int[] INPUT_SLOTS = {0, 1, 2};
    private static final int[] OUTPUT_SLOTS = {3, 4, 5};
    protected final PropertyDelegate propertyDelegate;
    private final DefaultedList<ItemStack> inventory;
    /**
     * Remaining Time till cocktail is finished
     */
    int mixTime;
    private ItemStack mixing;
    private boolean[] slotsEmptyLastTick;

    public MixerBlockEntity(BlockPos pos, BlockState state) {
        super(DesertStorm.MIXER_BLOCK_ENTITY, pos, state);
        this.inventory = DefaultedList.ofSize(6, ItemStack.EMPTY);
        this.propertyDelegate = new MyPropertyDelegate(this);
    }

    public static void tick(World world, BlockPos pos, BlockState state, MixerBlockEntity blockEntity) {
        boolean weCanCraft = canCraft(blockEntity.inventory);
        boolean stillMixing = blockEntity.mixTime > 0;
        if (weCanCraft) {
            if (blockEntity.mixing == null) {
                blockEntity.mixing = CocktailRecipeRegistry.craft(inputSlots(blockEntity.inventory));
                blockEntity.mixTime = 300;
            }
            if (stillMixing) {
                --blockEntity.mixTime;
            }
            if (blockEntity.mixTime == 0) {
                craft(world, pos, blockEntity.inventory, blockEntity.mixing);
                blockEntity.mixing = null;
                blockEntity.mixTime = 300;
                markDirty(world, pos, state);
            }
        } else {
            blockEntity.mixing = null;
        }

        // show bottles
        boolean[] bottles = blockEntity.getGlassBottles();
        if (!Arrays.equals(bottles, blockEntity.slotsEmptyLastTick)) {
            blockEntity.slotsEmptyLastTick = bottles;
            BlockState blockState = state;
            if (!(state.getBlock() instanceof MixerBlock)) {
                return;
            }

            for (int i = 0; i < MixerBlock.BOTTLE_PROPERTIES.length; ++i) {
                blockState = blockState.with(MixerBlock.BOTTLE_PROPERTIES[i], bottles[i]);
            }

            world.setBlockState(pos, blockState, Block.NOTIFY_LISTENERS);
        }
    }

    /**
     * Cleans slots up
     *
     * @param slots All slots of an inventory
     * @return input slots containing Items
     */
    private static List<ItemStack> inputSlots(DefaultedList<ItemStack> slots) {
        List<ItemStack> items = new ArrayList<>(3);
        for (int i : INPUT_SLOTS) {
            if (!slots.get(i).isEmpty()) {
                items.add(slots.get(i));
            }
        }
        return items;
    }

    private static boolean canCraft(DefaultedList<ItemStack> slots) {
        if (CocktailRecipeRegistry.hasRecipe(inputSlots(slots))) {
            // at least one bottle needed
            for (int i : OUTPUT_SLOTS) {
                if (slots.get(i).isOf(Items.GLASS_BOTTLE)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void craft(World world, BlockPos pos, DefaultedList<ItemStack> slots, ItemStack expected) {
        ItemStack result = CocktailRecipeRegistry.craft(inputSlots(slots));
        if (!result.isItemEqual(expected)) return;
        for (int i : OUTPUT_SLOTS) {
            if (slots.get(i).isOf(Items.GLASS_BOTTLE)) {
                slots.set(i, result);
            }
        }

        for (int i : INPUT_SLOTS) {
            ItemStack itemStack = slots.get(i);
            itemStack.decrement(1);
            // Bottles can remain
            if (itemStack.getItem().hasRecipeRemainder()) {
                ItemStack remainder = new ItemStack(itemStack.getItem().getRecipeRemainder());
                // Ingredient used up
                if (itemStack.isEmpty()) {
                    slots.set(i, remainder);
                }  // drop Item
                else {
                    ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(),
                            remainder);
                }
            }
        }

        world.syncWorldEvent(WorldEvents.BREWING_STAND_BREWS, pos, 0);
    }

    public DefaultedList<ItemStack> getItems() {
        return inventory;
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

            itemStack = var1.next();
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
        return !ArrayUtils.contains(OUTPUT_SLOTS, slot) || stack.isOf(Items.GLASS_BOTTLE);
    }

    private record MyPropertyDelegate(
            MixerBlockEntity mixerBlockEntity) implements PropertyDelegate {

        @Override
        public int get(int index) {
            Utils.Debug();
            Utils.Log(mixerBlockEntity.mixTime);
            return mixerBlockEntity.mixTime;
        }

        @Override
        public void set(int index, int value) {
            mixerBlockEntity.mixTime = value;
        }

        @Override
        public int size() {
            return 1;
        }
    }
}