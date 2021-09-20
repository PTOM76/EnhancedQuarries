package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.event.TileCreateEvent;
import ml.pkom.enhancedquarries.tile.base.FillerTile;
import net.minecraft.block.entity.BlockEntityType;

public class EnhancedFillerTile extends FillerTile {

    public EnhancedFillerTile() {
        super(Tiles.ENHANCED_FILLER_TILE);
    }

    public EnhancedFillerTile(BlockEntityType type) {
        super(type);
    }

    public EnhancedFillerTile(TileCreateEvent event) {
        this();
    }

    @Override
    public double getBasicSpeed() {
        return super.getBasicSpeed() * 2;
    }

    @Override
    public void coolTimeBonus() {
        super.coolTimeBonus();
        if (getBaseMaxPower() / 1.025 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed() * 10;
        }
        if (getBaseMaxPower() / 1.0125 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed() * 25;
        }
    }

    @Override
    public double getBaseMaxPower() {
        return 10000;
    }

    @Override
    public double getBaseMaxInput() {
        return super.getBaseMaxInput() * 3;
    }

    @Override
    public double getSettingCoolTime() {
        return 200;
    }
}
