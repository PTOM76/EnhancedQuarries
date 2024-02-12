package net.pitan76.enhancedquarries.tile;

import net.pitan76.enhancedquarries.Tiles;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class MarkerTile extends BlockEntity {
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
