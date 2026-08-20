package net.pitan76.enhancedquarries.block;

import net.minecraft.block.entity.BlockEntity;
import net.pitan76.enhancedquarries.EnhancedQuarries;
import net.pitan76.enhancedquarries.tile.EnhancedFillerTile;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;

public class EnhancedFiller extends NormalFiller {

    public EnhancedFiller(CompatibleBlockSettings settings) {
        super(settings);
    }

    public EnhancedFiller(CompatIdentifier id) {
        super(id);
    }

    public EnhancedFiller() {
        this(EnhancedQuarries._id("enhanced_filler"));
    }

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent e) {
        return new EnhancedFillerTile(e);
    }
}
