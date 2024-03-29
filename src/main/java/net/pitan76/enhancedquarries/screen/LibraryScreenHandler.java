package net.pitan76.enhancedquarries.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.pitan76.enhancedquarries.ScreenHandlers;
import net.pitan76.enhancedquarries.inventory.slot.LibrarySlot;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.gui.SimpleScreenHandler;
import net.pitan76.mcpitanlib.api.util.SlotUtil;

public class LibraryScreenHandler extends SimpleScreenHandler {
    public Inventory libraryInventory;

    public String blueprintName = "";

    public LibraryScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(4));
    }

    public LibraryScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        this(ScreenHandlers.LIBRARY_SCREEN_HANDLER_TYPE, syncId, playerInventory, inventory);
    }

    public LibraryScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory libraryInventory) {
        super(type, syncId);
        this.libraryInventory = libraryInventory;

        addPlayerMainInventorySlots(playerInventory, 8, 84);
        addPlayerHotbarSlots(playerInventory, 8, 142);
        callAddSlot(new LibrarySlot(this, libraryInventory, 0, 87, 19));
        callAddSlot(new LibrarySlot(this, libraryInventory, 1, 135, 19));

        callAddSlot(new LibrarySlot(this, libraryInventory, 2, 87, 49));
        callAddSlot(new LibrarySlot(this, libraryInventory, 3, 135, 49));
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveOverride(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            if (index < 36) {
                if (!this.callInsertItem(originalStack, 36,  36 + libraryInventory.size() - 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
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

    public void setBlueprintName(String name) {
        blueprintName = name;
    }
}
