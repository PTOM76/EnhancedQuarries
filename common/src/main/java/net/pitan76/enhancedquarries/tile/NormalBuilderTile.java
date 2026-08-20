package net.pitan76.enhancedquarries.tile;

import net.minecraft.block.entity.BlockEntityType;
import net.pitan76.enhancedquarries.Tiles;
import net.pitan76.enhancedquarries.tile.base.BuilderTile;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;

public class NormalBuilderTile extends BuilderTile {

    public NormalBuilderTile(TileCreateEvent event) {
        this(Tiles.NORMAL_BUILDER_TILE.getOrNull(), event);
    }

    public NormalBuilderTile(BlockEntityType<?> type, TileCreateEvent event) {
        super(type, event);
    }
}
