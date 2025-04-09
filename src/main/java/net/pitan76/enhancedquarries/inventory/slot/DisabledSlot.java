package net.pitan76.enhancedquarries.inventory.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.gui.slot.CompatibleSlot;

public class DisabledSlot extends CompatibleSlot {
    public DisabledSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canTakePartial(Player player) {
        return false;
    }

    @Override
    public boolean canTakeItems(Player player) {
        return false;
    }
}
