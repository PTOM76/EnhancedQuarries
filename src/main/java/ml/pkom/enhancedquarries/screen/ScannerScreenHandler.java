package ml.pkom.enhancedquarries.screen;

import ml.pkom.enhancedquarries.ScreenHandlers;
import ml.pkom.enhancedquarries.inventory.ScannerInventory;
import ml.pkom.enhancedquarries.inventory.slot.ScannerSlot;
import ml.pkom.mcpitanlibarch.api.gui.SimpleScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public class ScannerScreenHandler extends SimpleScreenHandler {
    public Inventory scannerInventory;

    @Deprecated
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
        addSlot(new ScannerSlot(scannerInventory, 0, 53, 34));
        addSlot(new ScannerSlot(scannerInventory, 1, 107, 34));
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack quickMoveOverride(PlayerEntity player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (index < 36) {
                if (!this.insertItem(originalStack, 36, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index == 37) {
                if (!this.insertItem(originalStack, 0, 35, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return ItemStack.EMPTY;
    }
}
