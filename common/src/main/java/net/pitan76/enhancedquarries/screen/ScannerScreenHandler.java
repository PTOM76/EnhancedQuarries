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
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.ScreenHandlerUtil;
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
        ItemStack newStack = ItemStackUtil.empty();
        Slot slot = ScreenHandlerUtil.getSlot(this, index);
        if (SlotUtil.hasStack(slot)) {
            ItemStack originalStack = SlotUtil.getStack(slot);
            newStack = originalStack.copy();
            if (index < 36) {
                if (!this.callInsertItem(originalStack, 36, 37, false)) {
                    return ItemStackUtil.empty();
                }
            } else if (index == 37) {
                if (!this.callInsertItem(originalStack, 0, 35, false)) {
                    return ItemStackUtil.empty();
                }
            }

            if (originalStack.isEmpty()) {
                SlotUtil.setStack(slot, ItemStackUtil.empty());
            } else {
                SlotUtil.markDirty(slot);
            }
        }

        return ItemStackUtil.empty();
    }
}
