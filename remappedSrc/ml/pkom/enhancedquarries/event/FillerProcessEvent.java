package ml.pkom.enhancedquarries.event;

import ml.pkom.enhancedquarries.tile.base.FillerTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FillerProcessEvent {
    private FillerTile tile;
    private BlockPos processPos;
    private Block processBlock;

    public FillerProcessEvent(FillerTile fillerTile, BlockPos procPos, Block procBlock) {
        this.tile = fillerTile;
        this.processPos = procPos;
        this.processBlock = procBlock;
    }

    public FillerTile getTile() {
        return tile;
    }

    public BlockState getProcessBlockState() {
        return getWorld().getBlockState(processPos);
    }

    public BlockPos getProcessPos() {
        return processPos;
    }

    public Block getProcessBlock() {
        return processBlock;
    }

    public BlockPos getTilePos() {
        return getTile().getPos();
    }

    public BlockState getBlockState() {
        return getWorld().getBlockState(getPos());
    }

    public BlockPos getPos() {
        return getTilePos();
    }

    public BlockPos getPos1() {
        return getTile().getPos1();
    }

    public BlockPos getPos2() {
        return getTile().getPos2();
    }

    public World getWorld() {
        return getTile().getWorld();
    }

}
