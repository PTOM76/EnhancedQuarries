package ml.pkom.enhancedquarries.item.fillermodule;

import ml.pkom.enhancedquarries.event.FillerModuleReturn;
import ml.pkom.enhancedquarries.event.FillerProcessEvent;
import ml.pkom.enhancedquarries.item.base.FillerModuleItem;

public class DeleteFluidModule extends FillerModuleItem {
    public DeleteFluidModule(Settings settings) {
        super(settings);
    }

    @Override
    public FillerModuleReturn onProcessInRange(FillerProcessEvent e) {
        if (!e.getWorld().getFluidState(e.getProcessPos()).isEmpty()) {
            if (e.getWorld().getFluidState(e.getProcessPos()).isStill()) {
                if (e.getWorld().removeBlock(e.getProcessPos(), false)) return FillerModuleReturn.RETURN_TRUE;
                else return FillerModuleReturn.RETURN_FALSE;
            } else return FillerModuleReturn.CONTINUE;
        }
        return FillerModuleReturn.CONTINUE;
    }
}
