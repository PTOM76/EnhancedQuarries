package ml.pkom.enhancedquarries.item;

import ml.pkom.enhancedquarries.block.base.Quarry;
import ml.pkom.enhancedquarries.tile.base.QuarryTile;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LuckEnchantModule extends Item {
    public LuckEnchantModule(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (world.isClient())
            return super.useOnBlock(context);
        BlockPos blockPos = context.getBlockPos();

        if (world.getBlockState(blockPos).getBlock() instanceof Quarry) {
            if (world.getBlockEntity(blockPos) != null && world.getBlockEntity(blockPos) instanceof QuarryTile) {
                QuarryTile quarry = (QuarryTile) world.getBlockEntity(blockPos);
                if (quarry.isSetLuck()) {
                    context.getPlayer().sendMessage(new TranslatableText("message.enhanced_quarries.luck_enchant_module.1"), false);
                    return ActionResult.PASS;
                }
                quarry.setLuckModule(true);
                context.getStack().setCount(context.getStack().getCount() - 1);
                return ActionResult.SUCCESS;
            }
        }
        return super.useOnBlock(context);
    }
}
