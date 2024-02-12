package net.pitan76.enhancedquarries.block;

import net.pitan76.enhancedquarries.block.base.Quarry;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import net.pitan76.enhancedquarries.tile.FluidQuarryTile;
import net.minecraft.block.entity.BlockEntity;

public class FluidQuarry extends Quarry {

    public FluidQuarry() {
        super();
    }

    // instance
    public static Quarry INSTANCE = new FluidQuarry();

    public static Quarry getInstance() {
        return INSTANCE;
    }

    public static Quarry getQuarry() {
        return getInstance();
    }
    // ----

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new FluidQuarryTile(event);
    }

}
