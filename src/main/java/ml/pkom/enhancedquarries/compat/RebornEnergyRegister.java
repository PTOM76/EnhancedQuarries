package ml.pkom.enhancedquarries.compat;

import ml.pkom.enhancedquarries.tile.base.BaseEnergyTile;
import ml.pkom.mcpitanlibarch.api.event.registry.RegistryEvent;
import net.minecraft.block.entity.BlockEntityType;
import team.reborn.energy.api.EnergyStorage;

import java.util.ArrayList;
import java.util.List;

import static ml.pkom.enhancedquarries.Tiles.*;

public class RebornEnergyRegister {

    public static void init() {
        List<RegistryEvent<BlockEntityType<?>>> tileTypes = new ArrayList<>();

        tileTypes.add(NORMAL_QUARRY_TILE);
        tileTypes.add(OPTIMUM_QUARRY_TILE);
        tileTypes.add(ENHANCED_QUARRY_TILE);
        tileTypes.add(ENHANCED_OPTIMUM_QUARRY_TILE);
        tileTypes.add(ENHANCED_FILLER_TILE);
        tileTypes.add(ENHANCED_FILLER_WITH_CHEST_TILE);
        tileTypes.add(NORMAL_FILLER_TILE);
        tileTypes.add(NORMAL_BUILDER_TILE);
        tileTypes.add(NORMAL_SCANNER_TILE);
        tileTypes.add(NORMAL_PUMP_TILE);
        tileTypes.add(ENHANCED_PUMP_TILE);
        tileTypes.add(FLUID_QUARRY_TILE);
        tileTypes.add(FLUID_OPTIMUM_QUARRY_TILE);
        tileTypes.add(ENERGY_GENERATOR_TILE);

        for (RegistryEvent<BlockEntityType<?>> registryEvent : tileTypes) {
            EnergyStorage.SIDED.registerForBlockEntity((blockEntity, dir) -> {
                if (!(blockEntity instanceof BaseEnergyTile)) return null;
                BaseEnergyTile tile = (BaseEnergyTile) blockEntity;
                if (!tile.hasEnergyStorage())
                    tile.setEnergyStorage(new TREnergyStorage(tile));

                if (tile.getEnergyStorage() instanceof TREnergyStorage)
                    return (TREnergyStorage) tile.getEnergyStorage();

                return null;
            }, registryEvent.getOrNull());
        }
    }
}
