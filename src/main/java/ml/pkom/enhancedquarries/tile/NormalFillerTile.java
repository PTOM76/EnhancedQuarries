package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.event.TileCreateEvent;
import ml.pkom.enhancedquarries.tile.base.FillerTile;
import ml.pkom.enhancedquarries.tile.base.QuarryTile;

public class NormalFillerTile extends FillerTile {

    public NormalFillerTile() {
        super(Tiles.NORMAL_FILLER_TILE);
    }

    public NormalFillerTile(TileCreateEvent event) {
        this();
    }
}
