package net.pitan76.enhancedquarries.block;

import net.pitan76.enhancedquarries.block.base.Filler;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import net.pitan76.enhancedquarries.tile.EnhancedFillerTile;
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
