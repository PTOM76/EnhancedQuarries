package net.pitan76.enhancedquarries.item.fillermodule;

import net.minecraft.block.AirBlock;
import net.pitan76.enhancedquarries.event.FillerModuleReturn;
import net.pitan76.enhancedquarries.event.FillerProcessEvent;
import net.pitan76.enhancedquarries.item.base.FillerModule;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;

public class CutPyramidModule extends FillerModule {
    public CutPyramidModule(CompatibleItemSettings settings) {
        super(settings);
    }

    @Override
    public boolean requiresBlocks() {
        return false;
    }

    @Override
    public FillerModuleReturn onProcess(FillerProcessEvent e) {
        if (!(e.getProcessBlock() instanceof AirBlock)) {
            int procX = e.getProcessPos().getX();
            int procZ = e.getProcessPos().getZ();
            int procY = e.getProcessPos().getY();
            int diffY = procY - e.getPos1().getY();
            if (procX >= e.getPos1().getX() + diffY && procX <= e.getPos2().getX() - diffY && procZ <= e.getPos1().getZ() - diffY && procZ >= e.getPos2().getZ() + diffY) {
                return e.destroyBlock();
            }
        }
        return FillerModuleReturn.CONTINUE;
    }
}
