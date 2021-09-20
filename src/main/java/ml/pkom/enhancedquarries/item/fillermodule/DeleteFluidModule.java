package ml.pkom.enhancedquarries.item.fillermodule;

import ml.pkom.enhancedquarries.event.FillerModuleReturn;
import ml.pkom.enhancedquarries.event.FillerProcessEvent;
import ml.pkom.enhancedquarries.item.base.FillerModuleItem;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;

public class DeleteFluidModule extends FillerModuleItem {
    public DeleteFluidModule(Settings settings) {
        super(settings);
    }

    @Override
    public FillerModuleReturn onProcessInRange(FillerProcessEvent e) {
        if (!e.getWorld().getFluidState(e.getProcessPos()).isEmpty()) {
            if (e.getWorld().getFluidState(e.getProcessPos()).isStill()) {
                if (e.getProcessBlock() instanceof FluidBlock) {
                    e.getWorld().setBlockState(e.getProcessPos(), Blocks.AIR.getDefaultState());
                    return FillerModuleReturn.RETURN_TRUE;
                }
                if (e.getWorld().setBlockState(e.getProcessPos(), e.getProcessBlockState().getBlock().getDefaultState())) return FillerModuleReturn.RETURN_TRUE;
                else return FillerModuleReturn.RETURN_FALSE;
            } else return FillerModuleReturn.CONTINUE;
        }
        return FillerModuleReturn.CONTINUE;
    }
}
