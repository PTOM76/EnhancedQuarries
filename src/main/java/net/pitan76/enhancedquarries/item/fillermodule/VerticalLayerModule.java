package net.pitan76.enhancedquarries.item.fillermodule;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.pitan76.enhancedquarries.event.FillerModuleReturn;
import net.pitan76.enhancedquarries.event.FillerProcessEvent;
import net.pitan76.enhancedquarries.item.base.FillerModule;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;

public class VerticalLayerModule extends FillerModule {
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
