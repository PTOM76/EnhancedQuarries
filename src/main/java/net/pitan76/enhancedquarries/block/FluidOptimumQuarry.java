package net.pitan76.enhancedquarries.block;

import net.pitan76.enhancedquarries.block.base.Quarry;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import net.pitan76.enhancedquarries.tile.FluidOptimumQuarryTile;
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
