package net.pitan76.enhancedquarries.tile;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.pitan76.enhancedquarries.Tiles;
import net.pitan76.enhancedquarries.screen.FillerWithChestScreenHandler;
import net.pitan76.enhancedquarries.util.EQStorageBoxUtil;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.gui.args.CreateMenuEvent;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import net.pitan76.mcpitanlib.api.util.entity.ItemEntityUtil;
import net.pitan76.mcpitanlib.api.util.world.ServerWorldUtil;
import net.pitan76.mcpitanlib.midohra.util.math.BlockPos;
import net.pitan76.mcpitanlib.midohra.world.World;
import net.pitan76.storagebox.api.StorageBoxUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnhancedFillerWithChestTile extends EnhancedFillerTile {

    public EnhancedFillerWithChestTile(BlockEntityType<?> type, TileCreateEvent e) {
        super(type, e);
    }

    public EnhancedFillerWithChestTile(TileCreateEvent event) {
        this(Tiles.ENHANCED_FILLER_WITH_CHEST_TILE.getOrNull(), event);
    }

    @Override
    public int getInvSize() {
        return 54;
    }

    @Override
    public ItemStack getInventoryStack() {
        int size = getItems().size();
        for (int i = 0; i < size; i++) {
            ItemStack stack = getItems().get(i);
            if (i >= 26)
                break;
            latestGotStack = stack;
            if (ItemStackUtil.isEmpty(stack)) continue;
            if (ItemStackUtil.getItem(stack) instanceof BlockItem) return stack;
            // StorageBox
            if (EQStorageBoxUtil.isStorageBox(stack)) {
                ItemStack stackInBox = StorageBoxUtil.getStackInStorageBox(stack);
                if (stackInBox == null) continue;

                if (ItemStackUtil.getItem(stackInBox) instanceof BlockItem) return stackInBox;
            }
            // ---- StorageBox
        }
        return ItemStackUtil.empty();
    }

    @Override
    public boolean tryBreaking(BlockPos procPos) {
        World world = getWorldM();
        List<ItemStack> drops = ServerWorldUtil.getDroppedStacksOnBlock(world.getBlockState(procPos).toMinecraft(), world.toServerWorld().get().getRaw(), procPos.toRaw(), world.getBlockEntity(procPos));
        if (WorldUtil.breakBlock(callGetWorld(), procPos, false)) {
            drops.forEach(this::addStack);
            return true;
        }
        return false;
    }

    public void addStack(ItemStack stack) {
        World world = getWorldM();
        if (world.getRaw() == null || world.isClient()) return;

        int size = getItems().size();
        for (int i = 27; i < size; i++) {
            if (stack.isEmpty() || stack.getCount() == 0) return;
            if (getItems().get(i).isEmpty()) {
                getItems().set(i, stack);
                return;
            }
            ItemStack inStack = getItems().get(i);
            if (stack.getItem().equals(inStack.getItem()) && (ItemStackUtil.areNbtOrComponentEqual(stack, inStack) || !ItemStackUtil.hasNbtOrComponent(stack) == !ItemStackUtil.hasNbtOrComponent(inStack)) && inStack.getItem().getMaxCount() != 1) {
                int originInCount = getItems().get(i).getCount();
                getItems().get(i).setCount(Math.min(ItemStackUtil.getMaxCount(stack), ItemStackUtil.getCount(stack) + originInCount));
                if (ItemStackUtil.getMaxCount(stack) >= ItemStackUtil.getCount(stack) + originInCount) {
                    return;
                }
                ItemStackUtil.setCount(stack, ItemStackUtil.getCount(stack) + originInCount - ItemStackUtil.getMaxCount(stack));
            }
        }

        ItemEntityUtil.createWithSpawn(world.getRaw(), stack, callGetPos());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(CreateMenuEvent e) {
        return new FillerWithChestScreenHandler(e.syncId, e.playerInventory, this, craftingInventory.toInventory());
    }
}
