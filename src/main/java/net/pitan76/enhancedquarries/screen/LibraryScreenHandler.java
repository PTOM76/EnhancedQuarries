package net.pitan76.enhancedquarries.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.pitan76.enhancedquarries.ScreenHandlers;
import net.pitan76.enhancedquarries.inventory.slot.LibrarySlot;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.gui.SimpleScreenHandler;
import net.pitan76.mcpitanlib.api.util.InventoryUtil;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.ScreenHandlerUtil;
import net.pitan76.mcpitanlib.api.util.SlotUtil;

public class LibraryScreenHandler extends SimpleScreenHandler {
    public Inventory libraryInventory;

    public String blueprintName = "";

    public LibraryScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, InventoryUtil.createSimpleInventory(4));
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
        Slot slot = ScreenHandlerUtil.getSlot(this, index);
        if (SlotUtil.hasStack(slot)) {
            ItemStack originalStack = SlotUtil.getStack(slot);
            if (index < 36) {
                if (!this.callInsertItem(originalStack, 36,  36 + InventoryUtil.getSize(libraryInventory) - 1, false)) {
                    return ItemStackUtil.empty();
                }
            } else {
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

    public void setBlueprintName(String name) {
        blueprintName = name;
    }
}
