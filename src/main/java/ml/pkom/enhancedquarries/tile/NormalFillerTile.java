package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.Tiles;
import ml.pkom.enhancedquarries.tile.base.FillerTile;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import net.minecraft.block.entity.BlockEntityType;

public class NormalFillerTile extends FillerTile {

    public NormalFillerTile(BlockEntityType<?> type, TileCreateEvent event) {
        super(type, event);
    }

    public NormalFillerTile(TileCreateEvent event) {
        this(Tiles.NORMAL_FILLER_TILE.getOrNull(), event);
    }
}
