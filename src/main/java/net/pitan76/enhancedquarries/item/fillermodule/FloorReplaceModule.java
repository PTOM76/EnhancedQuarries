package net.pitan76.enhancedquarries.item.fillermodule;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.pitan76.enhancedquarries.event.FillerModuleRange;
import net.pitan76.enhancedquarries.event.FillerModuleReturn;
import net.pitan76.enhancedquarries.event.FillerProcessEvent;
import net.pitan76.enhancedquarries.item.base.FillerModule;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;

public class FloorReplaceModule extends FillerModule {
    public FloorReplaceModule(CompatibleItemSettings settings) {
        super(settings);
    }

    @Override
    public FillerModuleRange getRange(BlockPos pos1, BlockPos pos2) {
        return FillerModuleRange.singleLayer(pos1.getY() - 1);
    }
    @Override
    public FillerModuleReturn onProcess(FillerProcessEvent e) {
        return e.placeBlock();
    }
}
