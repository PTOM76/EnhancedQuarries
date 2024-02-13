package net.pitan76.enhancedquarries.compat;

import ml.pkom.mcpitanlibarch.api.event.registry.RegistryResult;
import net.pitan76.enhancedquarries.tile.base.BaseEnergyTile;
import net.minecraft.block.entity.BlockEntityType;
import net.pitan76.enhancedquarries.Tiles;
import team.reborn.energy.api.EnergyStorage;

import java.util.ArrayList;
import java.util.List;

public class RebornEnergyRegister {

    public static void init() {
        List<RegistryResult<BlockEntityType<?>>> tileTypes = new ArrayList<>();

        tileTypes.add(Tiles.NORMAL_QUARRY_TILE);
        tileTypes.add(Tiles.OPTIMUM_QUARRY_TILE);
        tileTypes.add(Tiles.ENHANCED_QUARRY_TILE);
        tileTypes.add(Tiles.ENHANCED_OPTIMUM_QUARRY_TILE);
        tileTypes.add(Tiles.ENHANCED_FILLER_TILE);
        tileTypes.add(Tiles.ENHANCED_FILLER_WITH_CHEST_TILE);
        tileTypes.add(Tiles.NORMAL_FILLER_TILE);
        tileTypes.add(Tiles.NORMAL_BUILDER_TILE);
        tileTypes.add(Tiles.NORMAL_SCANNER_TILE);
        tileTypes.add(Tiles.NORMAL_PUMP_TILE);
        tileTypes.add(Tiles.ENHANCED_PUMP_TILE);
        tileTypes.add(Tiles.FLUID_QUARRY_TILE);
        tileTypes.add(Tiles.FLUID_OPTIMUM_QUARRY_TILE);
        tileTypes.add(Tiles.ENERGY_GENERATOR_TILE);

        for (RegistryResult<BlockEntityType<?>> registryEvent : tileTypes) {
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
