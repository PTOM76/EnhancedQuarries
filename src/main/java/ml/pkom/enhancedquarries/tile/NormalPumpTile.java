package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.Tiles;
import ml.pkom.enhancedquarries.tile.base.PumpTile;
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
