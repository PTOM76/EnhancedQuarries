package net.pitan76.enhancedquarries.item.quarrymodule;

import net.pitan76.enhancedquarries.item.base.MachineModule;
import net.pitan76.enhancedquarries.tile.base.FillerTile;
import net.pitan76.mcpitanlib.api.event.item.ItemUseOnBlockEvent;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.util.CompatActionResult;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.TextUtil;

public class BedrockBreakModule extends MachineModule {
    public BedrockBreakModule(CompatibleItemSettings settings) {
        super(settings);
    }

    @Override
    public boolean allowMultiple() {
        return false;
    }

    @Override
    public CompatActionResult onRightClickOnFiller(ItemUseOnBlockEvent e, FillerTile filler) {
        if (filler.canBedrockBreak()) {
            e.getPlayer().sendMessage(TextUtil.translatable("message.enhanced_quarries.bedrock_break_module.1"));
            return e.pass();
        }
        filler.setBedrockBreak(true);
        ItemStackUtil.decrementCount(e.stack, 1);
        return e.success();
    }
}
