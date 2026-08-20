package net.pitan76.enhancedquarries.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.pitan76.mcpitanlib.api.gui.slot.CompatibleSlot;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;

public class TargetSlot extends CompatibleSlot {
    public TargetSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return true;
    }

    @Override
    public void callSetStack(ItemStack stack) {
        ItemStack newStack = stack.copy();
        newStack.setCount(1);

        super.callSetStack(newStack);
    }

    @Override
    public ItemStack callTakeStack(int amount) {
        callSetStack(ItemStackUtil.empty());
        return ItemStackUtil.empty();
    }
}
