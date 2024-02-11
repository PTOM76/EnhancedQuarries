package ml.pkom.enhancedquarries.item.fillermodule;

import ml.pkom.enhancedquarries.event.FillerModuleReturn;
import ml.pkom.enhancedquarries.event.FillerProcessEvent;
import ml.pkom.enhancedquarries.item.base.FillerModule;
import ml.pkom.mcpitanlibarch.api.item.CompatibleItemSettings;
import net.minecraft.block.AirBlock;
import net.minecraft.util.math.Direction;

public class CutStairsModule extends FillerModule {
    public CutStairsModule(CompatibleItemSettings settings) {
        super(settings);
    }

    @Override
    public FillerModuleReturn onProcessInRange(FillerProcessEvent e) {
        if (!(e.getProcessBlock() instanceof AirBlock)) {
            int procX = e.getProcessPos().getX();
            int procZ = e.getProcessPos().getZ();
            int procY = e.getProcessPos().getY();
            int diffY = procY - e.getPos1().getY();
            if ((procX >= e.getPos1().getX() + diffY && e.getTile().getFacing().equals(Direction.SOUTH)) || (procX <= e.getPos2().getX() - diffY && e.getTile().getFacing().equals(Direction.NORTH)) || (procZ <= e.getPos1().getZ() - diffY && e.getTile().getFacing().equals(Direction.EAST)) || (procZ >= e.getPos2().getZ() + diffY && e.getTile().getFacing().equals(Direction.WEST))) {
                if (e.getTile().tryBreaking(e.getProcessPos())) return FillerModuleReturn.RETURN_TRUE;
                else return FillerModuleReturn.RETURN_FALSE;
            }
        }
        return FillerModuleReturn.CONTINUE;
    }
}
