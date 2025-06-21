package net.pitan76.enhancedquarries.item.fillermodule;

import net.minecraft.block.FluidBlock;
import net.pitan76.enhancedquarries.event.FillerModuleReturn;
import net.pitan76.enhancedquarries.event.FillerProcessEvent;
import net.pitan76.enhancedquarries.item.base.FillerModule;
import net.pitan76.mcpitanlib.api.block.CompatBlocks;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.util.BlockStateUtil;
import net.pitan76.mcpitanlib.midohra.world.World;

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
        World world = e.getMidohraWorld();
        if (!world.getFluidState(e.getProcessPos().toRaw()).isEmpty()) {
            if (world.getFluidState(e.getProcessPos().toRaw()).isStill()) {
                if (e.getProcessBlock() instanceof FluidBlock) {
                    world.setBlockState(e.getProcessPos(), BlockStateUtil.getMidohraDefaultState(CompatBlocks.AIR));
                    return FillerModuleReturn.RETURN_TRUE;
                }

                if (world.setBlockState(e.getProcessPos(), BlockStateUtil.getDefaultState(e.getProcessBlockState().getBlock())))
                    return FillerModuleReturn.RETURN_TRUE;
                else
                    return FillerModuleReturn.RETURN_FALSE;

            } else
                return FillerModuleReturn.CONTINUE;
        }
        return FillerModuleReturn.CONTINUE;
    }
}
