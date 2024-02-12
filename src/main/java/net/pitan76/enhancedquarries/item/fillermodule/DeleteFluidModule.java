package net.pitan76.enhancedquarries.item.fillermodule;

import net.pitan76.enhancedquarries.event.FillerModuleReturn;
import net.pitan76.enhancedquarries.event.FillerProcessEvent;
import net.pitan76.enhancedquarries.item.base.FillerModule;
import ml.pkom.mcpitanlibarch.api.item.CompatibleItemSettings;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;

public class DeleteFluidModule extends FillerModule {
    public DeleteFluidModule(CompatibleItemSettings settings) {
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
