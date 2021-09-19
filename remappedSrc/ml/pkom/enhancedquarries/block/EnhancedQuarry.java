package ml.pkom.enhancedquarries.block;

import ml.pkom.enhancedquarries.block.base.Quarry;
import ml.pkom.enhancedquarries.event.TileCreateEvent;
import ml.pkom.enhancedquarries.tile.EnhancedQuarryTile;
import net.minecraft.block.entity.BlockEntity;

public class EnhancedQuarry extends Quarry {

    public EnhancedQuarry() {
        super();
    }

    // instance
    public static Quarry INSTANCE = new EnhancedQuarry();

    public static Quarry getInstance() {
        return INSTANCE;
    }

    public static Quarry getQuarry() {
        return getInstance();
    }
    // ----

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new EnhancedQuarryTile(event);
    }

}
