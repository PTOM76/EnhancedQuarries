package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.Tiles;
import ml.pkom.enhancedquarries.event.TileCreateEvent;
import ml.pkom.enhancedquarries.tile.base.PumpTile;
import net.minecraft.block.entity.BlockEntityType;

public class NormalPumpTile extends PumpTile {

    public NormalPumpTile() {
        this(Tiles.NORMAL_PUMP);
    }

    public NormalPumpTile(BlockEntityType<?> type) {
        super(type);
    }

    public NormalPumpTile(TileCreateEvent event) {
        this();
    }
}
