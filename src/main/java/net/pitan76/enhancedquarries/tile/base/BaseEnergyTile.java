package net.pitan76.enhancedquarries.tile.base;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pitan76.enhancedquarries.block.base.BaseBlock;
import net.pitan76.enhancedquarries.compat.IEnergyStorage;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.event.nbt.ReadNbtArgs;
import net.pitan76.mcpitanlib.api.event.nbt.WriteNbtArgs;
import net.pitan76.mcpitanlib.api.tile.CompatBlockEntity;

public abstract class BaseEnergyTile extends CompatBlockEntity implements BlockEntityTicker<BaseEnergyTile> {
    public boolean keepNbtOnDrop = false;

    public BaseEnergyTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public BaseEnergyTile(BlockEntityType<?> type, TileCreateEvent e) {
        super(type, e);
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
    public void writeNbt(WriteNbtArgs args) {
        NbtCompound nbt = args.getNbt();
        nbt.putLong("energy", holdEnergy);
    }

    @Override
    public void readNbt(ReadNbtArgs args) {
        NbtCompound nbt = args.getNbt();
        if (nbt.contains("energy"))
            holdEnergy = nbt.getLong("energy");
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
