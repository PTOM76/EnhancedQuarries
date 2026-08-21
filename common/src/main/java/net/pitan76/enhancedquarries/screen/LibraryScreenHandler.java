package net.pitan76.enhancedquarries.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.pitan76.enhancedquarries.Items;
import net.pitan76.enhancedquarries.ScreenHandlers;
import net.pitan76.enhancedquarries.inventory.slot.LibrarySlot;
import net.pitan76.enhancedquarries.util.BlueprintUtil;
import net.pitan76.enhancedquarries.util.TemplateUtil;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.gui.SimpleScreenHandler;
import net.pitan76.mcpitanlib.api.gui.args.SlotClickEvent;
import net.pitan76.mcpitanlib.api.util.InventoryUtil;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.ScreenHandlerUtil;
import net.pitan76.mcpitanlib.api.util.SlotUtil;

public class LibraryScreenHandler extends SimpleScreenHandler {
    public Inventory libraryInventory;

    public PlayerInventory playerInventory;

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
        this.playerInventory = playerInventory;

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
        process();
    }

    @Override
    public void onSlotClick(SlotClickEvent e) {
        super.onSlotClick(e);
        process();
    }

    public void process() {
        if (isClient()) return;

        String name = BlueprintUtil.normalizeName(blueprintName);
        if (name == null) return;

        trySaving(name);
        tryLoading(name);
    }

    protected void trySaving(String name) {
        ItemStack input = InventoryUtil.getStack(libraryInventory, LibrarySlot.SLOT_SAVE_INPUT);
        if (ItemStackUtil.isEmpty(input)) return;
        if (!ItemStackUtil.isEmpty(InventoryUtil.getStack(libraryInventory, LibrarySlot.SLOT_SAVE_OUTPUT))) return;

        boolean template = input.getItem() == Items.TEMPLATE;
        net.pitan76.mcpitanlib.midohra.item.ItemStack midohraInput = net.pitan76.mcpitanlib.midohra.item.ItemStack.of(input);

        if (!(template ? TemplateUtil.save(midohraInput, name) : BlueprintUtil.save(midohraInput, name))) return;

        InventoryUtil.setStack(libraryInventory, LibrarySlot.SLOT_SAVE_INPUT, ItemStackUtil.empty());
        InventoryUtil.setStack(libraryInventory, LibrarySlot.SLOT_SAVE_OUTPUT, input);
        libraryInventory.markDirty();
    }

    protected void tryLoading(String name) {
        ItemStack input = InventoryUtil.getStack(libraryInventory, LibrarySlot.SLOT_LOAD_INPUT);
        if (ItemStackUtil.isEmpty(input)) return;
        if (!ItemStackUtil.isEmpty(InventoryUtil.getStack(libraryInventory, LibrarySlot.SLOT_LOAD_OUTPUT))) return;

        boolean template = input.getItem() == Items.EMPTY_TEMPLATE;
        ItemStack loaded = ItemStackUtil.create(template ? Items.TEMPLATE : Items.BLUEPRINT, ItemStackUtil.getCount(input));
        net.pitan76.mcpitanlib.midohra.item.ItemStack midohraLoaded = net.pitan76.mcpitanlib.midohra.item.ItemStack.of(loaded);

        // 読込に失敗したら入力はそのまま残す
        if (!(template ? TemplateUtil.load(midohraLoaded, name) : BlueprintUtil.load(midohraLoaded, name))) return;

        InventoryUtil.setStack(libraryInventory, LibrarySlot.SLOT_LOAD_INPUT, ItemStackUtil.empty());
        InventoryUtil.setStack(libraryInventory, LibrarySlot.SLOT_LOAD_OUTPUT, loaded);
        libraryInventory.markDirty();
    }

    public boolean isClient() {
        return new Player(playerInventory.player).isClient();
    }
}
