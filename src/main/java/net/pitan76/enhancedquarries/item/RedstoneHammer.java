package net.pitan76.enhancedquarries.item;

import net.minecraft.block.entity.BlockEntity;
import net.pitan76.enhancedquarries.tile.base.BaseEnergyTile;
import net.pitan76.mcpitanlib.api.event.item.ItemUseOnBlockEvent;
import net.pitan76.mcpitanlib.api.item.tool.CompatiblePickaxeItem;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.v3.VanillaCompatToolMaterial;
import net.pitan76.mcpitanlib.api.util.CompatActionResult;
import net.pitan76.mcpitanlib.api.util.PlatformUtil;

public class RedstoneHammer extends CompatiblePickaxeItem {
    public RedstoneHammer(CompatibleItemSettings settings) {
        super(VanillaCompatToolMaterial.STONE, 1, -3f, settings);
    }

    @Override
    public CompatActionResult onRightClickOnBlock(ItemUseOnBlockEvent e, Options options) {
        if (e.hasBlockEntity()) {
            BlockEntity blockEntity = e.getBlockEntity();
            if (PlatformUtil.isModLoaded("reborncore")) {
                if (blockEntity instanceof reborncore.common.powerSystem.PowerAcceptorBlockEntity) {
                    reborncore.common.powerSystem.PowerAcceptorBlockEntity energyStorage = (reborncore.common.powerSystem.PowerAcceptorBlockEntity) blockEntity;
                    energyStorage.addEnergy(5);
                    return e.success();
                }
            }
            if (blockEntity instanceof BaseEnergyTile) {
                BaseEnergyTile energyStorage = (BaseEnergyTile) blockEntity;
                energyStorage.addEnergy(5);
                return e.success();
            }
        }
        /*
        BlockState block = context.getWorld().getBlockState(context.getBlockPos());
        block.cycle(LeverBlock.POWERED);
        context.getWorld().updateNeighborsAlways(context.getBlockPos(), block.getBlock());
        block.cycle(LeverBlock.POWERED);
        context.getWorld().updateNeighborsAlways(context.getBlockPos(), block.getBlock());

         */
        return super.onRightClickOnBlock(e, options);
    }
}
