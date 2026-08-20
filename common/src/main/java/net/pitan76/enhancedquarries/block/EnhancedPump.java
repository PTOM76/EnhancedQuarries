package net.pitan76.enhancedquarries.block;

import net.minecraft.block.entity.BlockEntity;
import net.pitan76.enhancedquarries.block.base.Pump;
import net.pitan76.enhancedquarries.tile.EnhancedPumpTile;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;

public class EnhancedPump extends Pump {

    public EnhancedPump() {
        super();
    }

    public EnhancedPump(CompatIdentifier id) {
        super(id);
    }

    public EnhancedPump(CompatibleBlockSettings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new EnhancedPumpTile(event);
    }
}
