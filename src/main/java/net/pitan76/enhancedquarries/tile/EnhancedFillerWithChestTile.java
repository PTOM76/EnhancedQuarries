package net.pitan76.enhancedquarries.tile;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.pitan76.enhancedquarries.Tiles;
import net.pitan76.enhancedquarries.screen.FillerWithChestScreenHandler;
import net.pitan76.enhancedquarries.util.EQStorageBoxUtil;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.gui.args.CreateMenuEvent;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import net.pitan76.mcpitanlib.api.util.collection.ItemStackList;
import net.pitan76.mcpitanlib.api.util.entity.ItemEntityUtil;
import net.pitan76.mcpitanlib.api.util.world.ServerWorldUtil;
import net.pitan76.storagebox.api.StorageBoxUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnhancedFillerWithChestTile extends EnhancedFillerTile {

    public ItemStackList invItems = ItemStackList.ofSize(54, ItemStackUtil.empty());
    
    @Override
    public ItemStackList getItems() {
        return invItems;
    }

    public EnhancedFillerWithChestTile(BlockEntityType<?> type, TileCreateEvent event) {
        super(type, event);
    }

    public EnhancedFillerWithChestTile(TileCreateEvent event) {
        this(Tiles.ENHANCED_FILLER_WITH_CHEST_TILE.getOrNull(), event);
    }

    @Override
    public ItemStack getInventoryStack() {
        int i = 0;
        for (ItemStack stack : getItems()) {
            i++;
            if (i >= 27)
                break;
            latestGotStack = stack;
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof BlockItem) return stack;
            // StorageBox
            if (EQStorageBoxUtil.isStorageBox(stack)) {
                ItemStack itemInBox = StorageBoxUtil.getStackInStorageBox(stack);
                if (itemInBox == null) continue;

                if (itemInBox.getItem() instanceof BlockItem) return itemInBox;
            }
            // ---- StorageBox
        }
        return ItemStackUtil.empty();
    }

    @Override
    public boolean tryBreaking(BlockPos procPos) {
        List<ItemStack> drops = ServerWorldUtil.getDroppedStacksOnBlock(WorldUtil.getBlockState(callGetWorld(), procPos), (ServerWorld) callGetWorld(), procPos, WorldUtil.getBlockEntity(callGetWorld(), procPos));
        if (WorldUtil.breakBlock(callGetWorld(), procPos, false)) {
            drops.forEach(this::addStack);
            return true;
        }
        return false;
    }

    public void addStack(ItemStack stack) {
        if (callGetWorld() == null || callGetWorld().isClient()) return;
        int index = 27;
        for (;index < getItems().size();index++) {
            if (stack.isEmpty() || stack.getCount() == 0) return;
            if (getItems().get(index).isEmpty()) {
                getItems().set(index, stack);
                return;
            }
            ItemStack inStack = getItems().get(index);
            if (stack.getItem().equals(inStack.getItem()) && (ItemStackUtil.areNbtOrComponentEqual(stack, inStack) || !ItemStackUtil.hasNbtOrComponent(stack) == !ItemStackUtil.hasNbtOrComponent(inStack)) && inStack.getItem().getMaxCount() != 1) {
                int originInCount = getItems().get(index).getCount();
                getItems().get(index).setCount(Math.min(ItemStackUtil.getMaxCount(stack), ItemStackUtil.getCount(stack) + originInCount));
                if (ItemStackUtil.getMaxCount(stack) >= ItemStackUtil.getCount(stack) + originInCount) {
                    return;
                }
                ItemStackUtil.setCount(stack, ItemStackUtil.getCount(stack) + originInCount - ItemStackUtil.getMaxCount(stack));
            }
        }
        ItemEntity itemEntity = ItemEntityUtil.create(callGetWorld(), callGetPos(), stack);
        WorldUtil.spawnEntity(callGetWorld(), itemEntity);
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(CreateMenuEvent e) {
        return new FillerWithChestScreenHandler(e.syncId, e.playerInventory, this, getCraftingInventory());
    }
}
