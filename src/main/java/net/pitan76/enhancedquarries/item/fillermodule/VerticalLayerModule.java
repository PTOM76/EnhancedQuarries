package net.pitan76.enhancedquarries.item.fillermodule;

import net.minecraft.util.math.Direction;
import net.pitan76.enhancedquarries.event.FillerModuleReturn;
import net.pitan76.enhancedquarries.event.FillerProcessEvent;
import net.pitan76.enhancedquarries.item.base.FillerModule;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;

public class VerticalLayerModule extends FillerModule {
    public VerticalLayerModule(CompatibleItemSettings settings) {
        super(settings);
    }

    public static int interval = 6;

    @Override
    public FillerModuleReturn onProcess(FillerProcessEvent e) {
        boolean isInterval = ((e.getProcessPos().getX() - e.getPos1().getX() + interval) % interval == 0 && isX(e.getTile().getFacing()))
                        || ((e.getProcessPos().getZ() - e.getPos1().getZ() + interval) % interval == 0 && !isX(e.getTile().getFacing()));
        if (e.isAirOrLiquid() && isInterval) {
            return e.placeBlock();
        }

        return FillerModuleReturn.CONTINUE;
    }

    public static boolean isX(Direction dir) {
        return dir.equals(Direction.NORTH) || dir.equals(Direction.SOUTH);
    }
}
