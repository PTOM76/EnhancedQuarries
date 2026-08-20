package net.pitan76.enhancedquarries.item.fillermodule;

import net.pitan76.enhancedquarries.event.FillerModuleReturn;
import net.pitan76.enhancedquarries.event.FillerProcessEvent;
import net.pitan76.enhancedquarries.item.base.FillerModule;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;

public class HorizontalLayerModule extends FillerModule {
    public HorizontalLayerModule(CompatibleItemSettings settings) {
        super(settings);
    }

    public static int interval = 6;

    @Override
    public FillerModuleReturn onProcess(FillerProcessEvent e) {
        boolean isOnInterval = (e.getProcessPos().getY() - e.getPos1().getY() + interval) % interval == 0;
        if (e.isAirOrLiquid() && isOnInterval) {
            return e.placeBlock();
        }
        return FillerModuleReturn.CONTINUE;
    }
}
