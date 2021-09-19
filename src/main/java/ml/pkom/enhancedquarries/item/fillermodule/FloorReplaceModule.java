package ml.pkom.enhancedquarries.item.fillermodule;

import ml.pkom.enhancedquarries.event.FillerModuleReturn;
import ml.pkom.enhancedquarries.event.FillerProcessEvent;
import ml.pkom.enhancedquarries.item.base.FillerModuleItem;
import net.minecraft.block.Blocks;

public class FloorReplaceModule extends FillerModuleItem {
    public FloorReplaceModule(Settings settings) {
        super(settings);
    }

    @Override
    public FillerModuleReturn onProcessUnderRange(FillerProcessEvent e) {
        if (e.getProcessPos().getY() == e.getPos1().getY() - 1) {
            e.getWorld().setBlockState(e.getProcessPos(), Blocks.AIR.getDefaultState());
            return FillerModuleReturn.RETURN_TRUE;
        }
        return FillerModuleReturn.CONTINUE;
    }
}
