package net.pitan76.enhancedquarries.tile.base;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.pitan76.enhancedquarries.screen.LibraryScreenHandler;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.event.container.factory.DisplayNameArgs;
import net.pitan76.mcpitanlib.api.event.nbt.ReadNbtArgs;
import net.pitan76.mcpitanlib.api.event.nbt.WriteNbtArgs;
import net.pitan76.mcpitanlib.api.gui.args.CreateMenuEvent;
import net.pitan76.mcpitanlib.api.gui.inventory.IInventory;
import net.pitan76.mcpitanlib.api.gui.v2.SimpleScreenHandlerFactory;
import net.pitan76.mcpitanlib.api.tile.CompatBlockEntity;
import net.pitan76.mcpitanlib.api.util.InventoryUtil;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.TextUtil;
import net.pitan76.mcpitanlib.api.util.collection.ItemStackList;
import org.jetbrains.annotations.Nullable;

public class LibraryTile extends CompatBlockEntity implements IInventory, SimpleScreenHandlerFactory {
    public ItemStackList inventory = ItemStackList.ofSize(4, ItemStackUtil.empty());

    public ItemStackList getInventory() {
        return inventory;
    }

    @Override
    public void writeNbt(WriteNbtArgs args) {
        InventoryUtil.writeNbt(args, inventory);
    }

    @Override
    public void readNbt(ReadNbtArgs args) {
        InventoryUtil.readNbt(args, inventory);
    }

    public LibraryTile(BlockEntityType<?> type, TileCreateEvent e) {
        super(type, e);
    }

    @Override
    public ItemStackList getItems() {
        return inventory;
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(CreateMenuEvent e) {
        return new LibraryScreenHandler(e.syncId, e.playerInventory, this);
    }

    @Override
    public Text getDisplayName(DisplayNameArgs args) {
        return TextUtil.translatable("screen.enhanced_quarries.library.title");
    }
}
