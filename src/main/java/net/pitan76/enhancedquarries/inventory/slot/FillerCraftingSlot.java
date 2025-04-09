package net.pitan76.enhancedquarries.inventory.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.pitan76.enhancedquarries.FillerCraftingPattern;
import net.pitan76.enhancedquarries.FillerCraftingPatterns;
import net.pitan76.enhancedquarries.item.base.FillerModule;
import net.pitan76.mcpitanlib.api.gui.slot.CompatibleSlot;
import net.pitan76.mcpitanlib.api.util.InventoryUtil;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;

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
            if (ItemStackUtil.isEmpty(stack) && !isCrafting) {
                int i;
                for (i = 0;i < 9;i++) {
                    InventoryUtil.setStack(inventory, i, ItemStackUtil.empty());
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
                    InventoryUtil.setStack(inventory, i, ItemStackUtil.empty());
                }
            }

        }
        return stack;
    }

    public void tryCraft() {
        for(FillerCraftingPattern pattern : FillerCraftingPatterns.getPatterns()) {
            if (pattern.inputEquals(new FillerCraftingPattern(
                    ItemStackUtil.empty(), InventoryUtil.getStack(inventory, 0), InventoryUtil.getStack(inventory, 1), InventoryUtil.getStack(inventory, 2),
                    InventoryUtil.getStack(inventory, 3), InventoryUtil.getStack(inventory, 4), InventoryUtil.getStack(inventory, 5), InventoryUtil.getStack(inventory, 6),
                    InventoryUtil.getStack(inventory, 7), InventoryUtil.getStack(inventory, 8)
            ))) {
                InventoryUtil.setStack(inventory, 9, pattern.getOutput());
                return;
            }
        }
        int i;
        int moduleAmount = 0;
        int moduleSlot = 0;
        for (i = 0;i < 9;i++) {
            if (InventoryUtil.getStack(inventory, i).getItem() instanceof FillerModule) {
                moduleAmount++;
                moduleSlot = i;
            }
        }
        if (moduleAmount == 1) {
            InventoryUtil.setStack(inventory, 9, InventoryUtil.getStack(inventory, moduleSlot).copy());
            return;
        }
        InventoryUtil.setStack(inventory, 9, ItemStackUtil.empty());
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
