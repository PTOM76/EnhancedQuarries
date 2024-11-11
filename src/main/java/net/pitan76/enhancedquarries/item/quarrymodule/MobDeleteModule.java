package net.pitan76.enhancedquarries.item.quarrymodule;

import net.pitan76.enhancedquarries.item.base.MachineModule;
import net.pitan76.mcpitanlib.api.event.item.ItemUseOnBlockEvent;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.util.TextUtil;

public class MobDeleteModule extends MachineModule {
    public MobDeleteModule(CompatibleItemSettings settings) {
        super(settings);
    }

    @Override
    public boolean allowMultiple() {
        return false;
    }

    @Override
    public boolean checkConflict(ItemUseOnBlockEvent e, MachineModule module) {
        if (module instanceof MobKillModule) {
            e.getPlayer().sendMessage(TextUtil.translatable("message.enhanced_quarries.mob_delete_module.conflict"));
            return true;
        }

        return false;
    }
}
