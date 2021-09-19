package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.event.TileCreateEvent;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class FluidQuarryTile extends EnhancedQuarryTile {

    public FluidQuarryTile(BlockPos pos, BlockState state) {
        super(Tiles.FLUID_QUARRY_TILE, pos, state);
    }

    // 継承のため
    public FluidQuarryTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public FluidQuarryTile(TileCreateEvent event) {
        this(event.getBlockPos(), event.getBlockState());
    }

    @Override
    public boolean canReplaceFluid() {
        return true;
    }
}
