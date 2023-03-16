package ml.pkom.enhancedquarries.block;

import ml.pkom.enhancedquarries.block.base.Filler;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import ml.pkom.enhancedquarries.tile.EnhancedFillerTile;
import net.minecraft.block.entity.BlockEntity;

public class EnhancedFiller extends NormalFiller {

    public EnhancedFiller() {
        super();
    }

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new EnhancedFillerTile(event);
    }

    // instance
    public static Filler INSTANCE = new EnhancedFiller();

    public static Filler getInstance() {
        return INSTANCE;
    }

    public static Filler getFiller() {
        return getInstance();
    }
    // ----
}
