package net.pitan76.enhancedquarries.block;

import net.minecraft.block.entity.BlockEntity;
import net.pitan76.enhancedquarries.block.base.Quarry;
import net.pitan76.enhancedquarries.tile.NormalQuarryTile;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;

public class NormalQuarry extends Quarry {

    public NormalQuarry() {
        super();
    }

    // instance
    public static Quarry INSTANCE = new NormalQuarry();

    public static Quarry getInstance() {
        return INSTANCE;
    }

    public static Quarry getQuarry() {
        return getInstance();
    }
    // ----

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new NormalQuarryTile(event);
    }

}
