package net.pitan76.enhancedquarries.block;

import net.minecraft.block.entity.BlockEntity;
import net.pitan76.enhancedquarries.block.base.Pump;
import net.pitan76.enhancedquarries.tile.NormalPumpTile;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;

public class NormalPump extends Pump {

    public NormalPump() {
        super();
    }

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new NormalPumpTile(event);
    }

    // instance
    public static Pump INSTANCE = new NormalPump();

    public static Pump getInstance() {
        return INSTANCE;
    }

    public static Pump getPump() {
        return getInstance();
    }
    // ----
}
