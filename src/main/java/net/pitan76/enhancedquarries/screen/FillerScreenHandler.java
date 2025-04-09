package net.pitan76.enhancedquarries.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.pitan76.enhancedquarries.ScreenHandlers;
import net.pitan76.enhancedquarries.inventory.FillerCraftingInventory;
import net.pitan76.enhancedquarries.inventory.FillerInventory;
import net.pitan76.enhancedquarries.inventory.slot.FillerCraftingSlot;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.gui.SimpleScreenHandler;
import net.pitan76.mcpitanlib.api.util.InventoryUtil;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.ScreenHandlerUtil;
import net.pitan76.mcpitanlib.api.util.SlotUtil;

public class FillerScreenHandler extends SimpleScreenHandler {
    public Inventory inventory;
    public Inventory craftingInventory;

    public FillerScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new FillerInventory(), new FillerCraftingInventory());
    }

    public FillerScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, Inventory craftingInventory) {
        this(ScreenHandlers.FILLER_SCREEN_HANDLER_TYPE, syncId, playerInventory, inventory, craftingInventory);
    }

    public FillerScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory, Inventory craftingInventory) {
        super(type, syncId);
        this.inventory = inventory;
        this.craftingInventory = craftingInventory;
        int m, l;
        int i = 0;
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 3; ++l) {
                callAddSlot(new FillerCraftingSlot(craftingInventory, i, 30 + l * 18, 17 + m * 18));
                i++;
            }
        }
        callAddSlot(new FillerCraftingSlot(craftingInventory, 9, 124, 35, true));
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                callAddSlot(new Slot(inventory, l + m * 9, 8 + l * 18, 85 + m * 18));
            }
        }
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                callAddSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 153 + m * 18));
            }
        }
        for (m = 0; m < 9; ++m) {
            callAddSlot(new Slot(playerInventory, m, 8 + m * 18, 211));
        }
    }

    @Override
    public boolean canUse(Player player) {
        return inventory.canPlayerUse(player.getPlayerEntity());
    }

    @Override
    public ItemStack quickMoveOverride(Player player, int invSlot) {
        ItemStack newStack = ItemStackUtil.empty();
        Slot slot = ScreenHandlerUtil.getSlots(this).get(invSlot);
        if (slot instanceof FillerCraftingSlot) {
            if (invSlot != 9)
                return ItemStackUtil.empty();
        }
        if (slot != null && SlotUtil.hasStack(slot)) {
            ItemStack originalStack = SlotUtil.getStack(slot);
            newStack = originalStack.copy();
            if (invSlot < InventoryUtil.getSize(inventory) + InventoryUtil.getSize(craftingInventory)) {
                if (!this.callInsertItem(originalStack, InventoryUtil.getSize(inventory) + InventoryUtil.getSize(craftingInventory), ScreenHandlerUtil.getSlots(this).size(), true)) {
                    return ItemStackUtil.empty();
                }
            } else if (!this.callInsertItem(originalStack, InventoryUtil.getSize(craftingInventory),InventoryUtil.getSize(craftingInventory) + InventoryUtil.getSize(inventory), false)) {
                return ItemStackUtil.empty();
            }

            if (originalStack.isEmpty()) {
                SlotUtil.setStack(slot, ItemStackUtil.empty());
            } else {
                SlotUtil.markDirty(slot);
            }
        }

        return newStack;
    }
}
