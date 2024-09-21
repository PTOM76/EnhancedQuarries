package net.pitan76.enhancedquarries.item.quarrymodule;

import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import net.pitan76.enhancedquarries.block.base.Quarry;
import net.pitan76.enhancedquarries.item.base.MachineModule;
import net.pitan76.enhancedquarries.tile.base.QuarryTile;
import net.pitan76.mcpitanlib.api.event.item.ItemUseOnBlockEvent;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.TextUtil;
import net.pitan76.mcpitanlib.api.util.WorldUtil;

public class MobDeleteModule extends MachineModule {
    public MobDeleteModule(CompatibleItemSettings settings) {
        super(settings);
    }

    @Override
    public ActionResult onRightClickOnBlock(ItemUseOnBlockEvent e) {
        World world = e.getWorld();
        if (WorldUtil.isClient(world) || !(e.getBlockState().getBlock() instanceof Quarry) ||
                e.getBlockEntity() == null || !(e.getBlockEntity() instanceof QuarryTile))
            return super.onRightClickOnBlock(e);

        QuarryTile quarry = (QuarryTile) e.getBlockEntity();
        if (quarry.isSetMobDelete()) {
            e.getPlayer().sendMessage(TextUtil.translatable("message.enhanced_quarries.mob_delete_module.1"));
            return e.pass();
        }
        if (quarry.isSetMobKill()) {
            e.getPlayer().sendMessage(TextUtil.translatable("message.enhanced_quarries.mob_delete_module.2"));
            return e.pass();
        }
        quarry.setMobDeleteModule(true);
        ItemStackUtil.decrementCount(e.getStack(), 1);
        return e.success();
    }
}
