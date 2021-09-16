package ml.pkom.enhancedquarries.event;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockStatePos {
    private BlockState blockState = null;
    private BlockPos blockPos = null;
    private World world = null;

    public BlockStatePos(BlockState state, BlockPos pos, World world) {
        this.blockState = state;
        this.blockPos = pos;
        this.world = world;
    }

    public BlockStatePos(BlockState state, BlockPos pos) {
        this.blockState = state;
        this.blockPos = pos;
    }

    public BlockStatePos(BlockPos pos, World world) {
        this.blockPos = pos;
        this.world = world;
    }

    public BlockState getBlockState() {
        if (blockState == null) return getWorld().getBlockState(getBlockPos());
        return blockState;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public World getWorld() {
        return world;
    }

    public void setBlockState(BlockState blockState) {
        this.blockState = blockState;
    }

    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public int getPosX() {
        return getBlockPos().getX();
    }

    public int getPosY() {
        return getBlockPos().getY();
    }

    public int getPosZ() {
        return getBlockPos().getZ();
    }

    public BlockStatePos copy() {
        return new BlockStatePos(getBlockState(), getBlockPos(), getWorld());
    }

    @Override
    public String toString() {
        return "BlockStatePos{" +
                "blockState=" + blockState +
                ", blockPos=" + blockPos +
                ", world=" + world +
                '}';
    }
}
