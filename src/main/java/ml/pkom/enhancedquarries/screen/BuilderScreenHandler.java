package ml.pkom.enhancedquarries.screen;

import ml.pkom.enhancedquarries.ScreenHandlers;
import ml.pkom.enhancedquarries.inventory.FillerCraftingInventory;
import ml.pkom.enhancedquarries.inventory.FillerCraftingSlot;
import ml.pkom.enhancedquarries.inventory.FillerInventory;
import ml.pkom.mcpitanlibarch.api.gui.SimpleScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public class BuilderScreenHandler extends SimpleScreenHandler {
    public Inventory inventory;
    public Inventory craftingInventory;

    @Deprecated
    public BuilderScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new FillerInventory(), new FillerCraftingInventory());
    }

    public BuilderScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, Inventory craftingInventory) {
        this(ScreenHandlers.BUILDER_SCREEN_HANDLER_TYPE, syncId, playerInventory, inventory, craftingInventory);
    }

    public BuilderScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory, Inventory craftingInventory) {
        super(type, syncId);
        this.inventory = inventory;
        this.craftingInventory = craftingInventory;
        int m, l;
        int i = 0;
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 3; ++l) {
                addSlot(new FillerCraftingSlot(craftingInventory, i, 30 + l * 18, 17 + m * 18));
                i++;
            }
        }
        addSlot(new FillerCraftingSlot(craftingInventory, 9, 124, 35, true));
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                addSlot(new Slot(inventory, l + m * 9, 8 + l * 18, 85 + m * 18));
            }
        }
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 153 + m * 18));
            }
        }
        for (m = 0; m < 9; ++m) {
            addSlot(new Slot(playerInventory, m, 8 + m * 18, 211));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMoveOverride(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot instanceof FillerCraftingSlot) {
            if (invSlot != 9)
                return ItemStack.EMPTY;
        }
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < inventory.size() + craftingInventory.size()) {
                if (!this.insertItem(originalStack, inventory.size() + craftingInventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, craftingInventory.size(),craftingInventory.size() + inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }
}
