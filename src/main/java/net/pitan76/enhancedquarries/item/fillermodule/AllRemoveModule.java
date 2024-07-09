package net.pitan76.enhancedquarries.item.fillermodule;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.pitan76.enhancedquarries.event.FillerModuleReturn;
import net.pitan76.enhancedquarries.event.FillerProcessEvent;
import net.pitan76.enhancedquarries.item.base.FillerModule;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;

public class AllRemoveModule extends FillerModule {
    public AllRemoveModule(CompatibleItemSettings settings) {
        super(settings);
    }

    @Override
    public boolean requiresBlocks() {
        return false;
    }

    @Override
    public FillerModuleReturn onProcess(FillerProcessEvent e) {
        Block procBlock = e.getProcessBlock();
        if (procBlock instanceof AirBlock || (procBlock.equals(Blocks.BEDROCK) && !e.canBreakBedrock())) return FillerModuleReturn.CONTINUE;
        
        return e.destroyBlock();
    }
}
