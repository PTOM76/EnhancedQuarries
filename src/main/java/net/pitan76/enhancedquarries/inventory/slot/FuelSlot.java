package net.pitan76.enhancedquarries.inventory.slot;

import ml.pkom.mcpitanlibarch.api.gui.slot.CompatibleSlot;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class FuelSlot extends CompatibleSlot {
    public FuelSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        if (!AbstractFurnaceBlockEntity.canUseAsFuel(stack)) return false;
        return super.canInsert(stack);
    }
}
