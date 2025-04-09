package net.pitan76.enhancedquarries.inventory.slot;

import net.minecraft.inventory.Inventory;
import net.pitan76.enhancedquarries.Items;
import net.pitan76.mcpitanlib.api.gui.slot.CompatibleSlot;
import net.pitan76.mcpitanlib.midohra.item.ItemStack;

public class BuilderSlot extends CompatibleSlot {
    public BuilderSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        if (getIndex() == 0 && stack.getItem().get() != Items.BLUEPRINT) return false;
        return super.canInsert(stack);
    }
}
