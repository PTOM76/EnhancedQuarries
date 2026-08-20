package net.pitan76.enhancedquarries.platform.fabric;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.world.World;
import net.pitan76.enhancedquarries.CompatUtil;
import net.pitan76.enhancedquarries.fabric.compat.RebornEnergyRegister;
import net.pitan76.mcpitanlib.api.util.PlatformUtil;
import net.pitan76.mcpitanlib.midohra.util.math.BlockPos;
import net.pitan76.mcpitanlib.midohra.util.math.Direction;

public class PlatformHooksImpl {

    public static void registerEnergyStorage() {
        if (PlatformUtil.isModLoaded("team_reborn_energy")) {
            RebornEnergyRegister.init();
        }
    }

    public static long bucketAmount() {
        return FluidConstants.BUCKET;
    }

    public static long moveToNeighbor(World world, BlockPos pos, Inventory inventory, int slot, Direction dir) {
        return StorageUtil.move(
                InventoryStorage.of(inventory, null).getSlot(slot),
                ItemStorage.SIDED.find(world, pos.offset(dir).toMinecraft(), dir.getOpposite().toMinecraft()),
                (iv) -> true,
                Long.MAX_VALUE, null);
    }

    public static boolean addEnergyToForeignTile(BlockEntity blockEntity, long amount) {
        if (!CompatUtil.isUsableRebornCore()) return false;
        if (!(blockEntity instanceof reborncore.common.powerSystem.PowerAcceptorBlockEntity)) return false;

        ((reborncore.common.powerSystem.PowerAcceptorBlockEntity) blockEntity).addEnergy(amount);
        return true;
    }
}
