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

        ItemStack stack = inventory.getStack(slot);
        if (stack.isEmpty()) return 0;

        ItemStack remainder = ItemHandlerHelper.insertItemStacked(handler, stack.copy(), false);
        int moved = stack.getCount() - remainder.getCount();
        if (moved <= 0) return 0;

        stack.decrement(moved);
        inventory.markDirty();
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
