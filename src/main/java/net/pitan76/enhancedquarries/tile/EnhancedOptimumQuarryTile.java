package net.pitan76.enhancedquarries.tile;

import net.pitan76.enhancedquarries.Tiles;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import net.minecraft.block.entity.BlockEntityType;

public class EnhancedOptimumQuarryTile extends OptimumQuarryTile {

    public EnhancedOptimumQuarryTile(BlockEntityType<?> type, TileCreateEvent event) {
        super(type, event);
    }

    public EnhancedOptimumQuarryTile(TileCreateEvent event) {
        this(Tiles.ENHANCED_OPTIMUM_QUARRY_TILE.getOrNull(), event);
    }

    @Override
    public double getBasicSpeed() {
        return 10;
    }

    @Override
    public void coolTimeBonus() {
        super.coolTimeBonus();
        if (getMaxEnergy() / 1.025 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed() * 10;
        }
        if (getMaxEnergy() / 1.0125 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed() * 25;
        }
    }

    @Override
    public long getMaxEnergy() {
        return 10000;
    }

    @Override
    public long getMaxInput() {
        return super.getMaxInput() * 3;
    }

    @Override
    public double getSettingCoolTime() {
        return 200;
    }
}
