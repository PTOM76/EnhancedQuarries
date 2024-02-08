package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.Tiles;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import ml.pkom.enhancedquarries.tile.base.PumpTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class EnhancedPumpTile extends PumpTile {

    public double getSettingCoolTime() {
        return 200;
    }

    public double getBasicSpeed() {
        return super.getBasicSpeed() * 2;
    }

    public long getMaxEnergy() {
        return super.getMaxEnergy() * 2;
    }

    public long getEnergyCost() {
        return 40;
    }

    public EnhancedPumpTile(BlockPos pos, BlockState state) {
        this(Tiles.ENHANCED_PUMP_TILE.getOrNull(), pos, state);
    }

    public EnhancedPumpTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public EnhancedPumpTile(TileCreateEvent event) {
        this(event.getBlockPos(), event.getBlockState());
    }
}
