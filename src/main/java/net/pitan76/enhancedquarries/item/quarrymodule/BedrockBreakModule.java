package net.pitan76.enhancedquarries.item.quarrymodule;

import net.pitan76.enhancedquarries.block.base.Filler;
import net.pitan76.enhancedquarries.block.base.Quarry;
import net.pitan76.enhancedquarries.item.base.MachineModule;
import net.pitan76.enhancedquarries.tile.base.FillerTile;
import net.pitan76.enhancedquarries.tile.base.QuarryTile;
import ml.pkom.mcpitanlibarch.api.event.item.ItemUseOnBlockEvent;
import ml.pkom.mcpitanlibarch.api.item.CompatibleItemSettings;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.pitan76.mcpitanlib.api.util.TextUtil;

public class BedrockBreakModule extends MachineModule {
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
                    return e.pass();
                }
                quarry.setBedrockBreak(true);
                e.getStack().setCount(e.getStack().getCount() - 1);
                return e.success();
            }
        }
        if (world.getBlockState(blockPos).getBlock() instanceof Filler) {
            if (world.getBlockEntity(blockPos) != null && world.getBlockEntity(blockPos) instanceof FillerTile) {
                FillerTile filler = (FillerTile) world.getBlockEntity(blockPos);
                if (filler.canBedrockBreak()) {
                    e.getPlayer().sendMessage(TextUtil.translatable("message.enhanced_quarries.bedrock_break_module.1"));
                    return e.pass();
                }
                filler.setBedrockBreak(true);
                e.getStack().setCount(e.getStack().getCount() - 1);
                return e.success();
            }
        }
        return super.onRightClickOnBlock(e);
    }
}
