package net.pitan76.enhancedquarries.tile;

import net.minecraft.block.entity.BlockEntityType;
import net.pitan76.enhancedquarries.Tiles;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;

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
