package net.pitan76.enhancedquarries.block;

import net.pitan76.enhancedquarries.block.base.Pump;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import net.pitan76.enhancedquarries.tile.EnhancedPumpTile;
import net.minecraft.block.entity.BlockEntity;

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
