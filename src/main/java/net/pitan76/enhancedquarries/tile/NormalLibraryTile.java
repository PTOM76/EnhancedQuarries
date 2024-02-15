package net.pitan76.enhancedquarries.tile;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.pitan76.enhancedquarries.Tiles;
import net.pitan76.enhancedquarries.tile.base.LibraryTile;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;

public class NormalLibraryTile extends LibraryTile {

    public NormalLibraryTile(BlockPos pos, BlockState state) {
        this(Tiles.NORMAL_LIBRARY_TILE.getOrNull(), new TileCreateEvent(pos, state));
    }

    public NormalLibraryTile(BlockView world) {
        this(Tiles.NORMAL_LIBRARY_TILE.getOrNull(), new TileCreateEvent(world));
    }

    public NormalLibraryTile(TileCreateEvent e) {
        this(Tiles.NORMAL_LIBRARY_TILE.getOrNull(), e);
    }

    public NormalLibraryTile(BlockEntityType<?> blockEntityType, TileCreateEvent e) {
        super(blockEntityType, e);
    }
}
