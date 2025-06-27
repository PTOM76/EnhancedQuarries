package net.pitan76.enhancedquarries.util;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.collection.ItemStackList;
import org.jetbrains.annotations.NotNull;

public class ClippedItemStackList extends ItemStackList {

    public final ItemStackList list;
    public final int start;
    public final int end;

    public ClippedItemStackList(ItemStackList list, int start, int end) {
        super(null, null);
        this.list = list;
        if (start < 0 || end > list.size() || start >= end) {
            throw new IllegalArgumentException("Invalid start or end indices for clipping item stack list.");
        }
        this.start = start;
        this.end = end;
    }

    public static ClippedItemStackList of(ItemStackList list, int start, int end) {
        return new ClippedItemStackList(list, start, end);
    }

    public static ClippedItemStackList of(ItemStackList list) {
        return of(list, 0, list.size());
    }

    public static ClippedItemStackList of(ItemStackList list, int start) {
        return of(list, start, list.size());
    }

    @Override
    public int size() {
        return end - start;
    }

    @Override
    public Inventory toInventory() {
        return ClippedInventory.of(list.toInventory(), start, end);
    }

    @Override
    public DefaultedList<ItemStack> defaultedList() {
        DefaultedList<ItemStack> clippedList = DefaultedList.ofSize(size(), ItemStackUtil.empty());
        for (int i = 0; i < size(); i++)
            clippedList.set(i, get(i));

        return clippedList;
    }

    @Override
    public @NotNull ItemStack get(int index) {
        return list.get(start + index);
    }

    @Override
    public ItemStack set(int index, ItemStack element) {
        return list.set(start + index, element);
    }

    @Override
    public ItemStack remove(int index) {
        return list.remove(start + index);
    }

    @Override
    public boolean contains(Object o) {
        for (int i = start; i < end; i++) {
            if (list.get(i).equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        for (int i = start; i < end; i++) {
            if (!list.get(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void clear() {
        for (int i = start; i < end; i++) {
            list.set(i, ItemStackUtil.empty());
        }
    }


}
