package net.pitan76.enhancedquarries.item;

import net.pitan76.mcpitanlib.api.transfer.energy.v1.EnergyLookup;
import net.pitan76.enhancedquarries.tile.base.BaseEnergyTile;
import net.pitan76.mcpitanlib.api.event.item.ItemUseOnBlockEvent;
import net.pitan76.mcpitanlib.api.item.tool.CompatiblePickaxeItem;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.v3.VanillaCompatToolMaterial;
import net.pitan76.mcpitanlib.api.util.CompatActionResult;
import net.pitan76.mcpitanlib.midohra.block.entity.BlockEntityWrapper;

public class RedstoneHammer extends CompatiblePickaxeItem {
    public RedstoneHammer(CompatibleItemSettings settings) {
        super(VanillaCompatToolMaterial.STONE, 1, -3f, settings);
    }

    @Override
    public CompatActionResult onRightClickOnBlock(ItemUseOnBlockEvent e, Options options) {
        if (e.hasBlockEntity()) {
            BlockEntityWrapper blockEntity = e.getBlockEntityWrapper();
            if (EnergyLookup.ENERGY.addEnergyToForeignTile(blockEntity, 5, null) > 0)
                return e.success();

            if (blockEntity.instanceOf(BaseEnergyTile.class)) {
                BaseEnergyTile energyStorage = blockEntity.getCompatBlockEntity(BaseEnergyTile.class);
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
