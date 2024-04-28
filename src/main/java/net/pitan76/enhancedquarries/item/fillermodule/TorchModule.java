package net.pitan76.enhancedquarries.item.fillermodule;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.pitan76.enhancedquarries.event.FillerModuleReturn;
import net.pitan76.enhancedquarries.event.FillerProcessEvent;
import net.pitan76.enhancedquarries.item.base.FillerModule;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;

public class TorchModule extends FillerModule {
    public TorchModule(CompatibleItemSettings settings) {
        super(settings);
    }

    public static int interval = 6;

    @Override
    public FillerModuleReturn onProcess(FillerProcessEvent e) {
        if (!e.isAirOrLiquid()) {
            return FillerModuleReturn.CONTINUE;
        }

        int procX = e.getProcessPos().getX();
        int procZ = e.getProcessPos().getZ();
        int procY = e.getProcessPos().getY();
        BlockPos pos1 = e.getPos1();
        boolean coordPlaceable = ((procY - pos1.getY() + interval) % interval == 0)
                            && ((procX - pos1.getX() + interval) % interval == 0)
                            && ((procZ - pos1.getZ() + interval) % interval == 0);
        if (!coordPlaceable) {
            return FillerModuleReturn.CONTINUE;
        }

        boolean isNotPlaceable = e.getWorld().getBlockState(e.getProcessPos().down()).getBlock() instanceof AirBlock;
        if (isNotPlaceable) {
            return FillerModuleReturn.CONTINUE;
        }

        return e.placeBlock();
    }
}
