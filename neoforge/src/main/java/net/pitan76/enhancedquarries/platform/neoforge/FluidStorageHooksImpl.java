package net.pitan76.enhancedquarries.platform.neoforge;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.pitan76.enhancedquarries.EnhancedQuarries;
import net.pitan76.enhancedquarries.tile.base.PumpTile;
import net.pitan76.mcpitanlib.api.transfer.fluid.v1.IFluidStorage;
import net.pitan76.mcpitanlib.api.transfer.fluid.v1.neoforge.NeoForgeFluidStorage;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@EventBusSubscriber(modid = EnhancedQuarries.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class FluidStorageHooksImpl {

    private static final List<BlockEntityType<?>> types = new CopyOnWriteArrayList<>();

    public static void registerPumpFluidStorage(BlockEntityType<?> type) {
        types.add(type);
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        for (BlockEntityType<?> type : types) {
            register(event, type);
        }
    }

    private static <T extends BlockEntity> void register(RegisterCapabilitiesEvent event, BlockEntityType<T> type) {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, type, (blockEntity, direction) -> {
            if (!(blockEntity instanceof PumpTile)) return null;

            IFluidStorage storage = ((PumpTile) blockEntity).getStoredFluid();
            if (!(storage instanceof NeoForgeFluidStorage)) return null;

            return ((NeoForgeFluidStorage) storage).storage;
        });
    }
}
