package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.Tiles;
import ml.pkom.enhancedquarries.event.TileCreateEvent;
import ml.pkom.enhancedquarries.mixin.MachineBaseBlockEntityAccessor;
import ml.pkom.enhancedquarries.tile.base.FillerTile;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import reborncore.common.blockentity.SlotConfiguration;
import reborncore.common.util.RebornInventory;

import java.util.List;

public class EnhancedFillerWithChestTile extends EnhancedFillerTile {

    public RebornInventory<FillerTile> inventory = new RebornInventory<>(54, "FillerWithChestTile", 64, this);

    @Override
    public RebornInventory<FillerTile> getInventory() {
        return inventory;
    }

    public EnhancedFillerWithChestTile() {
        super(Tiles.ENHANCED_FILLER_WITH_CHEST_TILE);
    }

    public EnhancedFillerWithChestTile(BlockEntityType type) {
        super(type);
    }

    public EnhancedFillerWithChestTile(TileCreateEvent event) {
        this();
    }

    @Override
    public ItemStack getInventoryStack() {
        int i = 0;
        for (ItemStack stack : getInventory().getStacks()) {
            i++;
            if (i >= 27)
                break;
            latestGotStack = stack;
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof BlockItem) return stack;
            // StorageBox
            if (isStorageBox(stack)) {
                NbtCompound tag = stack.getTag();
                if (tag.contains("item")) {
                    ItemStack itemInBox = ItemStack.fromNbt(tag.getCompound("item"));
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
            drops.forEach((stack) -> {
                addStack(stack);
            });
            return true;
        }
        return false;
    }

    public void addStack(ItemStack stack) {
        if (getWorld() == null || getWorld().isClient()) return;
        int index = 27;
        for (;index < getInventory().size();index++) {
            if (stack.isEmpty() || stack.getCount() == 0) return;
            if (getInventory().getStack(index).isEmpty()) {
                getInventory().setStack(index, stack);
                return;
            }
            ItemStack inStack = getInventory().getStack(index);
            if (stack.getItem().equals(inStack.getItem()) && (ItemStack.areTagsEqual(stack, inStack) || !stack.hasTag() == !inStack.hasTag()) && inStack.getItem().getMaxCount() != 1) {
                int originInCount = getInventory().getStack(index).getCount();
                getInventory().getStack(index).setCount(Math.min(stack.getMaxCount(), stack.getCount() + originInCount));
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
        int index = 0;
        SlotConfiguration.SlotIO output = new SlotConfiguration.SlotIO(SlotConfiguration.ExtractConfig.OUTPUT);
        SlotConfiguration.SlotIO input = new SlotConfiguration.SlotIO(SlotConfiguration.ExtractConfig.INPUT);
        SlotConfiguration slotConfig = new SlotConfiguration(getInventory());
        for (;index < 27;index++){
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.NORTH, input, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.SOUTH, input, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.EAST, input, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.WEST, input, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.UP, input, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.DOWN, output, index));
            markDirty();
        }
        for (index = 27;index < getInventory().size();index++){
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.NORTH, output, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.SOUTH, output, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.EAST, output, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.WEST, output, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.UP, output, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.DOWN, output, index));
            markDirty();
        }
        ((MachineBaseBlockEntityAccessor) this).setSlotConfiguration(slotConfig);
    }
}
