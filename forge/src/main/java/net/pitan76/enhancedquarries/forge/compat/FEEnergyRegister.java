package net.pitan76.enhancedquarries.forge.compat;

import net.minecraft.util.math.Direction;
import net.minecraft.util.Identifier;
import net.minecraft.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.common.MinecraftForge;
import net.pitan76.enhancedquarries.EnhancedQuarries;
import net.pitan76.enhancedquarries.tile.base.BaseEnergyTile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FEEnergyRegister {

    public static void init() {
        MinecraftForge.EVENT_BUS.addGenericListener(BlockEntity.class, FEEnergyRegister::onAttach);
    }

    private static void onAttach(AttachCapabilitiesEvent<BlockEntity> event) {
        BlockEntity blockEntity = event.getObject();
        if (!(blockEntity instanceof BaseEnergyTile)) return;

        BaseEnergyTile tile = (BaseEnergyTile) blockEntity;
        event.addCapability(new Identifier(EnhancedQuarries.MOD_ID, "energy"), new Provider(tile));
    }

    private static class Provider implements ICapabilityProvider {
        private final BaseEnergyTile tile;
        private LazyOptional<net.minecraftforge.energy.IEnergyStorage> holder;

        private Provider(BaseEnergyTile tile) {
            this.tile = tile;
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            if (cap != ForgeCapabilities.ENERGY) return LazyOptional.empty();

            if (holder == null) {
                if (!tile.hasEnergyStorage() || !(tile.getEnergyStorage() instanceof FEEnergyStorage))
                    tile.setEnergyStorage(new FEEnergyStorage(tile));

                FEEnergyStorage storage = (FEEnergyStorage) tile.getEnergyStorage();
                holder = LazyOptional.of(() -> storage);
            }
            return holder.cast();
        }
    }
}
