package net.pitan76.enhancedquarries.item.fillermodule;

import net.minecraft.block.AirBlock;
import net.minecraft.util.math.Direction;
import net.pitan76.enhancedquarries.event.FillerModuleReturn;
import net.pitan76.enhancedquarries.event.FillerProcessEvent;
import net.pitan76.enhancedquarries.item.base.FillerModule;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;

public class CutStairsModule extends FillerModule {
    public CutStairsModule(CompatibleItemSettings settings) {
        super(settings);
    }

    @Override
    public FillerModuleReturn onProcess(FillerProcessEvent e) {
        if (!(e.getProcessBlock() instanceof AirBlock)) {
            int procX = e.getProcessPos().getX();
            int procZ = e.getProcessPos().getZ();
            int procY = e.getProcessPos().getY();
            int diffY = procY - e.getPos1().getY();
            if ((procX >= e.getPos1().getX() + diffY && e.getTile().getFacing().equals(Direction.SOUTH)) || (procX <= e.getPos2().getX() - diffY && e.getTile().getFacing().equals(Direction.NORTH)) || (procZ <= e.getPos1().getZ() - diffY && e.getTile().getFacing().equals(Direction.EAST)) || (procZ >= e.getPos2().getZ() + diffY && e.getTile().getFacing().equals(Direction.WEST))) {
                return e.destroyBlock();
            }
        }
        return FillerModuleReturn.CONTINUE;
    }
}
