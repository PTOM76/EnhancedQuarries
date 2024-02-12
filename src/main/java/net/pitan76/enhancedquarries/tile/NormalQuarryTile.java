package net.pitan76.enhancedquarries.tile;

import net.pitan76.enhancedquarries.Tiles;
import net.pitan76.enhancedquarries.tile.base.QuarryTile;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import net.minecraft.block.entity.BlockEntityType;

public class NormalQuarryTile extends QuarryTile {

    public NormalQuarryTile(BlockEntityType<?> type, TileCreateEvent event) {
        super(type, event);
    }

    public NormalQuarryTile(TileCreateEvent event) {
        this(Tiles.NORMAL_QUARRY_TILE.getOrNull(), event);
    }
}
