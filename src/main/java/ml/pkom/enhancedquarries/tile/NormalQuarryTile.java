package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.Tiles;
import ml.pkom.enhancedquarries.event.TileCreateEvent;
import ml.pkom.enhancedquarries.tile.base.QuarryTile;
import net.minecraft.block.entity.BlockEntityType;

public class NormalQuarryTile extends QuarryTile {

    public NormalQuarryTile() {
        super(Tiles.NORMAL_QUARRY_TILE);
    }

    public NormalQuarryTile(BlockEntityType<?> type) {
        super(type);
    }

    public NormalQuarryTile(TileCreateEvent event) {
        this();
    }
}
