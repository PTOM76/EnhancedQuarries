package ml.pkom.enhancedquarries.item.fillermodule;

import ml.pkom.enhancedquarries.event.FillerModuleReturn;
import ml.pkom.enhancedquarries.event.FillerProcessEvent;
import ml.pkom.enhancedquarries.item.base.FillerModuleItem;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public class VerticalLayerModule extends FillerModuleItem {
    public VerticalLayerModule(Item.Settings settings) {
        super(settings);
    }

    @Override
    public FillerModuleReturn onProcessInRange(FillerProcessEvent e) {
        if ((e.getProcessBlock() instanceof AirBlock || e.getProcessBlock() instanceof FluidBlock)
                && (
                        ((e.getProcessPos().getX() - e.getPos1().getX() + e.getTile().getModuleInterval()) % e.getTile().getModuleInterval() == 0 && isX(e.getTile().getFacing()))
                        || ((e.getProcessPos().getZ() - e.getPos1().getZ() + e.getTile().getModuleInterval()) % e.getTile().getModuleInterval() == 0 && !isX(e.getTile().getFacing()))
                )) {
            ItemStack stack = e.getTile().getInventoryStack();
            if (stack.isEmpty()) return FillerModuleReturn.RETURN_FALSE;
            Block block = Block.getBlockFromItem(stack.getItem());
            if (block.equals(e.getProcessBlock())) return FillerModuleReturn.CONTINUE;
            if (e.getTile().tryPlacing(e.getProcessPos(), block, stack)) return FillerModuleReturn.RETURN_TRUE;
        }
        return super.onProcessInRange(e);
    }

    public static boolean isX(Direction dir) {
        if (dir.equals(Direction.NORTH)) return true;
        if (dir.equals(Direction.SOUTH)) return true;
        return false;
    }
}
