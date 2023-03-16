package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.Tiles;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import ml.pkom.enhancedquarries.tile.base.FillerTile;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class NormalFillerTile extends FillerTile {

    public NormalFillerTile(BlockPos pos, BlockState state) {
        super(Tiles.NORMAL_FILLER_TILE.getOrNull(), pos, state);
    }

    public NormalFillerTile(TileCreateEvent event) {
        this(event.getBlockPos(), event.getBlockState());
    }
}
