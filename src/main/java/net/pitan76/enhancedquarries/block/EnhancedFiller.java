package net.pitan76.enhancedquarries.block;

import net.minecraft.block.entity.BlockEntity;
import net.pitan76.enhancedquarries.block.base.Filler;
import net.pitan76.enhancedquarries.tile.EnhancedFillerTile;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;

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
