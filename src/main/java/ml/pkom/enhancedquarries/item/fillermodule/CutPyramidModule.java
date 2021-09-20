package ml.pkom.enhancedquarries.item.fillermodule;

import ml.pkom.enhancedquarries.event.FillerModuleReturn;
import ml.pkom.enhancedquarries.event.FillerProcessEvent;
import ml.pkom.enhancedquarries.item.base.FillerModuleItem;
import net.minecraft.block.AirBlock;

public class CutPyramidModule extends FillerModuleItem {
    public CutPyramidModule(Settings settings) {
        super(settings);
    }

    @Override
    public FillerModuleReturn onProcessInRange(FillerProcessEvent e) {
        if (!(e.getProcessBlock() instanceof AirBlock)) {
            int procX = e.getProcessPos().getX();
            int procZ = e.getProcessPos().getZ();
            int procY = e.getProcessPos().getY();
            int diffY = procY - e.getPos1().getY();
            if (procX >= e.getPos1().getX() + diffY && procX <= e.getPos2().getX() - diffY && procZ <= e.getPos1().getZ() - diffY && procZ >= e.getPos2().getZ() + diffY) {
                if (e.getTile().tryBreaking(e.getProcessPos())) return FillerModuleReturn.RETURN_TRUE;
                else return FillerModuleReturn.RETURN_FALSE;
            }
        }
        return FillerModuleReturn.CONTINUE;
    }
}
