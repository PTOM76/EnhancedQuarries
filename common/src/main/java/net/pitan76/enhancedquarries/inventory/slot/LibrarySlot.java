package net.pitan76.enhancedquarries.inventory.slot;

import net.minecraft.inventory.Inventory;
import net.pitan76.enhancedquarries.Items;
import net.pitan76.enhancedquarries.screen.LibraryScreenHandler;
import net.pitan76.mcpitanlib.api.gui.slot.CompatibleSlot;
import net.pitan76.mcpitanlib.midohra.item.ItemStack;
import net.pitan76.mcpitanlib.midohra.item.ItemWrapper;

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
    public boolean canInsert(ItemStack stack) {
        // 書き込み済みのものを入れると上書きで中身が消えるため空のものだけ許可
        ItemWrapper item = stack.getItem();

        if (getIndex() == SLOT_LOAD_INPUT)
            return item.rawEquals(Items.EMPTY_BLUEPRINT) || item.rawEquals(Items.EMPTY_TEMPLATE);
        if (getIndex() == SLOT_SAVE_INPUT)
            return item.rawEquals(Items.BLUEPRINT) || item.rawEquals(Items.TEMPLATE);
        return false;
    }
}
