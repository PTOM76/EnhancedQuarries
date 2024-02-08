package ml.pkom.enhancedquarries.inventory.slot;

import ml.pkom.enhancedquarries.Items;
import ml.pkom.enhancedquarries.screen.LibraryScreenHandler;
import ml.pkom.enhancedquarries.util.BlueprintUtil;
import ml.pkom.mcpitanlibarch.api.gui.slot.CompatibleSlot;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class LibrarySlot extends CompatibleSlot {
    public LibraryScreenHandler screenHandler;

    public LibrarySlot(LibraryScreenHandler screenHandler,Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.screenHandler = screenHandler;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        if (getIndex() == 1 || getIndex() == 2) return false;
        if (getIndex() == 0 && (stack.getItem().equals(Items.BLUEPRINT) || stack.getItem().equals(Items.EMPTY_BLUEPRINT))) return true;
        if (getIndex() == 3 && stack.getItem().equals(Items.BLUEPRINT)) return true;
        return false;
    }

    @Override
    public void callSetStack(ItemStack stack) {
        super.callSetStack(stack);
        if (getIndex() == 3) { // Save
            BlueprintUtil.save(stack, screenHandler.blueprintName);
            super.callSetStack(ItemStack.EMPTY);
            inventory.setStack(2, stack);
        }
        if (getIndex() == 0) { // Load
            ItemStack newStack = new ItemStack(Items.BLUEPRINT, stack.getCount());
            BlueprintUtil.load(newStack, screenHandler.blueprintName);
            super.callSetStack(ItemStack.EMPTY);
            inventory.setStack(1, newStack);
        }
    }
}
