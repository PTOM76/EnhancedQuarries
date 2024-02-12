package net.pitan76.enhancedquarries.item.fillermodule;

import net.pitan76.enhancedquarries.event.FillerModuleReturn;
import net.pitan76.enhancedquarries.event.FillerProcessEvent;
import net.pitan76.enhancedquarries.item.base.FillerModule;
import ml.pkom.mcpitanlibarch.api.item.CompatibleItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class FloorReplaceModule extends FillerModule {
    public FloorReplaceModule(CompatibleItemSettings settings) {
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
