package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.Tiles;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class FluidOptimumQuarryTile extends EnhancedOptimumQuarryTile {

    public FluidOptimumQuarryTile(BlockPos pos, BlockState state) {
        super(Tiles.FLUID_OPTIMUM_QUARRY_TILE.getOrNull(), pos, state);
    }

    // 継承のため
    public FluidOptimumQuarryTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public FluidOptimumQuarryTile(TileCreateEvent event) {
        this(event.getBlockPos(), event.getBlockState());
    }

    @Override
    public boolean canReplaceFluid() {
        return true;
    }
}
