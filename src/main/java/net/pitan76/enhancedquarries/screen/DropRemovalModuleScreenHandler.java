package net.pitan76.enhancedquarries.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.pitan76.enhancedquarries.ScreenHandlers;
import net.pitan76.enhancedquarries.item.quarrymodule.DropRemovalModule;
import net.pitan76.enhancedquarries.screen.slot.TargetSlot;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.gui.SimpleScreenHandler;
import net.pitan76.mcpitanlib.api.util.*;

public class DropRemovalModuleScreenHandler extends SimpleScreenHandler {

    public PlayerInventory playerInventory;
    public Inventory targetInventory;

    public ItemStack moduleStack = ItemStackUtil.empty();

    public void setStackFromNbt(NbtCompound nbt) {
        if (!NbtUtil.has(nbt, "Items")) return;

        NbtList items = NbtUtil.getList(nbt, "Items");
        for (int i = 0; i < 5; i++) {
            String item = items.getString(i);
            if (item.isEmpty()) continue;
            CompatIdentifier id = CompatIdentifier.of(item);

            if (!ItemUtil.isExist(id)) continue;

            targetInventory.setStack(i, ItemStackUtil.create(ItemUtil.fromId(id)));
        }
    }

    public DropRemovalModuleScreenHandler(int syncId, PlayerInventory playerInventory, ItemStack stack) {
        this(syncId, playerInventory);

        Player player = new Player(playerInventory.player);
        NbtCompound nbt = DropRemovalModule.getNbtFromCurrentStack(player);
        if (nbt == null) return;
        setStackFromNbt(nbt);

        moduleStack = stack;
    }

    public DropRemovalModuleScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(ScreenHandlers.DROP_REMOVAL_MODULE_SCREEN_HANDLER_TYPE, syncId, playerInventory, InventoryUtil.createSimpleInventory(9));
    }

    public DropRemovalModuleScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(type, syncId);

        this.playerInventory = playerInventory;
        this.targetInventory = inventory;
        initSlots();
    }

    public void initSlots() {
        addPlayerMainInventorySlots(playerInventory, 8, 51);
        addPlayerHotbarSlots(playerInventory, 8, 109);

        for (int i = 0; i < 5; i++) {
            addTargetSlot(targetInventory, i, 44 + i * 18, 20);
        }
    }

    public void addTargetSlot(Inventory inventory, int index, int x, int y) {
        callAddSlot(new TargetSlot(inventory, index, x, y));
    }

    @Override
    public ItemStack quickMoveOverride(Player player, int index) {
        Slot slot = ScreenHandlerUtil.getSlot(this, index);
        if (SlotUtil.hasStack(slot)) {
            ItemStack originalStack = SlotUtil.getStack(slot);
            if (index < 36) {
                if (!this.callInsertItem(originalStack, 36, 41, false)) {
                    return ItemStackUtil.empty();
                }
            } else if (index >= 37) {
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

    @Override
    public void close(Player player) {
        if (ItemStackUtil.isEmpty(moduleStack)) {
            super.close(player);
            return;
        }

        NbtList items = NbtUtil.createNbtList();
        for (int i = 0; i < targetInventory.size(); i++) {
            ItemStack stack = targetInventory.getStack(i);
            if (ItemStackUtil.isEmpty(stack)) continue;

            items.add(NbtString.of(ItemUtil.toCompatID(stack.getItem()).toString()));
        }

        CustomDataUtil.put(moduleStack, "Items", items);
        super.close(player);
    }

    @Override
    public void overrideOnSlotClick(int slotIndex, int button, SlotActionType actionType, Player player) {
        Slot targetSlot = callGetSlot(slotIndex);

        if (slotIndex >= 36) { // Target Slot
            // カーソルでアイテムを持ってない場合
            if (getCursorStack().isEmpty()) {
                SlotUtil.setStack(targetSlot, ItemStackUtil.empty());
                return;
            }

            ItemStack oldStack = ItemStackUtil.create(getCursorStack().getItem());
            SlotUtil.setStack(targetSlot, oldStack);
            callSetCursorStack(getCursorStack());
            return;
        }

        super.overrideOnSlotClick(slotIndex, button, actionType, player);
    }
}
