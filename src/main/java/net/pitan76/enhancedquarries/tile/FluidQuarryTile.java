package net.pitan76.enhancedquarries.tile;

import net.pitan76.enhancedquarries.Tiles;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import net.minecraft.block.entity.BlockEntityType;

public class FluidQuarryTile extends EnhancedQuarryTile {

    // 継承のため
    public FluidQuarryTile(BlockEntityType<?> type, TileCreateEvent event) {
        super(type, event);
    }

    public FluidQuarryTile(TileCreateEvent event) {
        this(Tiles.FLUID_QUARRY_TILE.getOrNull(), event);
    }

    @Override
    public boolean canReplaceFluid() {
        return true;
    }
}
