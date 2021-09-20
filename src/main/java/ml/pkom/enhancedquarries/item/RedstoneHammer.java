package ml.pkom.enhancedquarries.item;

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
        return super.useOnBlock(context);
    }
}
