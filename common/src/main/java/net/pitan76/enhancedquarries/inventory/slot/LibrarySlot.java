package net.pitan76.enhancedquarries.inventory.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.pitan76.enhancedquarries.Items;
import net.pitan76.enhancedquarries.screen.LibraryScreenHandler;
import net.pitan76.enhancedquarries.util.BlueprintUtil;
import net.pitan76.mcpitanlib.api.gui.slot.CompatibleSlot;
import net.pitan76.mcpitanlib.api.util.InventoryUtil;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;

public class LibrarySlot extends CompatibleSlot {

    public static final int SLOT_LOAD_INPUT = 0;
    public static final int SLOT_LOAD_OUTPUT = 1;
    public static final int SLOT_SAVE_OUTPUT = 2;
    public static final int SLOT_SAVE_INPUT = 3;

    public LibraryScreenHandler screenHandler;

    public LibrarySlot(LibraryScreenHandler screenHandler, Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.screenHandler = screenHandler;
    }

    @Override
    public boolean canInsert(net.pitan76.mcpitanlib.midohra.item.ItemStack stack) {
        // 書き込み済みのものを入れると上書きで中身が消えるため空のブループリントのみ許可
        if (getIndex() == SLOT_LOAD_INPUT) return stack.getItem().get() == Items.EMPTY_BLUEPRINT;
        if (getIndex() == SLOT_SAVE_INPUT) return stack.getItem().get() == Items.BLUEPRINT;
        return false;
    }

    @Override
    public void callSetStack(ItemStack stack) {
        super.callSetStack(stack);

        // 空にされたとき(同期・クイックムーブなど)に走ると出力スロットを消してしまう
        if (ItemStackUtil.isEmpty(stack)) return;
        if (screenHandler.isClient()) return;

        String name = BlueprintUtil.normalizeName(screenHandler.blueprintName);
        if (name == null) return;

        Inventory inventory = callGetInventory();

        if (getIndex() == SLOT_SAVE_INPUT) { // Save
            if (!ItemStackUtil.isEmpty(InventoryUtil.getStack(inventory, SLOT_SAVE_OUTPUT))) return;
            if (!BlueprintUtil.save(net.pitan76.mcpitanlib.midohra.item.ItemStack.of(stack), name)) return;

            super.callSetStack(ItemStackUtil.empty());
            InventoryUtil.setStack(inventory, SLOT_SAVE_OUTPUT, stack);
        }
        if (getIndex() == SLOT_LOAD_INPUT) { // Load
            if (!ItemStackUtil.isEmpty(InventoryUtil.getStack(inventory, SLOT_LOAD_OUTPUT))) return;

            ItemStack newStack = ItemStackUtil.create(Items.BLUEPRINT, ItemStackUtil.getCount(stack));
            if (!BlueprintUtil.load(net.pitan76.mcpitanlib.midohra.item.ItemStack.of(newStack), name)) return;

            super.callSetStack(ItemStackUtil.empty());
            InventoryUtil.setStack(inventory, SLOT_LOAD_OUTPUT, newStack);
        }
    }
}
