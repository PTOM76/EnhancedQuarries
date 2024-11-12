package net.pitan76.enhancedquarries.tile;

import net.minecraft.block.BlockState;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.pitan76.enhancedquarries.Tiles;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.tile.CompatBlockEntity;

public class MarkerTile extends CompatBlockEntity {
    public MarkerTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

    }

    public MarkerTile(BlockPos pos, BlockState state) {
        super(Tiles.NORMAL_MARKER.getOrNull(), pos, state);
    }

    public MarkerTile(TileCreateEvent event) {
        this(event.getBlockPos(), event.getBlockState());
    }


}
