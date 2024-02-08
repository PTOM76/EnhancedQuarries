package ml.pkom.enhancedquarries.tile.base;

import ml.pkom.enhancedquarries.block.base.BaseBlock;
import ml.pkom.enhancedquarries.compat.IEnergyStorage;
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

    private IEnergyStorage energyStorage = null;

    public void setEnergyStorage(IEnergyStorage energyStorage) {
        this.energyStorage = energyStorage;
    }

    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public boolean hasEnergyStorage() {
        return energyStorage != null;
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

    public boolean addEnergy(long energy) {
        if (canAddEnergy(energy)) {
            holdEnergy += energy;
            return true;
        }
        return false;
    }

    public boolean removeEnergy(long energy) {
        return addEnergy(-energy);
    }


    public boolean canAddEnergy(long energy) {
        return getMaxEnergy() > getEnergy() + energy && getEnergy() + energy >= 0;
    }

    public long insertEnergy(long amount) {
        long usableCapacity = getUsableCapacity();
        if (amount > usableCapacity) {
            holdEnergy += usableCapacity;
            return usableCapacity;
        }
        holdEnergy += amount;
        return amount;
    }

    public long extractEnergy(long amount) {
        if (amount > holdEnergy) {
            long energy = this.holdEnergy;
            this.holdEnergy = 0;
            return energy;
        }
        holdEnergy -= amount;
        return amount;
    }

    public void useEnergy(long energy) {
        setEnergy(getEnergy() - energy);
    }

    public abstract long getMaxEnergy();

    public long getUsableCapacity() {
        return getMaxEnergy() - getEnergy();
    }

    public abstract long getMaxOutput();

    public abstract long getMaxInput();

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
