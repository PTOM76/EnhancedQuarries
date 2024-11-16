package net.pitan76.enhancedquarries.event.v2;

import net.pitan76.mcpitanlib.midohra.block.BlockState;
import net.pitan76.mcpitanlib.midohra.util.math.BlockPos;
import net.pitan76.mcpitanlib.midohra.world.World;

public class BlockStatePos {
    private BlockState blockState = null;
    private BlockPos blockPos = null;
    private World world = null;

    public BlockStatePos(BlockState state, BlockPos pos, World world) {
        this.blockState = state;
        this.blockPos = pos;
        this.world = world;
    }

    public BlockStatePos(net.minecraft.block.BlockState state, net.minecraft.util.math.BlockPos pos, net.minecraft.world.World world) {
        this(BlockState.of(state), BlockPos.of(pos), World.of(world));
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

    public net.minecraft.block.BlockState getRawBlockState() {
        return getBlockState().toMinecraft();
    }

    public net.minecraft.util.math.BlockPos getRawBlockPos() {
        return getBlockPos().toMinecraft();
    }

    public net.minecraft.world.World getRawWorld() {
        return getWorld().toMinecraft();
    }

    @Override
    public String toString() {
        return "BlockStatePos{" +
                "blockState=" + getRawBlockState() +
                ", blockPos=" + getRawBlockPos() +
                ", world=" + getRawWorld() +
                '}';
    }
}
