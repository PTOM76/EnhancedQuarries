package net.pitan76.enhancedquarries.inventory.slot;

import net.pitan76.enhancedquarries.FillerCraftingPattern;
import net.pitan76.enhancedquarries.FillerCraftingPatterns;
import net.pitan76.enhancedquarries.item.base.FillerModule;
import ml.pkom.mcpitanlibarch.api.gui.slot.CompatibleSlot;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class FillerCraftingSlot extends CompatibleSlot {

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
    public void callSetStack(ItemStack stack) {
        super.callSetStack(stack);
        if (isOutput) {
            if (stack.isEmpty() && !isCrafting) {
                int i;
                for (i = 0;i < 9;i++) {
                    inventory.setStack(i, ItemStack.EMPTY);
                }
            }
            return;
        }

        // Input
        isCrafting = true;
        tryCraft();
        isCrafting = false;
    }

    @Override
    public ItemStack callTakeStack(int amount) {
        ItemStack stack = super.callTakeStack(amount);
        if (isOutput) {
            if (callGetStack().isEmpty() && !isCrafting) {
                int i;
                for (i = 0;i < 9;i++) {
                    inventory.setStack(i, ItemStack.EMPTY);
                }
            }

        }
        return stack;
    }

    public void tryCraft() {
        for(FillerCraftingPattern pattern : FillerCraftingPatterns.getPatterns()) {
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
