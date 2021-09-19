package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.event.TileCreateEvent;
import ml.pkom.enhancedquarries.tile.base.QuarryTile;
import net.minecraft.block.entity.BlockEntityType;

public class EnhancedQuarryTile extends QuarryTile {

    public EnhancedQuarryTile() {
        super(Tiles.ENHANCED_QUARRY_TILE);
    }

    // 継承のため
    public EnhancedQuarryTile(BlockEntityType<?> type) {
        super(type);
    }

    public EnhancedQuarryTile(TileCreateEvent event) {
        this();
    }

    @Override
    public double getBasicSpeed() {
        return 10;
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
