package ml.pkom.enhancedquarries.item.fillermodule;

import ml.pkom.enhancedquarries.event.FillerModuleReturn;
import ml.pkom.enhancedquarries.event.FillerProcessEvent;
import ml.pkom.enhancedquarries.item.base.FillerModuleItem;
import ml.pkom.mcpitanlibarch.api.item.CompatibleItemSettings;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public class VerticalLayerModule extends FillerModuleItem {
    public VerticalLayerModule(CompatibleItemSettings settings) {
        super(settings);
    }

    public static int interval = 6;

    @Override
    public FillerModuleReturn onProcessInRange(FillerProcessEvent e) {
        if ((e.getProcessBlock() instanceof AirBlock || e.getProcessBlock() instanceof FluidBlock)
                && (
                ((e.getProcessPos().getX() - e.getPos1().getX() + interval) % interval == 0 && isX(e.getTile().getFacing()))
                        || ((e.getProcessPos().getZ() - e.getPos1().getZ() + interval) % interval == 0 && !isX(e.getTile().getFacing()))
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
