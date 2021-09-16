package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.event.TileCreateEvent;
import ml.pkom.enhancedquarries.tile.base.QuarryTile;

public class NormalQuarryTile extends QuarryTile {

    public NormalQuarryTile() {
        super(Tiles.NORMAL_QUARRY_TILE);
    }

    public NormalQuarryTile(TileCreateEvent event) {
        this();
    }
}
