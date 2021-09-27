package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.Tiles;
import ml.pkom.enhancedquarries.event.TileCreateEvent;
import net.minecraft.block.entity.BlockEntityType;

public class FluidOptimumQuarryTile extends EnhancedOptimumQuarryTile {

    public FluidOptimumQuarryTile() {
        super(Tiles.FLUID_OPTIMUM_QUARRY_TILE);
    }

    // 継承のため
    public FluidOptimumQuarryTile(BlockEntityType<?> type) {
        super(type);
    }

    public FluidOptimumQuarryTile(TileCreateEvent event) {
        this();
    }

    @Override
    public boolean canReplaceFluid() {
        return true;
    }
}
