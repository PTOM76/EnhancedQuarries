package net.pitan76.enhancedquarries.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;

public class ClippedInventory implements Inventory {

    private final Inventory inventory;
    private final int start;
    private final int end;

    public static ClippedInventory of(Inventory inventory, int start, int end) {
        if (start < 0 || end > inventory.size() || start >= end) {
            throw new IllegalArgumentException("Invalid start or end indices for clipping inventory.");
        }
        return new ClippedInventory(inventory, start, end);
    }

    public static ClippedInventory of(Inventory inventory) {
        return of(inventory, 0, inventory.size());
    }

    public static ClippedInventory of(Inventory inventory, int start) {
        return of(inventory, start, inventory.size());
    }

    public ClippedInventory(Inventory inventory, int start, int end) {
        this.inventory = inventory;
        this.start = start;
        this.end = end;
    }

    @Override
    public int size() {
        return end - start;
    }

    @Override
    public boolean isEmpty() {
        for (int i = start; i < end; i++) {
            if (!inventory.getStack(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.getStack(start + slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return inventory.removeStack(start + slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return inventory.removeStack(start + slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.setStack(start + slot, stack);
    }

    @Override
    public void markDirty() {
        inventory.markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    @Override
    public void clear() {
        for (int i = start; i < end; i++) {
            inventory.setStack(i, ItemStackUtil.empty());
        }
    }

    @Override
    public int getMaxCountPerStack() {
        return inventory.getMaxCountPerStack();
    }
}
