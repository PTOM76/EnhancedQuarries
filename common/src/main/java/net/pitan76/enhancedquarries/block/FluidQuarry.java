package net.pitan76.enhancedquarries.block;

import net.minecraft.block.entity.BlockEntity;
import net.pitan76.enhancedquarries.block.base.Quarry;
import net.pitan76.enhancedquarries.tile.FluidQuarryTile;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;

public class FluidQuarry extends Quarry {

    public FluidQuarry(CompatIdentifier id) {
        super(id);
    }

    // ----

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new FluidQuarryTile(event);
    }

}
