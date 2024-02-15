package net.pitan76.enhancedquarries.tile;

import net.minecraft.block.entity.BlockEntityType;
import net.pitan76.enhancedquarries.Tiles;
import net.pitan76.enhancedquarries.tile.base.FillerTile;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;

public class NormalFillerTile extends FillerTile {

    public NormalFillerTile(BlockEntityType<?> type, TileCreateEvent event) {
        super(type, event);
    }

    public NormalFillerTile(TileCreateEvent event) {
        this(Tiles.NORMAL_FILLER_TILE.getOrNull(), event);
    }
}
