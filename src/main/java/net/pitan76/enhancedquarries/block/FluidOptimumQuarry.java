package net.pitan76.enhancedquarries.block;

import net.minecraft.block.entity.BlockEntity;
import net.pitan76.enhancedquarries.block.base.Quarry;
import net.pitan76.enhancedquarries.tile.FluidOptimumQuarryTile;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;

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
