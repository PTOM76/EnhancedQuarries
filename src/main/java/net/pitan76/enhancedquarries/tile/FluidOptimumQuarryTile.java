package net.pitan76.enhancedquarries.tile;

import net.pitan76.enhancedquarries.Tiles;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import net.minecraft.block.entity.BlockEntityType;

public class FluidOptimumQuarryTile extends EnhancedOptimumQuarryTile {

    // 継承のため
    public FluidOptimumQuarryTile(BlockEntityType<?> type, TileCreateEvent event) {
        super(type, event);
    }

    public FluidOptimumQuarryTile(TileCreateEvent event) {
        this(Tiles.FLUID_OPTIMUM_QUARRY_TILE.getOrNull(), event);
    }

    @Override
    public boolean canReplaceFluid() {
        return true;
    }
}
