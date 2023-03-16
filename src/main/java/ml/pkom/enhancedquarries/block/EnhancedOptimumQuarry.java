package ml.pkom.enhancedquarries.block;

import ml.pkom.enhancedquarries.block.base.Quarry;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import ml.pkom.enhancedquarries.tile.EnhancedOptimumQuarryTile;
import net.minecraft.block.entity.BlockEntity;

public class EnhancedOptimumQuarry extends Quarry {

    public EnhancedOptimumQuarry() {
        super();
    }

    // instance
    public static Quarry INSTANCE = new EnhancedOptimumQuarry();

    public static Quarry getInstance() {
        return INSTANCE;
    }

    public static Quarry getQuarry() {
        return getInstance();
    }
    // ----

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new EnhancedOptimumQuarryTile(event);
    }

}
