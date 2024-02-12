package net.pitan76.enhancedquarries.tile;

import net.pitan76.enhancedquarries.Tiles;
import net.pitan76.enhancedquarries.tile.base.ScannerTile;
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
