package net.pitan76.enhancedquarries.item.base;

import net.minecraft.util.math.BlockPos;
import net.pitan76.enhancedquarries.event.FillerModuleRange;
import net.pitan76.enhancedquarries.event.FillerModuleReturn;
import net.pitan76.enhancedquarries.event.FillerProcessEvent;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.ExtendItem;

public abstract class FillerModule extends ExtendItem {

    public FillerModule(CompatibleItemSettings settings) {
        super(settings);
    }

    public boolean requiresBlocks() { return true; }
    public FillerModuleRange getRange(BlockPos pos1, BlockPos pos2) {
        return FillerModuleRange.between(pos1, pos2);
    }
    abstract public FillerModuleReturn onProcess(FillerProcessEvent e);
}
