package net.pitan76.enhancedquarries.platform.forge;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.pitan76.enhancedquarries.EnhancedQuarries;
import net.pitan76.enhancedquarries.tile.base.PumpTile;
import net.pitan76.mcpitanlib.api.transfer.fluid.v1.IFluidStorage;
import net.pitan76.mcpitanlib.api.transfer.fluid.v1.forge.ForgeFluidStorage;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@EventBusSubscriber(modid = EnhancedQuarries.MOD_ID)
public class FluidStorageHooksImpl {

    private static final Set<BlockEntityType<?>> types = new CopyOnWriteArraySet<>();

    public static void registerPumpFluidStorage(BlockEntityType<?> type) {
        types.add(type);
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
        if (types.isEmpty()) return;

        BlockEntity blockEntity = event.getObject();
        if (!(blockEntity instanceof PumpTile)) return;
        if (!types.contains(blockEntity.getType())) return;

        event.addCapability(CompatIdentifier.of(EnhancedQuarries.MOD_ID, "fluid").toMinecraft(), new ICapabilityProvider() {
            @NotNull
            @Override
            public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
                if (capability != ForgeCapabilities.FLUID_HANDLER) return LazyOptional.empty();

                IFluidStorage storage = ((PumpTile) blockEntity).getStoredFluid();
                if (!(storage instanceof ForgeFluidStorage)) return LazyOptional.empty();

                return LazyOptional.of(() -> ((ForgeFluidStorage) storage).storage).cast();
            }
        });
    }
}
