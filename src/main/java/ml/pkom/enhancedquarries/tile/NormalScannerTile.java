package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.Tiles;
import ml.pkom.enhancedquarries.tile.base.ScannerTile;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import net.minecraft.block.entity.BlockEntityType;

public class NormalScannerTile extends ScannerTile {

    public NormalScannerTile(BlockEntityType<?> type, TileCreateEvent event) {
        super(type, event);
    }

    public NormalScannerTile(TileCreateEvent event) {
        this(Tiles.NORMAL_SCANNER_TILE.getOrNull(), event);
    }
}
