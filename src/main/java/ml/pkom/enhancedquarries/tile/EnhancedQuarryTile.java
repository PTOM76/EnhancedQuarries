package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.Tiles;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import ml.pkom.enhancedquarries.tile.base.QuarryTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class EnhancedQuarryTile extends QuarryTile {

    public EnhancedQuarryTile(BlockPos pos, BlockState state) {
        super(Tiles.ENHANCED_QUARRY_TILE.getOrNull(), pos, state);
    }

    // 継承のため
    public EnhancedQuarryTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public EnhancedQuarryTile(TileCreateEvent event) {
        this(event.getBlockPos(), event.getBlockState());
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
