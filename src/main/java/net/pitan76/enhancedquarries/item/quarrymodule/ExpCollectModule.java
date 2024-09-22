package net.pitan76.enhancedquarries.item.quarrymodule;

import net.pitan76.enhancedquarries.item.base.MachineModule;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;

public class ExpCollectModule extends MachineModule {
    public ExpCollectModule(CompatibleItemSettings settings) {
        super(settings);
    }

    @Override
    public boolean allowMultiple() {
        return false;
    }
}
