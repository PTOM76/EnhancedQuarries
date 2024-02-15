package net.pitan76.enhancedquarries.block;

import net.minecraft.block.entity.BlockEntity;
import net.pitan76.enhancedquarries.block.base.Pump;
import net.pitan76.enhancedquarries.tile.EnhancedPumpTile;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;

public class EnhancedPump extends Pump {

    public EnhancedPump() {
        super();
    }

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new EnhancedPumpTile(event);
    }

    // instance
    public static Pump INSTANCE = new EnhancedPump();

    public static Pump getInstance() {
        return INSTANCE;
    }

    public static Pump getPump() {
        return getInstance();
    }
    // ----
}
