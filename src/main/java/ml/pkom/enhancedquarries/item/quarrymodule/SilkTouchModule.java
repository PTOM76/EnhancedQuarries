package ml.pkom.enhancedquarries.item.quarrymodule;

import ml.pkom.enhancedquarries.block.base.Quarry;
import ml.pkom.enhancedquarries.tile.base.QuarryTile;
import ml.pkom.mcpitanlibarch.api.event.item.ItemUseOnBlockEvent;
import ml.pkom.mcpitanlibarch.api.item.CompatibleItemSettings;
import ml.pkom.mcpitanlibarch.api.item.ExtendItem;
import ml.pkom.mcpitanlibarch.api.util.TextUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SilkTouchModule extends ExtendItem {
    public SilkTouchModule(CompatibleItemSettings settings) {
        super(settings);
    }

    @Override
    public ActionResult onRightClickOnBlock(ItemUseOnBlockEvent e) {
        World world = e.getWorld();
        if (world.isClient())
            return super.onRightClickOnBlock(e);
        BlockPos blockPos = e.getBlockPos();

        if (world.getBlockState(blockPos).getBlock() instanceof Quarry) {
            if (world.getBlockEntity(blockPos) != null && world.getBlockEntity(blockPos) instanceof QuarryTile) {
                QuarryTile quarry = (QuarryTile) world.getBlockEntity(blockPos);
                if (quarry.isSetSilkTouch()) {
                    e.getPlayer().sendMessage(TextUtil.translatable("message.enhanced_quarries.silk_touch_module.1"));
                    return ActionResult.PASS;
                }
                quarry.setSilkTouchModule(true);
                e.getStack().setCount(e.getStack().getCount() - 1);
                return ActionResult.SUCCESS;
            }
        }
        return super.onRightClickOnBlock(e);
    }
}
