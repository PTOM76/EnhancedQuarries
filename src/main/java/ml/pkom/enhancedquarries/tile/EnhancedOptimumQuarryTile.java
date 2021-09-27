package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.Tiles;
import ml.pkom.enhancedquarries.event.TileCreateEvent;
import net.minecraft.block.entity.BlockEntityType;

public class EnhancedOptimumQuarryTile extends OptimumQuarryTile {

    public EnhancedOptimumQuarryTile() {
        super(Tiles.ENHANCED_OPTIMUM_QUARRY_TILE);
    }

    public EnhancedOptimumQuarryTile(BlockEntityType<?> type) {
        super(type);
    }

    public EnhancedOptimumQuarryTile(TileCreateEvent event) {
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
