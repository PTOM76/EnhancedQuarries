package net.pitan76.enhancedquarries.event;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.pitan76.enhancedquarries.tile.base.FillerTile;

public class FillerProcessEvent {
    private final FillerTile tile;
    private final BlockPos processPos;
    private final Block processBlock;

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
        return getWorld().getBlockState(getTilePos());
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

    public ItemStack getStack() { return getTile().getInventoryStack(); }

    public FillerModuleReturn placeBlock() {
        ItemStack stack = getStack();
        if (stack.isEmpty()) return FillerModuleReturn.RETURN_FALSE;

        Block itemBlock = Block.getBlockFromItem(stack.getItem());
        // Skip if it's the same block. E.g., don't replace stone with stone!
        if (itemBlock.equals(getProcessBlock())) return FillerModuleReturn.CONTINUE;
        // New block!
        boolean wasPlacementSuccessful = getTile().tryPlacing(getProcessPos(), itemBlock, stack);
        if (wasPlacementSuccessful) return FillerModuleReturn.RETURN_TRUE;

        return FillerModuleReturn.CONTINUE;
    }

    public FillerModuleReturn destroyBlock() {
        if (getTile().tryBreaking(getProcessPos())) return FillerModuleReturn.RETURN_TRUE;
        else return FillerModuleReturn.RETURN_FALSE;
    }

    public boolean isAirOrLiquid() {
        Block procBlock = getProcessBlock();
        return procBlock instanceof AirBlock || procBlock instanceof FluidBlock;
    }

    public boolean canBreakBedrock() {
        return getTile().canBedrockBreak();
    }

}
