package net.pitan76.enhancedquarries.inventory.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.pitan76.enhancedquarries.Items;
import net.pitan76.enhancedquarries.screen.LibraryScreenHandler;
import net.pitan76.enhancedquarries.util.BlueprintUtil;
import net.pitan76.mcpitanlib.api.gui.slot.CompatibleSlot;
import net.pitan76.mcpitanlib.api.util.InventoryUtil;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;

public class LibrarySlot extends CompatibleSlot {
    public LibraryScreenHandler screenHandler;

    public LibrarySlot(LibraryScreenHandler screenHandler,Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.screenHandler = screenHandler;
    }

    @Override
    public boolean canInsert(net.pitan76.mcpitanlib.midohra.item.ItemStack stack) {
        if (getIndex() == 1 || getIndex() == 2) return false;
        if (getIndex() == 0 && (stack.getItem().get() == Items.BLUEPRINT || stack.getItem().get() == Items.EMPTY_BLUEPRINT)) return true;
        if (getIndex() == 3 && stack.getItem().get() == Items.BLUEPRINT) return true;
        return false;
    }

    @Override
    public void callSetStack(ItemStack stack) {
        super.callSetStack(stack);
        Inventory inventory = callGetInventory();

        if (getIndex() == 3) { // Save
            BlueprintUtil.save(stack, screenHandler.blueprintName);
            super.callSetStack(ItemStackUtil.empty());
            InventoryUtil.setStack(inventory, 2, stack);
        }
        if (getIndex() == 0) { // Load
            ItemStack newStack = ItemStackUtil.create(Items.BLUEPRINT, ItemStackUtil.getCount(stack));
            BlueprintUtil.load(newStack, screenHandler.blueprintName);
            super.callSetStack(ItemStackUtil.empty());
            InventoryUtil.setStack(inventory, 1, newStack);
        }
    }
}
