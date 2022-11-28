package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.Tiles;
import ml.pkom.enhancedquarries.event.TileCreateEvent;
import ml.pkom.enhancedquarries.tile.base.PumpTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class NormalPumpTile extends PumpTile {

    public NormalPumpTile(BlockPos pos, BlockState state) {
        this(Tiles.NORMAL_PUMP_TILE, pos, state);
    }

    public NormalPumpTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public NormalPumpTile(TileCreateEvent event) {
        this(event.getBlockPos(), event.getBlockState());
    }
}
