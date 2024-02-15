package net.pitan76.enhancedquarries.tile;

import net.minecraft.block.entity.BlockEntityType;
import net.pitan76.enhancedquarries.Tiles;
import net.pitan76.enhancedquarries.tile.base.PumpTile;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;

public class NormalPumpTile extends PumpTile {

    public NormalPumpTile(BlockEntityType<?> type, TileCreateEvent event) {
        super(type, event);
    }

    public NormalPumpTile(TileCreateEvent event) {
        this(Tiles.NORMAL_PUMP_TILE.getOrNull(), event);
    }
}
