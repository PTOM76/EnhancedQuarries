package ml.pkom.enhancedquarries.screen;

import ml.pkom.enhancedquarries.ScreenHandlers;
import ml.pkom.enhancedquarries.inventory.LibraryInventory;
import ml.pkom.enhancedquarries.inventory.slot.LibrarySlot;
import ml.pkom.mcpitanlibarch.api.gui.SimpleScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public class LibraryScreenHandler extends SimpleScreenHandler {
    public Inventory libraryInventory;

    @Deprecated
    public LibraryScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new LibraryInventory());
    }

    public LibraryScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        this(ScreenHandlers.LIBRARY_SCREEN_HANDLER_TYPE, syncId, playerInventory, inventory);
    }

    public LibraryScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory libraryInventory) {
        super(type, syncId);
        this.libraryInventory = libraryInventory;

        addPlayerMainInventorySlots(playerInventory, 8, 84);
        addPlayerHotbarSlots(playerInventory, 8, 142);
        addSlot(new LibrarySlot(libraryInventory, 0, 87, 19));
        addSlot(new LibrarySlot(libraryInventory, 1, 135, 19));

        addSlot(new LibrarySlot(libraryInventory, 2, 87, 49));
        addSlot(new LibrarySlot(libraryInventory, 3, 135, 49));
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack quickMoveOverride(PlayerEntity player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot instanceof LibrarySlot) {
            //if (index != 9)
            //    return ItemStack.EMPTY;
        }
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (index < 36) {
                if (!this.insertItem(originalStack, 36,  36 + libraryInventory.size() - 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
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
