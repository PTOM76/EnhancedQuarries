package net.pitan76.enhancedquarries.item.fillermodule;

import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.pitan76.enhancedquarries.event.FillerModuleReturn;
import net.pitan76.enhancedquarries.event.FillerProcessEvent;
import net.pitan76.enhancedquarries.item.base.FillerModule;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;

public class TowerModule extends FillerModule {
    public TowerModule(CompatibleItemSettings settings) {
        super(settings);
    }

    @Override
    public FillerModuleReturn onProcess(FillerProcessEvent e) {
        if (!e.isAirOrLiquid()) {
            return FillerModuleReturn.CONTINUE;
        }

        BlockState underState = e.getWorld().getBlockState(e.getProcessPos().down());
        if (underState.getBlock() instanceof AirBlock || underState.getBlock() instanceof FluidBlock) {
            return FillerModuleReturn.CONTINUE;
        }
        return e.placeBlock();
    }
}
