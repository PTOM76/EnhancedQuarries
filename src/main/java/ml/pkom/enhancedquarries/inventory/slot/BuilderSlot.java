package ml.pkom.enhancedquarries.inventory.slot;

import ml.pkom.enhancedquarries.Items;
import ml.pkom.mcpitanlibarch.api.gui.slot.CompatibleSlot;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class BuilderSlot extends CompatibleSlot {
    public BuilderSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        if (getIndex() == 0 && stack.getItem() != Items.BLUEPRINT) return false;
        return super.canInsert(stack);
    }
}
