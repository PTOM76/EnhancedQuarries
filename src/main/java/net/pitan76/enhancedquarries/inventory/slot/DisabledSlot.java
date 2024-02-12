package net.pitan76.enhancedquarries.inventory.slot;

import ml.pkom.mcpitanlibarch.api.gui.slot.CompatibleSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class DisabledSlot extends CompatibleSlot {
    public DisabledSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canTakePartial(PlayerEntity player) {
        return false;
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity) {
        return false;
    }
}
