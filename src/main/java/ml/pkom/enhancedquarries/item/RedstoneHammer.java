package ml.pkom.enhancedquarries.item;

import ml.pkom.enhancedquarries.simple_pipes.RedstoneHammerEvent;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.ActionResult;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;

public class RedstoneHammer extends PickaxeItem {
    public RedstoneHammer(Settings settings) {
        super(ToolMaterials.STONE, 1, -3f, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().getBlockEntity(context.getBlockPos()) != null) {
            BlockEntity blockEntity = context.getWorld().getBlockEntity(context.getBlockPos());
            if (blockEntity instanceof PowerAcceptorBlockEntity) {
                PowerAcceptorBlockEntity energyStorage = (PowerAcceptorBlockEntity) blockEntity;
                energyStorage.addEnergy(1);
                return ActionResult.SUCCESS;
            }
        }
        if (FabricLoader.getInstance().isModLoaded("simple_pipes")) {
            //new RedstoneHammerEvent(context);
            //return ActionResult.SUCCESS;
        }
        BlockState block = context.getWorld().getBlockState(context.getBlockPos());
        block.cycle(LeverBlock.POWERED);
        context.getWorld().updateNeighborsAlways(context.getBlockPos(), block.getBlock());
        block.cycle(LeverBlock.POWERED);
        context.getWorld().updateNeighborsAlways(context.getBlockPos(), block.getBlock());
        return super.useOnBlock(context);
    }
}
