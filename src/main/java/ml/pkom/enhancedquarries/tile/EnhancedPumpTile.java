package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.Tiles;
import ml.pkom.enhancedquarries.event.TileCreateEvent;
import ml.pkom.enhancedquarries.tile.base.PumpTile;
import net.minecraft.block.entity.BlockEntityType;

public class EnhancedPumpTile extends PumpTile {

    public double getSettingCoolTime() {
        return 200;
    }

    public double getBasicSpeed() {
        return super.getBasicSpeed() * 2;
    }

    public double getBaseMaxPower() {
        return super.getBaseMaxPower() * 2;
    }

    public long getEnergyCost() {
        return 40;
    }

    public EnhancedPumpTile() {
        this(Tiles.ENHANCED_PUMP);
    }

    public EnhancedPumpTile(BlockEntityType<?> type) {
        super(type);
    }

    public EnhancedPumpTile(TileCreateEvent event) {
        this();
    }
}
