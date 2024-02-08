package ml.pkom.enhancedquarries.inventory.slot;

import ml.pkom.mcpitanlibarch.api.gui.slot.CompatibleSlot;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class ScannerSlot extends CompatibleSlot {
    public ScannerSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        if (getIndex() == 1) return false;
        return super.canInsert(stack);
    }
}
