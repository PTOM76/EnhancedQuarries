package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.Tiles;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import ml.pkom.enhancedquarries.tile.base.BuilderTile;
import ml.pkom.enhancedquarries.tile.base.FillerTile;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class NormalBuilderTile extends BuilderTile {

    public NormalBuilderTile(BlockPos pos, BlockState state) {
        super(Tiles.NORMAL_BUILDER_TILE.getOrNull(), pos, state);
    }

    public NormalBuilderTile(TileCreateEvent event) {
        this(event.getBlockPos(), event.getBlockState());
    }
}
