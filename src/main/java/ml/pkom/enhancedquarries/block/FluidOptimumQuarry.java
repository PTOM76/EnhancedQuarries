package ml.pkom.enhancedquarries.block;

import ml.pkom.enhancedquarries.block.base.Quarry;
import ml.pkom.enhancedquarries.event.TileCreateEvent;
import ml.pkom.enhancedquarries.tile.EnhancedOptimumQuarryTile;
import ml.pkom.enhancedquarries.tile.FluidOptimumQuarryTile;
import net.minecraft.block.entity.BlockEntity;

public class FluidOptimumQuarry extends Quarry {

    public FluidOptimumQuarry() {
        super();
    }

    // instance
    public static Quarry INSTANCE = new FluidOptimumQuarry();

    public static Quarry getInstance() {
        return INSTANCE;
    }

    public static Quarry getQuarry() {
        return getInstance();
    }
    // ----

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new FluidOptimumQuarryTile(event);
    }

}
