package ml.pkom.enhancedquarries.item.fillermodule;

import ml.pkom.enhancedquarries.event.FillerModuleReturn;
import ml.pkom.enhancedquarries.event.FillerProcessEvent;
import ml.pkom.enhancedquarries.item.base.FillerModuleItem;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class FloorReplaceModule extends FillerModuleItem {
    public FloorReplaceModule(Settings settings) {
        super(settings);
    }

    @Override
    public FillerModuleReturn onProcessUnderRange(FillerProcessEvent e) {
        if (e.getProcessPos().getY() == e.getPos1().getY() - 1) {
            ItemStack stack = e.getTile().getInventoryStack();
            if (stack.isEmpty()) return FillerModuleReturn.RETURN_FALSE;
            Block block = Block.getBlockFromItem(stack.getItem());
            if (block.equals(e.getProcessBlock())) return FillerModuleReturn.CONTINUE;
            e.getTile().tryPlacing(e.getProcessPos(), block, stack);
            return FillerModuleReturn.RETURN_TRUE;
        }
        return FillerModuleReturn.CONTINUE;
    }
}
