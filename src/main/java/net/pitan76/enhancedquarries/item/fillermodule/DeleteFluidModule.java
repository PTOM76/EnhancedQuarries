package net.pitan76.enhancedquarries.item.fillermodule;

import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.pitan76.enhancedquarries.event.FillerModuleReturn;
import net.pitan76.enhancedquarries.event.FillerProcessEvent;
import net.pitan76.enhancedquarries.item.base.FillerModule;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.util.BlockStateUtil;
import net.pitan76.mcpitanlib.api.util.WorldUtil;

public class DeleteFluidModule extends FillerModule {
    public DeleteFluidModule(CompatibleItemSettings settings) {
        super(settings);
    }

    @Override
    public boolean requiresBlocks() {
        return false;
    }

    @Override
    public FillerModuleReturn onProcess(FillerProcessEvent e) {
        if (!e.getWorld().getFluidState(e.getProcessPos()).isEmpty()) {
            if (e.getWorld().getFluidState(e.getProcessPos()).isStill()) {
                if (e.getProcessBlock() instanceof FluidBlock) {
                    WorldUtil.setBlockState(e.getWorld(), e.getProcessPos(), BlockStateUtil.getDefaultState(Blocks.AIR));
                    return FillerModuleReturn.RETURN_TRUE;
                }
                if (WorldUtil.setBlockState(e.getWorld(), e.getProcessPos(), BlockStateUtil.getDefaultState(e.getProcessBlockState().getBlock()))) return FillerModuleReturn.RETURN_TRUE;
                else return FillerModuleReturn.RETURN_FALSE;
            } else return FillerModuleReturn.CONTINUE;
        }
        return FillerModuleReturn.CONTINUE;
    }
}
