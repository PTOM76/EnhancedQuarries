package net.pitan76.enhancedquarries.item.base;

import net.minecraft.util.math.BlockPos;
import net.pitan76.enhancedquarries.event.FillerModuleRange;
import net.pitan76.enhancedquarries.event.FillerModuleReturn;
import net.pitan76.enhancedquarries.event.FillerProcessEvent;
import net.pitan76.mcpitanlib.api.item.v2.CompatItem;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;

public abstract class FillerModule extends CompatItem {

    public FillerModule(CompatibleItemSettings settings) {
        super(settings);
    }

    public boolean requiresBlocks() { return true; }
    public FillerModuleRange getRange(BlockPos pos1, BlockPos pos2) {
        return FillerModuleRange.between(pos1, pos2);
    }
    abstract public FillerModuleReturn onProcess(FillerProcessEvent e);
}
