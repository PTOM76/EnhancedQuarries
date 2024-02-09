package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.Tiles;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import ml.pkom.enhancedquarries.tile.base.BuilderTile;
import ml.pkom.enhancedquarries.tile.base.FillerTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class NormalBuilderTile extends BuilderTile {

    public NormalBuilderTile(TileCreateEvent event) {
        this(Tiles.NORMAL_BUILDER_TILE.getOrNull(), event);
    }

    public NormalBuilderTile(BlockEntityType<?> type, TileCreateEvent event) {
        super(type, event);
    }
}
