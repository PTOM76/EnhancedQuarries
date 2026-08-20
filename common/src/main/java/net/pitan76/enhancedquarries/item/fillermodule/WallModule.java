package net.pitan76.enhancedquarries.item.fillermodule;

import net.pitan76.enhancedquarries.event.FillerModuleReturn;
import net.pitan76.enhancedquarries.event.FillerProcessEvent;
import net.pitan76.enhancedquarries.item.base.FillerModule;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.midohra.util.math.BlockPos;

public class WallModule extends FillerModule {
    public WallModule(CompatibleItemSettings settings) {
        super(settings);
    }

    @Override
    public FillerModuleReturn onProcess(FillerProcessEvent e) {
        if (!e.isAirOrLiquid()) {
            return FillerModuleReturn.CONTINUE;
        }

        int procX = e.getProcessPos().getX();
        int procZ = e.getProcessPos().getZ();
        BlockPos pos1 = e.getPos1();
        BlockPos pos2 = e.getPos2();
        boolean isPlaceable = procX == pos1.getX() || procX == pos2.getX()
                            || procZ == pos1.getZ() || procZ == pos2.getZ();
        if (!isPlaceable) {
            return FillerModuleReturn.CONTINUE;
        }

        return e.placeBlock();
    }
}
