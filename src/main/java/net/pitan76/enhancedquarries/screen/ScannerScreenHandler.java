package net.pitan76.enhancedquarries.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.pitan76.enhancedquarries.ScreenHandlers;
import net.pitan76.enhancedquarries.inventory.ScannerInventory;
import net.pitan76.enhancedquarries.inventory.slot.ScannerSlot;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.gui.SimpleScreenHandler;
import net.pitan76.mcpitanlib.api.util.SlotUtil;

public class ScannerScreenHandler extends SimpleScreenHandler {
    public Inventory scannerInventory;

    public ScannerScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new ScannerInventory());
    }

    public ScannerScreenHandler(int syncId, PlayerInventory playerInventory, Inventory scannerInventory) {
        this(ScreenHandlers.SCANNER_SCREEN_HANDLER_TYPE, syncId, playerInventory, scannerInventory);
    }

    public ScannerScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory scannerInventory) {
        super(type, syncId);
        this.scannerInventory = scannerInventory;

        addPlayerMainInventorySlots(playerInventory, 8, 84);
        addPlayerHotbarSlots(playerInventory, 8, 142);
        callAddSlot(new ScannerSlot(scannerInventory, 0, 53, 34));
        callAddSlot(new ScannerSlot(scannerInventory, 1, 107, 34));
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveOverride(Player player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (index < 36) {
                if (!this.callInsertItem(originalStack, 36, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index == 37) {
                if (!this.callInsertItem(originalStack, 0, 35, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (originalStack.isEmpty()) {
                SlotUtil.setStack(slot, ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return ItemStack.EMPTY;
    }
}
