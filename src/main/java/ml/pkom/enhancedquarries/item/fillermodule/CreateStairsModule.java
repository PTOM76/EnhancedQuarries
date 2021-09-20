package ml.pkom.enhancedquarries.item.fillermodule;

import ml.pkom.enhancedquarries.event.FillerModuleReturn;
import ml.pkom.enhancedquarries.event.FillerProcessEvent;
import ml.pkom.enhancedquarries.item.base.FillerModuleItem;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public class CreateStairsModule extends FillerModuleItem {
    public CreateStairsModule(Settings settings) {
        super(settings);
    }

    @Override
    public FillerModuleReturn onProcessInRange(FillerProcessEvent e) {
        if (e.getProcessBlock() instanceof AirBlock || e.getProcessBlock() instanceof FluidBlock) {
            int procX = e.getProcessPos().getX();
            int procZ = e.getProcessPos().getZ();
            int procY = e.getProcessPos().getY();
            int diffY = procY - e.getPos1().getY();
            if ((procX >= e.getPos1().getX() + diffY && e.getTile().getFacing().equals(Direction.SOUTH)) || (procX <= e.getPos2().getX() - diffY && e.getTile().getFacing().equals(Direction.NORTH)) || (procZ <= e.getPos1().getZ() - diffY && e.getTile().getFacing().equals(Direction.EAST)) || (procZ >= e.getPos2().getZ() + diffY && e.getTile().getFacing().equals(Direction.WEST))) {
                ItemStack stack = e.getTile().getInventoryStack();
                if (stack.isEmpty()) return FillerModuleReturn.RETURN_FALSE;
                Block block = Block.getBlockFromItem(stack.getItem());
                if (block.equals(e.getProcessBlock())) return FillerModuleReturn.CONTINUE;
                if (e.getTile().tryPlacing(e.getProcessPos(), block, stack)) return FillerModuleReturn.RETURN_TRUE;
            }
        }
        return FillerModuleReturn.CONTINUE;
    }
}
