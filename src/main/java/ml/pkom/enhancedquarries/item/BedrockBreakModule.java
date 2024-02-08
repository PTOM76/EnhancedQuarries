package ml.pkom.enhancedquarries.item;

import ml.pkom.enhancedquarries.block.base.Filler;
import ml.pkom.enhancedquarries.block.base.Quarry;
import ml.pkom.enhancedquarries.tile.base.FillerTile;
import ml.pkom.enhancedquarries.tile.base.QuarryTile;
import ml.pkom.mcpitanlibarch.api.event.item.ItemUseOnBlockEvent;
import ml.pkom.mcpitanlibarch.api.item.CompatibleItemSettings;
import ml.pkom.mcpitanlibarch.api.item.ExtendItem;
import ml.pkom.mcpitanlibarch.api.util.TextUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BedrockBreakModule extends ExtendItem {
    public BedrockBreakModule(CompatibleItemSettings settings) {
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
                if (quarry.canBedrockBreak()) {
                    e.getPlayer().sendMessage(TextUtil.translatable("message.enhanced_quarries.bedrock_break_module.1"));
                    return ActionResult.PASS;
                }
                quarry.setBedrockBreak(true);
                e.getStack().setCount(e.getStack().getCount() - 1);
                return ActionResult.SUCCESS;
            }
        }
        if (world.getBlockState(blockPos).getBlock() instanceof Filler) {
            if (world.getBlockEntity(blockPos) != null && world.getBlockEntity(blockPos) instanceof FillerTile) {
                FillerTile filler = (FillerTile) world.getBlockEntity(blockPos);
                if (filler.canBedrockBreak()) {
                    e.getPlayer().sendMessage(TextUtil.translatable("message.enhanced_quarries.bedrock_break_module.1"));
                    return ActionResult.PASS;
                }
                filler.setBedrockBreak(true);
                e.getStack().setCount(e.getStack().getCount() - 1);
                return ActionResult.SUCCESS;
            }
        }
        return super.onRightClickOnBlock(e);
    }
}
