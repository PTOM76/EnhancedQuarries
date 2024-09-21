package net.pitan76.enhancedquarries.item.quarrymodule;

import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import net.pitan76.enhancedquarries.block.base.Filler;
import net.pitan76.enhancedquarries.block.base.Quarry;
import net.pitan76.enhancedquarries.item.base.MachineModule;
import net.pitan76.enhancedquarries.tile.base.FillerTile;
import net.pitan76.enhancedquarries.tile.base.QuarryTile;
import net.pitan76.mcpitanlib.api.event.item.ItemUseOnBlockEvent;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.TextUtil;
import net.pitan76.mcpitanlib.api.util.WorldUtil;

public class BedrockBreakModule extends MachineModule {
    public BedrockBreakModule(CompatibleItemSettings settings) {
        super(settings);
    }

    @Override
    public ActionResult onRightClickOnBlock(ItemUseOnBlockEvent e) {
        World world = e.getWorld();
        if (WorldUtil.isClient(world))
            return super.onRightClickOnBlock(e);

        if (e.getBlockState().getBlock() instanceof Quarry
                && e.getBlockEntity() != null && e.getBlockEntity() instanceof QuarryTile) {

            QuarryTile quarry = (QuarryTile) e.getBlockEntity();
            if (quarry.canBedrockBreak()) {
                e.getPlayer().sendMessage(TextUtil.translatable("message.enhanced_quarries.bedrock_break_module.1"));
                return e.pass();
            }
            quarry.setBedrockBreak(true);
            ItemStackUtil.decrementCount(e.getStack(), 1);
            return e.success();
        }

        if (e.getBlockState().getBlock() instanceof Filler
                && e.getBlockEntity() != null && e.getBlockEntity() instanceof FillerTile) {

            FillerTile filler = (FillerTile) e.getBlockEntity();
            if (filler.canBedrockBreak()) {
                e.getPlayer().sendMessage(TextUtil.translatable("message.enhanced_quarries.bedrock_break_module.1"));
                return e.pass();
            }
            filler.setBedrockBreak(true);
            ItemStackUtil.decrementCount(e.getStack(), 1);
            return e.success();

        }
        return super.onRightClickOnBlock(e);
    }
}
