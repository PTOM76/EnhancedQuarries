package ml.pkom.enhancedquarries.block;

import ml.pkom.enhancedquarries.block.base.Pump;
import ml.pkom.enhancedquarries.event.TileCreateEvent;
import ml.pkom.enhancedquarries.tile.NormalPumpTile;
import net.minecraft.block.entity.BlockEntity;

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
