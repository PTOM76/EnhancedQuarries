package net.pitan76.enhancedquarries.tile;

import net.pitan76.enhancedquarries.Tiles;
import net.pitan76.enhancedquarries.tile.base.PumpTile;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import net.minecraft.block.entity.BlockEntityType;

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
