package net.pitan76.enhancedquarries.tile;

import ml.pkom.storagebox.StorageBoxItem;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.pitan76.enhancedquarries.Tiles;
import net.pitan76.enhancedquarries.screen.FillerWithChestScreenHandler;
import net.pitan76.enhancedquarries.tile.base.FillerTile;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnhancedFillerWithChestTile extends EnhancedFillerTile {

    public DefaultedList<ItemStack> invItems = DefaultedList.ofSize(54, ItemStack.EMPTY);
    
    @Override
    public DefaultedList<ItemStack> getItems() {
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
            if (FillerTile.isStorageBox(stack)) {
                ItemStack itemInBox = StorageBoxItem.getStackInStorageBox(stack);
                if (itemInBox == null) continue;

                if (itemInBox.getItem() instanceof BlockItem) return itemInBox;
            }
            // ---- StorageBox
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean tryBreaking(BlockPos procPos) {
        List<ItemStack> drops = Block.getDroppedStacks(getWorld().getBlockState(procPos), (ServerWorld) getWorld(), procPos, getWorld().getBlockEntity(procPos));
        if (getWorld().breakBlock(procPos, false)) {
            drops.forEach(this::addStack);
            return true;
        }
        return false;
    }

    public void addStack(ItemStack stack) {
        if (getWorld() == null || getWorld().isClient()) return;
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
                getItems().get(index).setCount(Math.min(stack.getMaxCount(), stack.getCount() + originInCount));
                if (stack.getMaxCount() >= stack.getCount() + originInCount) {
                    return;
                }
                stack.setCount(stack.getCount() + originInCount - stack.getMaxCount());
            }
        }
        getWorld().spawnEntity(new ItemEntity(getWorld(), getPos().getX(), getPos().getY(), getPos().getZ(), stack));
    }

    @Override
    public void init() {
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new FillerWithChestScreenHandler(syncId, playerInventory, this, getCraftingInventory());
    }
}
