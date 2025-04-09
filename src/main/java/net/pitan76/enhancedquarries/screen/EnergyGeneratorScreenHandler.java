package net.pitan76.enhancedquarries.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;
import net.pitan76.enhancedquarries.ScreenHandlers;
import net.pitan76.enhancedquarries.inventory.slot.FuelSlot;
import net.pitan76.enhancedquarries.tile.base.EnergyGeneratorTile;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.gui.ExtendedScreenHandler;
import net.pitan76.mcpitanlib.api.gui.args.CreateMenuEvent;
import net.pitan76.mcpitanlib.api.network.PacketByteUtil;
import net.pitan76.mcpitanlib.api.util.InventoryUtil;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.ScreenHandlerUtil;
import net.pitan76.mcpitanlib.api.util.SlotUtil;

public class EnergyGeneratorScreenHandler extends ExtendedScreenHandler {
    public Inventory inventory;
    public EnergyGeneratorTile tile;
    public long energy = 0;
    public long maxEnergy = 0;
    public int burnTime = 0;
    public int maxBurnTime = 0;

    public World world;

    public EnergyGeneratorScreenHandler(CreateMenuEvent e, PacketByteBuf buf) {
        this(ScreenHandlers.ENERGY_GENERATOR_SCREEN_HANDLER_TYPE, e.syncId, e.playerInventory, InventoryUtil.createSimpleInventory(1));
        if (buf == null) return;
        energy = PacketByteUtil.readLong(buf);
        maxEnergy = PacketByteUtil.readLong(buf);
        burnTime = PacketByteUtil.readInt(buf);
        maxBurnTime = PacketByteUtil.readInt(buf);
        this.world = new Player(e.playerInventory.player).getWorld();
    }

    public EnergyGeneratorScreenHandler(CreateMenuEvent e, EnergyGeneratorTile tile) {
        this(ScreenHandlers.ENERGY_GENERATOR_SCREEN_HANDLER_TYPE, e.syncId, e.playerInventory, tile);
        this.tile = tile;
        this.world = tile.callGetWorld();
    }

    public EnergyGeneratorScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(type, syncId);
        this.inventory = inventory;
        this.world = new Player(playerInventory.player).getWorld();

        addPlayerMainInventorySlots(playerInventory, 8, 84);
        addPlayerHotbarSlots(playerInventory, 8, 142);

        callAddSlot(new FuelSlot(this, inventory, 0, 80, 24));
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveOverride(Player player, int index) {
        ItemStack newStack = ItemStackUtil.empty();
        Slot slot = ScreenHandlerUtil.getSlot(this, index);
        if (SlotUtil.hasStack(slot)) {
            ItemStack originalStack = SlotUtil.getStack(slot);
            newStack = originalStack.copy();
            if (index < 36) {
                if (!this.callInsertItem(originalStack, 36, 37, false)) {
                    return ItemStackUtil.empty();
                }
            } else if (index == 37) {
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
}
