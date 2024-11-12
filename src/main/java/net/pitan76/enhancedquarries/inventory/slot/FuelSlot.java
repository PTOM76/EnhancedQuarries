package net.pitan76.enhancedquarries.inventory.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.pitan76.enhancedquarries.screen.EnergyGeneratorScreenHandler;
import net.pitan76.mcpitanlib.api.gui.slot.CompatibleSlot;
import net.pitan76.mcpitanlib.api.util.block.entity.FurnaceUtil;

public class FuelSlot extends CompatibleSlot {

    public EnergyGeneratorScreenHandler screenHandler;

    public FuelSlot(EnergyGeneratorScreenHandler screenHandler, Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.screenHandler = screenHandler;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        if (!FurnaceUtil.canUseAsFuel(stack, screenHandler.world)) return false;
        return super.canInsert(stack);
    }
}
