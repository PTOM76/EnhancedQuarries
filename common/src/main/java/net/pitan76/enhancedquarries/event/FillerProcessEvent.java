package net.pitan76.enhancedquarries.event;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.item.ItemStack;
import net.pitan76.enhancedquarries.tile.base.FillerTile;
import net.pitan76.mcpitanlib.api.util.v2.BlockUtilV2;
import net.pitan76.mcpitanlib.midohra.block.BlockState;
import net.pitan76.mcpitanlib.midohra.util.math.BlockPos;
import net.pitan76.mcpitanlib.midohra.world.World;

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
        return getMidohraWorld().getBlockState(processPos);
    }

    public BlockPos getProcessPos() {
        return processPos;
    }

    public Block getProcessBlock() {
        return processBlock;
    }

    public BlockPos getTilePos() {
        return BlockPos.of(getTile().callGetPos());
    }

    public BlockState getBlockState() {
        return getMidohraWorld().getBlockState(getTilePos());
    }

    public BlockPos getPos1() {
        return getTile().getPos1();
    }

    public BlockPos getPos2() {
        return getTile().getPos2();
    }

    public net.minecraft.world.World getWorld() {
        return getTile().callGetWorld();
    }

    public World getMidohraWorld() {
        return World.of(getWorld());
    }

    public ItemStack getStack() { return getTile().getInventoryStack(); }

    public FillerModuleReturn placeBlock() {
        ItemStack stack = getStack();
        if (stack.isEmpty()) return FillerModuleReturn.RETURN_FALSE;

        Block itemBlock = BlockUtilV2.fromItem(stack);
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
