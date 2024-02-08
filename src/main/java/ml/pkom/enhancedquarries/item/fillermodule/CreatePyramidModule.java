package ml.pkom.enhancedquarries.item.fillermodule;

import ml.pkom.enhancedquarries.event.FillerModuleReturn;
import ml.pkom.enhancedquarries.event.FillerProcessEvent;
import ml.pkom.enhancedquarries.item.base.FillerModuleItem;
import ml.pkom.mcpitanlibarch.api.item.CompatibleItemSettings;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.item.ItemStack;

public class CreatePyramidModule extends FillerModuleItem {
    public CreatePyramidModule(CompatibleItemSettings settings) {
        super(settings);
    }

    @Override
    public FillerModuleReturn onProcessInRange(FillerProcessEvent e) {
        if (e.getProcessBlock() instanceof AirBlock || e.getProcessBlock() instanceof FluidBlock) {
            int procX = e.getProcessPos().getX();
            int procZ = e.getProcessPos().getZ();
            int procY = e.getProcessPos().getY();
            int diffY = procY - e.getPos1().getY();
            if (procX >= e.getPos1().getX() + diffY && procX <= e.getPos2().getX() - diffY && procZ <= e.getPos1().getZ() - diffY && procZ >= e.getPos2().getZ() + diffY) {
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
