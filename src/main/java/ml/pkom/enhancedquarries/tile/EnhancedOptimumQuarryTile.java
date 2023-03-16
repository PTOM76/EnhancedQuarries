package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.Tiles;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class EnhancedOptimumQuarryTile extends OptimumQuarryTile {

    public EnhancedOptimumQuarryTile(BlockPos pos, BlockState state) {
        super(Tiles.ENHANCED_OPTIMUM_QUARRY_TILE.getOrNull(), pos, state);
    }

    public EnhancedOptimumQuarryTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public EnhancedOptimumQuarryTile(TileCreateEvent event) {
        this(event.getBlockPos(), event.getBlockState());
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
