package ml.pkom.enhancedquarries.item.fillermodule;

import ml.pkom.enhancedquarries.event.FillerModuleReturn;
import ml.pkom.enhancedquarries.event.FillerProcessEvent;
import ml.pkom.enhancedquarries.item.base.FillerModuleItem;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.item.ItemStack;

public class HorizontalLayerModule extends FillerModuleItem {
    public HorizontalLayerModule(Settings settings) {
        super(settings);
    }

    public static int interval = 6;

    @Override
    public FillerModuleReturn onProcessInRange(FillerProcessEvent e) {
        if ((e.getProcessBlock() instanceof AirBlock || e.getProcessBlock() instanceof FluidBlock)
                && (
                ((e.getProcessPos().getY() - e.getPos1().getY() + interval) % interval == 0)
        )) {
            ItemStack stack = e.getTile().getInventoryStack();
            if (stack.isEmpty()) return FillerModuleReturn.RETURN_FALSE;
            Block block = Block.getBlockFromItem(stack.getItem());
            if (block.equals(e.getProcessBlock())) return FillerModuleReturn.CONTINUE;
            if (e.getTile().tryPlacing(e.getProcessPos(), block, stack)) return FillerModuleReturn.RETURN_TRUE;
        }
        return super.onProcessInRange(e);
    }
}
