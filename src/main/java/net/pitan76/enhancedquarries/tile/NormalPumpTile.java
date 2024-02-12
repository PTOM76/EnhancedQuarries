package net.pitan76.enhancedquarries.tile;

import net.pitan76.enhancedquarries.Tiles;
import net.pitan76.enhancedquarries.tile.base.PumpTile;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import net.minecraft.block.entity.BlockEntityType;

public class NormalPumpTile extends PumpTile {

    public NormalPumpTile(BlockEntityType<?> type, TileCreateEvent event) {
        super(type, event);
    }

    public NormalPumpTile(TileCreateEvent event) {
        this(Tiles.NORMAL_PUMP_TILE.getOrNull(), event);
    }
}
