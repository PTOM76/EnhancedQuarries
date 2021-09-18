package ml.pkom.enhancedquarries.inventory;

import ml.pkom.enhancedquarries.EnhancedQuarries;
import ml.pkom.enhancedquarries.FillerCraftingPattern;
import ml.pkom.enhancedquarries.FillerCraftingPatterns;
import ml.pkom.enhancedquarries.item.base.FillerModule;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class FillerCraftingSlot extends Slot {

    public int index;

    public boolean isOutput;

    public FillerCraftingSlot(Inventory inventory, int index, int x, int y) {
        this(inventory, index, x, y, false);
    }

    public FillerCraftingSlot(Inventory inventory, int index, int x, int y, boolean isOutput) {
        super(inventory, index, x, y);
        this.isOutput = isOutput;
        this.index = index;
    }

    boolean isCrafting = false;

    @Override
    public void setStack(ItemStack stack) {
        super.setStack(stack);
        if (isOutput) {
            if (stack.isEmpty() && !isCrafting) {
                int i;
                for (i = 0;i < 9;i++) {
                    inventory.setStack(i, ItemStack.EMPTY);
                }
            }
        } else {
            // Input
            isCrafting = true;
            tryCraft();
            isCrafting = false;
        }
    }

    public void tryCraft() {
        for(FillerCraftingPattern pattern : FillerCraftingPatterns.patterns) {
            if (pattern.inputEquals(new FillerCraftingPattern(
                    ItemStack.EMPTY, inventory.getStack(0), inventory.getStack(1), inventory.getStack(2),
                    inventory.getStack(3), inventory.getStack(4), inventory.getStack(5), inventory.getStack(6),
                    inventory.getStack(7), inventory.getStack(8)
            ))) {
                inventory.setStack(9, pattern.getOutput());
                return;
            }
        }
        int i;
        int moduleAmount = 0;
        int moduleSlot = 0;
        for (i = 0;i < 9;i++) {
            if (inventory.getStack(i).getItem() instanceof FillerModule) {
                moduleAmount++;
                moduleSlot = i;
            }
        }
        if (moduleAmount == 1) {
            inventory.setStack(9, inventory.getStack(moduleSlot).copy());
            return;
        }
        inventory.setStack(9, ItemStack.EMPTY);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return !isOutput;
    }

    @Override
    public int getMaxItemCount() {
        return 1;
    }
}
