package net.pitan76.enhancedquarries.block;

import net.minecraft.block.entity.BlockEntity;
import net.pitan76.enhancedquarries.block.base.Pump;
import net.pitan76.enhancedquarries.tile.NormalPumpTile;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;

public class NormalPump extends Pump {

    public NormalPump() {
        super();
    }

    public NormalPump(CompatIdentifier id) {
        super(id);
    }

    public NormalPump(CompatibleBlockSettings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new NormalPumpTile(event);
    }
}
