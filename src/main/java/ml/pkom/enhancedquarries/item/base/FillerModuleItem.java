package ml.pkom.enhancedquarries.item.base;

import ml.pkom.enhancedquarries.event.FillerModuleReturn;
import ml.pkom.enhancedquarries.event.FillerProcessEvent;
import ml.pkom.mcpitanlibarch.api.item.CompatibleItemSettings;

public abstract class FillerModuleItem extends FillerModule {

    // 範囲の中
    public FillerModuleReturn onProcessInRange(FillerProcessEvent e) {
        return FillerModuleReturn.NOTHING;
    }

    // 範囲の上
    public FillerModuleReturn onProcessOnRange(FillerProcessEvent e) {
        return FillerModuleReturn.NOTHING;
    }

    // 範囲の下
    public FillerModuleReturn onProcessUnderRange(FillerProcessEvent e) {
        return FillerModuleReturn.NOTHING;
    }

    public FillerModuleItem(CompatibleItemSettings settings) {
        super(settings);
    }
}
