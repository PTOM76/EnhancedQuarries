package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.Tiles;
import ml.pkom.enhancedquarries.tile.base.BuilderTile;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import net.minecraft.block.entity.BlockEntityType;

public class NormalBuilderTile extends BuilderTile {

    public NormalBuilderTile(TileCreateEvent event) {
        this(Tiles.NORMAL_BUILDER_TILE.getOrNull(), event);
    }

    public NormalBuilderTile(BlockEntityType<?> type, TileCreateEvent event) {
        super(type, event);
    }
}
