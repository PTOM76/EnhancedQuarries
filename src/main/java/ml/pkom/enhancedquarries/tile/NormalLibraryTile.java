package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.Tiles;
import ml.pkom.enhancedquarries.event.TileCreateEvent;
import ml.pkom.enhancedquarries.tile.base.FillerTile;
import ml.pkom.enhancedquarries.tile.base.LibraryTile;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class NormalLibraryTile extends LibraryTile {

    public NormalLibraryTile(BlockPos pos, BlockState state) {
        super(Tiles.NORMAL_LIBRARY_TILE, pos, state);
    }

    public NormalLibraryTile(TileCreateEvent event) {
        this(event.getBlockPos(), event.getBlockState());
    }
}
