package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.Tiles;
import ml.pkom.enhancedquarries.tile.base.LibraryTile;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

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
