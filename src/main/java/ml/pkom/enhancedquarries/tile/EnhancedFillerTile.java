package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.event.TileCreateEvent;
import ml.pkom.enhancedquarries.tile.base.FillerTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class EnhancedFillerTile extends FillerTile {

    public EnhancedFillerTile(BlockPos pos, BlockState state) {
        super(Tiles.ENHANCED_FILLER_TILE, pos, state);
    }

    public EnhancedFillerTile(BlockEntityType type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public EnhancedFillerTile(TileCreateEvent event) {
        this(event.getBlockPos(), event.getBlockState());
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
    public long getBaseMaxPower() {
        return 10000;
    }

    @Override
    public long getBaseMaxInput() {
        return super.getBaseMaxInput() * 3;
    }

    @Override
    public double getSettingCoolTime() {
        return 200;
    }
}
