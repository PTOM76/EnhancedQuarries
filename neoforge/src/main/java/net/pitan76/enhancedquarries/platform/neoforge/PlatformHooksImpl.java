package net.pitan76.enhancedquarries.platform.neoforge;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.pitan76.mcpitanlib.api.util.InventoryUtil;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.midohra.util.math.BlockPos;
import net.pitan76.mcpitanlib.midohra.util.math.Direction;

public class PlatformHooksImpl {

    // 登録は RegisterCapabilitiesEvent 中でしか行えないので EnhancedQuarriesNeoForge 側で行う
    public static void registerEnergyStorage() {
    }

    public static long bucketAmount() {
        return FluidType.BUCKET_VOLUME;
    }

    public static long moveToNeighbor(World world, BlockPos pos, Inventory inventory, int slot, Direction dir) {
        IItemHandler handler = world.getCapability(
                Capabilities.ItemHandler.BLOCK,
                pos.offset(dir).toMinecraft(),
                dir.getOpposite().toMinecraft());
        if (handler == null) return 0;

        ItemStack stack = InventoryUtil.getStack(inventory, slot);
        if (ItemStackUtil.isEmpty(stack)) return 0;

        ItemStack remainder = ItemHandlerHelper.insertItemStacked(handler, ItemStackUtil.copy(stack), false);
        int moved = ItemStackUtil.getCount(stack) - ItemStackUtil.getCount(remainder);
        if (moved <= 0) return 0;

        ItemStackUtil.decrementCount(stack, moved);
        InventoryUtil.markDirty(inventory);
        return moved;
    }

    public static boolean addEnergyToForeignTile(BlockEntity blockEntity, long amount) {
        World world = blockEntity.getWorld();
        if (world == null) return false;

        IEnergyStorage storage = world.getCapability(
                Capabilities.EnergyStorage.BLOCK, blockEntity.getPos(), null);
        if (storage == null || !storage.canReceive()) return false;

        return storage.receiveEnergy((int) Math.min(amount, Integer.MAX_VALUE), false) > 0;
    }
}
