package net.pitan76.enhancedquarries.platform.fabric;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.pitan76.enhancedquarries.tile.base.PumpTile;
import net.pitan76.mcpitanlib.api.transfer.fluid.v1.IFluidStorage;
import net.pitan76.mcpitanlib.api.transfer.fluid.v1.fabric.FabricFluidStorage;

public class FluidStorageHooksImpl {

    public static void registerPumpFluidStorage(BlockEntityType<?> type) {
        register(type);
    }

    private static <T extends BlockEntity> void register(BlockEntityType<T> type) {
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, dir) -> {
            if (!(blockEntity instanceof PumpTile)) return null;

            IFluidStorage storage = ((PumpTile) blockEntity).getStoredFluid();
            if (!(storage instanceof FabricFluidStorage)) return null;

            return ((FabricFluidStorage) storage).storage;
        }, type);
    }
}
