package net.pitan76.enhancedquarries.item.fillermodule;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.pitan76.enhancedquarries.event.FillerModuleRange;
import net.pitan76.enhancedquarries.event.FillerModuleReturn;
import net.pitan76.enhancedquarries.event.FillerProcessEvent;
import net.pitan76.enhancedquarries.item.base.FillerModule;
import net.pitan76.enhancedquarries.util.UnbreakableBlocks;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;

public class LevelingModule extends FillerModule {
    public LevelingModule(CompatibleItemSettings settings) {
        super(settings);
    }

    @Override
    public FillerModuleRange getRange(BlockPos pos1, BlockPos pos2) {
        // This is very different to the original implementation.
        // The original one filled in *all* blocks below pos1 and removed *all*
        // blocks above pos1. This means caves will get filled in and overhangs
        // or floating islands removed, even though they're well outside the range.
        // If you want old behaviour at the cost of more time for the user (since we
        // limit the number of checks per tick), change the below code.
        return FillerModuleRange.between(pos1.getY() - 10, pos2.getY());
    }

    @Override
    public FillerModuleReturn onProcess(FillerProcessEvent e) {
        Block procBlock = e.getProcessBlock();

        if (e.getProcessPos().getY() < e.getPos1().getY()) {
            // Fill
            if (!e.isAirOrLiquid()) {
                return FillerModuleReturn.CONTINUE;
            }
            return e.placeBlock();
        } else {
            // Destroy
            if (procBlock instanceof AirBlock || (UnbreakableBlocks.isUnbreakable(procBlock) && !e.canBreakBedrock())) {
                return FillerModuleReturn.CONTINUE;
            }
            return e.destroyBlock();
        }
    }
}
