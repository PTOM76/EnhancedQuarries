package ml.pkom.enhancedquarries.item;

import ml.pkom.mcpitanlibarch.api.item.CompatibleItemSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.ActionResult;

public class RedstoneHammer extends PickaxeItem {
    public RedstoneHammer(CompatibleItemSettings settings) {
        super(ToolMaterials.STONE, 1, -3f, settings.build());
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().getBlockEntity(context.getBlockPos()) != null) {
            BlockEntity blockEntity = context.getWorld().getBlockEntity(context.getBlockPos());
            if (blockEntity instanceof reborncore.common.powerSystem.PowerAcceptorBlockEntity) {
                if (FabricLoader.getInstance().isModLoaded("reborncore")) {
                    reborncore.common.powerSystem.PowerAcceptorBlockEntity energyStorage = (reborncore.common.powerSystem.PowerAcceptorBlockEntity) blockEntity;
                    energyStorage.addEnergy(1);
                    return ActionResult.SUCCESS;
                }
            }
        }
        BlockState block = context.getWorld().getBlockState(context.getBlockPos());
        block.cycle(LeverBlock.POWERED);
        context.getWorld().updateNeighborsAlways(context.getBlockPos(), block.getBlock());
        block.cycle(LeverBlock.POWERED);
        context.getWorld().updateNeighborsAlways(context.getBlockPos(), block.getBlock());
        return super.useOnBlock(context);
    }
}
