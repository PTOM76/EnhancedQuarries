package net.pitan76.enhancedquarries.item;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.ActionResult;
import net.pitan76.enhancedquarries.tile.base.BaseEnergyTile;
import net.pitan76.mcpitanlib.api.event.item.ItemUseOnBlockEvent;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.tool.CompatiblePickaxeItem;
import net.pitan76.mcpitanlib.api.util.CompatActionResult;

public class RedstoneHammer extends CompatiblePickaxeItem {
    public RedstoneHammer(CompatibleItemSettings settings) {
        super(1, -3f, ToolMaterials.STONE, settings);
    }

    @Override
    public CompatActionResult onRightClickOnBlock(ItemUseOnBlockEvent e, Options options) {
        if (e.getWorld().getBlockEntity(e.getBlockPos()) != null) {
            BlockEntity blockEntity = e.getWorld().getBlockEntity(e.getBlockPos());
            if (FabricLoader.getInstance().isModLoaded("reborncore")) {
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
