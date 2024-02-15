package net.pitan76.enhancedquarries.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.pitan76.enhancedquarries.ScreenHandlers;
import net.pitan76.enhancedquarries.inventory.slot.FuelSlot;
import net.pitan76.enhancedquarries.tile.base.EnergyGeneratorTile;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.gui.ExtendedScreenHandler;
import net.pitan76.mcpitanlib.api.network.PacketByteUtil;
import net.pitan76.mcpitanlib.api.util.SlotUtil;

public class EnergyGeneratorScreenHandler extends ExtendedScreenHandler {
    public Inventory inventory;
    public EnergyGeneratorTile tile;
    public long energy = 0;
    public long maxEnergy = 0;

    public EnergyGeneratorScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(ScreenHandlers.ENERGY_GENERATOR_SCREEN_HANDLER_TYPE, syncId, playerInventory, new SimpleInventory(1));
        if (buf == null) return;
        energy = PacketByteUtil.readLong(buf);
        maxEnergy = PacketByteUtil.readLong(buf);
    }

    public EnergyGeneratorScreenHandler(int syncId, PlayerInventory playerInventory, EnergyGeneratorTile tile) {
        this(ScreenHandlers.ENERGY_GENERATOR_SCREEN_HANDLER_TYPE, syncId, playerInventory, tile);
        this.tile = tile;
    }

    public EnergyGeneratorScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(type, syncId);
        this.inventory = inventory;

        addPlayerMainInventorySlots(playerInventory, 8, 84);
        addPlayerHotbarSlots(playerInventory, 8, 142);

        callAddSlot(new FuelSlot(inventory, 0, 80, 24));
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveOverride(Player player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (index < 36) {
                if (!this.callInsertItem(originalStack, 36, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index == 37) {
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
}
