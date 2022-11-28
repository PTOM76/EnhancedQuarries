package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.Tiles;
import ml.pkom.enhancedquarries.event.TileCreateEvent;
import ml.pkom.enhancedquarries.tile.base.FillerTile;
import ml.pkom.enhancedquarries.tile.base.ScannerTile;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class NormalScannerTile extends ScannerTile {

    public NormalScannerTile(BlockPos pos, BlockState state) {
        super(Tiles.NORMAL_SCANNER_TILE, pos, state);
    }

    public NormalScannerTile(TileCreateEvent event) {
        this(event.getBlockPos(), event.getBlockState());
    }
}
