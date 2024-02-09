package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.Tiles;
import ml.pkom.enhancedquarries.tile.base.FillerTile;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import net.minecraft.block.entity.BlockEntityType;

public class EnhancedFillerTile extends FillerTile {
    public EnhancedFillerTile(BlockEntityType<?> type, TileCreateEvent event) {
        super(type, event);
    }

    public EnhancedFillerTile(TileCreateEvent event) {
        this(Tiles.ENHANCED_FILLER_TILE.getOrNull(), event);
    }

    @Override
    public double getBasicSpeed() {
        return super.getBasicSpeed() * 2;
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
