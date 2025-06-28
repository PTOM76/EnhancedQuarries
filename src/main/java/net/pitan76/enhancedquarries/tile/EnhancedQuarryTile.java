package net.pitan76.enhancedquarries.tile;

import net.minecraft.block.entity.BlockEntityType;
import net.pitan76.enhancedquarries.Tiles;
import net.pitan76.enhancedquarries.tile.base.QuarryTile;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;

public class EnhancedQuarryTile extends QuarryTile {

    // 継承のため
    public EnhancedQuarryTile(BlockEntityType<?> type, TileCreateEvent e) {
        super(type, e);
    }

    public EnhancedQuarryTile(TileCreateEvent event) {
        this(Tiles.ENHANCED_QUARRY_TILE.getOrNull(), event);
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
