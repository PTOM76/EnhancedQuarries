package net.pitan76.enhancedquarries.item.fillermodule;

import net.minecraft.util.math.Direction;
import net.pitan76.enhancedquarries.event.FillerModuleReturn;
import net.pitan76.enhancedquarries.event.FillerProcessEvent;
import net.pitan76.enhancedquarries.item.base.FillerModule;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;

public class CreateStairsModule extends FillerModule {
    public CreateStairsModule(CompatibleItemSettings settings) {
        super(settings);
    }

    @Override
    public FillerModuleReturn onProcess(FillerProcessEvent e) {
        if (!e.isAirOrLiquid()) {
            return FillerModuleReturn.CONTINUE;
        }

        int procX = e.getProcessPos().getX();
        int procZ = e.getProcessPos().getZ();
        int procY = e.getProcessPos().getY();
        int diffY = procY - e.getPos1().getY();

        boolean southCheck = procX >= e.getPos1().getX() + diffY && e.getTile().getFacing().equals(Direction.SOUTH);
        boolean northCheck = procX <= e.getPos2().getX() - diffY && e.getTile().getFacing().equals(Direction.NORTH);
        boolean eastCheck = procZ <= e.getPos1().getZ() - diffY && e.getTile().getFacing().equals(Direction.EAST);
        boolean westCheck = procZ >= e.getPos2().getZ() + diffY && e.getTile().getFacing().equals(Direction.WEST);
        if (southCheck || northCheck || eastCheck || westCheck) {
            return e.placeBlock();
        }
        return FillerModuleReturn.CONTINUE;
    }
}
