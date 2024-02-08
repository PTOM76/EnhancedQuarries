package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.Tiles;
import ml.pkom.enhancedquarries.screen.FillerWithChestScreenHandler;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import ml.pkom.mcpitanlibarch.api.util.ItemStackUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnhancedFillerWithChestTile extends EnhancedFillerTile {

    public DefaultedList<ItemStack> invItems = DefaultedList.ofSize(54, ItemStack.EMPTY);
    
    @Override
    public DefaultedList<ItemStack> getItems() {
        return invItems;
    }

    public EnhancedFillerWithChestTile(BlockPos pos, BlockState state) {
        super(Tiles.ENHANCED_FILLER_WITH_CHEST_TILE.getOrNull(), pos, state);
    }

    public EnhancedFillerWithChestTile(BlockEntityType type,BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public EnhancedFillerWithChestTile(TileCreateEvent event) {
        this(event.getBlockPos(), event.getBlockState());
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
            if (isStorageBox(stack)) {
                NbtCompound tag = stack.getNbt();
                if (tag.contains("StorageItemData")) {
                    ItemStack itemInBox = ItemStack.fromNbt(tag.getCompound("StorageItemData"));
                    if (itemInBox.getItem() instanceof BlockItem) return itemInBox;
                }
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
            if (stack.getItem().equals(inStack.getItem()) && (ItemStackUtil.areNbtEqual(stack, inStack) || !stack.hasNbt() == !inStack.hasNbt()) && inStack.getItem().getMaxCount() != 1) {
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
        /*
        int index = 0;
        SlotConfiguration.SlotIO output = new SlotConfiguration.SlotIO(SlotConfiguration.ExtractConfig.OUTPUT);
        SlotConfiguration.SlotIO input = new SlotConfiguration.SlotIO(SlotConfiguration.ExtractConfig.INPUT);
        SlotConfiguration slotConfig = new SlotConfiguration(getItems());
        for (;index < 27;index++){
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.NORTH, input, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.SOUTH, input, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.EAST, input, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.WEST, input, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.UP, input, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.DOWN, output, index));
            markDirty();
        }
        for (index = 27;index < getItems().size();index++){
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.NORTH, output, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.SOUTH, output, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.EAST, output, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.WEST, output, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.UP, output, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.DOWN, output, index));
            markDirty();
        }
        ((MachineBaseBlockEntityAccessor) this).setSlotConfiguration(slotConfig);

         */
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new FillerWithChestScreenHandler(syncId, playerInventory, this, getCraftingInventory());
    }
}
