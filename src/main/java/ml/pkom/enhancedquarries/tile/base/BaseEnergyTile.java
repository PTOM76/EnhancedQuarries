package ml.pkom.enhancedquarries.tile.base;

import ml.pkom.enhancedquarries.block.base.BaseBlock;
import ml.pkom.mcpitanlibarch.api.tile.ExtendBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public abstract class BaseEnergyTile extends ExtendBlockEntity implements BlockEntityTicker<BaseEnergyTile> {
    public BaseEnergyTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void writeNbtOverride(NbtCompound nbt) {
        nbt.putLong("energy", holdEnergy);
        super.writeNbtOverride(nbt);
    }

    @Override
    public void readNbtOverride(NbtCompound nbt) {
        if (nbt.contains("energy"))
            holdEnergy = nbt.getLong("energy");
        super.readNbtOverride(nbt);
    }

    private long holdEnergy = 0;

    public long getEnergy() {
        return holdEnergy;
    }

    public void setEnergy(long energy) {
        holdEnergy = energy;
    }

    public void addEnergy(long energy) {
        holdEnergy += energy;
    }

    public void useEnergy(long energy) {
        setEnergy(getEnergy() - energy);
    }

    public abstract long getBaseMaxPower();

    public abstract long getBaseMaxOutput();

    public abstract long getBaseMaxInput();

    @Override
    public void tick(World world, BlockPos pos, BlockState state, BaseEnergyTile blockEntity) {

    }

    public boolean isActive() {
        return BaseBlock.isActive(getBlockState());
    }

    public void setActive(boolean bool, World world, BlockPos blockPos) {
        BaseBlock.setActive(bool, world, blockPos);
    }

    public void setActive(boolean bool) {
        setActive(bool, getWorld(), getPos());
    }

    public Direction getFacing(BlockState state) {
        return BaseBlock.getFacing(state);
    }

    public Direction getFacing() {
        return getFacing(getBlockState());
    }

    public BlockState getBlockState() {
        return getWorld().getBlockState(getPos());
    }

}
