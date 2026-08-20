package net.pitan76.enhancedquarries.platform.forge;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.pitan76.enhancedquarries.forge.compat.FEEnergyRegister;
import net.pitan76.mcpitanlib.midohra.util.math.BlockPos;
import net.pitan76.mcpitanlib.midohra.util.math.Direction;

public class PlatformHooksImpl {

    public static void registerEnergyStorage() {
        FEEnergyRegister.init();
    }

    public static long bucketAmount() {
        return FluidType.BUCKET_VOLUME;
    }

    public static long moveToNeighbor(World world, BlockPos pos, Inventory inventory, int slot, Direction dir) {
        BlockEntity target = world.getBlockEntity(pos.offset(dir).toMinecraft());
        if (target == null) return 0;

        IItemHandler handler = target
                .getCapability(ForgeCapabilities.ITEM_HANDLER, dir.getOpposite().toMinecraft())
                .orElse(null);
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
        net.minecraftforge.energy.IEnergyStorage storage = blockEntity
                .getCapability(ForgeCapabilities.ENERGY)
                .orElse(null);
        if (storage == null || !storage.canReceive()) return false;

        return storage.receiveEnergy((int) Math.min(amount, Integer.MAX_VALUE), false) > 0;
    }
}
