package net.pitan76.enhancedquarries.tile;

import net.minecraft.block.entity.BlockEntityType;
import net.pitan76.enhancedquarries.Tiles;
import net.pitan76.enhancedquarries.tile.base.PumpTile;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;

public class EnhancedPumpTile extends PumpTile {

    public double getSettingCoolTime() {
        return 200;
    }

    public double getBasicSpeed() {
        return super.getBasicSpeed() * 2;
    }

    public long getMaxEnergy() {
        return super.getMaxEnergy() * 2;
    }

    public long getEnergyCost() {
        return 40;
    }

    public EnhancedPumpTile(BlockEntityType<?> type, TileCreateEvent event) {
        super(type, event);
    }

    public EnhancedPumpTile(TileCreateEvent event) {
        this(Tiles.ENHANCED_PUMP_TILE.getOrNull(), event);
    }
}
