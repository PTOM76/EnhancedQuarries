package ml.pkom.enhancedquarries.tile.base;

import alexiil.mc.lib.attributes.CombinableAttribute;
import alexiil.mc.lib.attributes.SearchOptions;
import ml.pkom.enhancedquarries.block.base.BaseBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public abstract class BaseEnergyTile extends BlockEntity implements Tickable {
    public BaseEnergyTile(BlockEntityType<?> type) {
        super(type);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putLong("energy", holdEnergy);
        return super.writeNbt(nbt);
    }

    @Override
    public void fromTag(BlockState state, NbtCompound nbt) {
        if (nbt.contains("energy"))
            holdEnergy = nbt.getLong("energy");
        super.fromTag(state, nbt);
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
    public void tick() {

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

    public BlockState getBlockState() {
        return getWorld().getBlockState(getPos());
    }

    public <T> T getNeighbourAttribute(CombinableAttribute<T> attr, Direction dir) {
        return attr.get(getWorld(), getPos().offset(dir), SearchOptions.inDirection(dir));
    }
}
