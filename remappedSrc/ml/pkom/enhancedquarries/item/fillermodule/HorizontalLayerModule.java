package ml.pkom.enhancedquarries.item.fillermodule;

import ml.pkom.enhancedquarries.event.FillerModuleReturn;
import ml.pkom.enhancedquarries.event.FillerProcessEvent;
import ml.pkom.enhancedquarries.item.base.FillerModuleItem;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class HorizontalLayerModule extends FillerModuleItem {
    public HorizontalLayerModule(Settings settings) {
        super(settings);
    }

    @Override
    public FillerModuleReturn onProcessInRange(FillerProcessEvent e) {
        if ((e.getProcessBlock() instanceof AirBlock || e.getProcessBlock() instanceof FluidBlock)
                && (
                ((e.getProcessPos().getY() - e.getPos1().getY() + e.getTile().getModuleInterval()) % e.getTile().getModuleInterval() == 0)
        )) {
            ItemStack stack = e.getTile().getInventoryStack();
            if (stack.isEmpty()) return FillerModuleReturn.RETURN_FALSE;
            Block block = Block.getBlockFromItem(stack.getItem());
            if (block.is(e.getProcessBlock())) return FillerModuleReturn.CONTINUE;
            if (e.getTile().tryPlacing(e.getProcessPos(), block, stack)) return FillerModuleReturn.RETURN_TRUE;
        }
        return super.onProcessInRange(e);
    }
}
