package ml.pkom.enhancedquarries.screen;

import ml.pkom.enhancedquarries.ScreenHandlers;
import ml.pkom.enhancedquarries.inventory.BuilderInventory;
import ml.pkom.enhancedquarries.inventory.DisabledInventory;
import ml.pkom.enhancedquarries.inventory.slot.BuilderSlot;
import ml.pkom.mcpitanlibarch.api.gui.SimpleScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public class BuilderScreenHandler extends SimpleScreenHandler {
    public Inventory builderInventory; // index0=Blueprint
    public Inventory needInventory;

    @Deprecated
    public BuilderScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new BuilderInventory(), new DisabledInventory(27));
    }

    public BuilderScreenHandler(int syncId, PlayerInventory playerInventory, Inventory builderInventory, Inventory needInventory) {
        this(ScreenHandlers.BUILDER_SCREEN_HANDLER_TYPE, syncId, playerInventory, builderInventory, needInventory);
    }

    public BuilderScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory builderInventory, Inventory needInventory) {
        super(type, syncId);
        this.builderInventory = builderInventory;
        this.needInventory = needInventory;

        addPlayerMainInventorySlots(playerInventory, 8, 153);
        addPlayerHotbarSlots(playerInventory, 8, 211);
        addSlot(new BuilderSlot(builderInventory, 0, 80, 34));
        addSlots(builderInventory, 1, 8, 85, 18, 9, 3);
        addSlots(needInventory, 0, 180, 13, 18, 3, 9);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack quickMoveOverride(PlayerEntity player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot instanceof BuilderSlot) {
            //if (invSlot != 9)
            //    return ItemStack.EMPTY;
        }
        if (slot.inventory instanceof DisabledInventory) {
            return ItemStack.EMPTY;
        }
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (index < 36) {
                if (!this.insertItem(originalStack, 36,  36 + builderInventory.size() - 1, false)) {
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
