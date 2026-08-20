package net.pitan76.enhancedquarries.neoforge.compat;

import net.minecraft.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.pitan76.enhancedquarries.Tiles;
import net.pitan76.enhancedquarries.tile.base.BaseEnergyTile;
import net.pitan76.mcpitanlib.api.registry.result.RegistryResult;

import java.util.ArrayList;
import java.util.List;

/**
 * NeoForge の新キャパビリティAPIで BaseEnergyTile を FE として公開する。
 */
public class FEEnergyRegister {

    public static void register(RegisterCapabilitiesEvent event) {
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

        for (RegistryResult<BlockEntityType<?>> result : tileTypes) {
            BlockEntityType<?> type = result.getOrNull();
            if (type == null) continue;

            event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, type, (blockEntity, dir) -> {
                if (!(blockEntity instanceof BaseEnergyTile)) return null;

                BaseEnergyTile tile = (BaseEnergyTile) blockEntity;
                if (!tile.hasEnergyStorage() || !(tile.getEnergyStorage() instanceof FEEnergyStorage))
                    tile.setEnergyStorage(new FEEnergyStorage(tile));

                return (FEEnergyStorage) tile.getEnergyStorage();
            });
        }
    }
}
