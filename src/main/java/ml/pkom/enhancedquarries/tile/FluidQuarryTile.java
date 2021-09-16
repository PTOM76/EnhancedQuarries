package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.event.TileCreateEvent;
import net.minecraft.block.entity.BlockEntityType;

public class FluidQuarryTile extends EnhancedQuarryTile {

    public FluidQuarryTile() {
        super(Tiles.FLUID_QUARRY_TILE);
    }

    // 継承のため
    public FluidQuarryTile(BlockEntityType<?> type) {
        super(type);
    }

    public FluidQuarryTile(TileCreateEvent event) {
        this();
    }

    @Override
    public boolean canReplaceFluid() {
        return true;
    }
}
