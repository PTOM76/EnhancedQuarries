package ml.pkom.enhancedquarries.item.base;

import ml.pkom.enhancedquarries.event.FillerModuleReturn;
import ml.pkom.enhancedquarries.event.FillerProcessEvent;
import ml.pkom.mcpitanlibarch.api.item.CompatibleItemSettings;
import ml.pkom.mcpitanlibarch.api.item.ExtendItem;

public abstract class FillerModule extends ExtendItem {

    public FillerModule(CompatibleItemSettings settings) {
        super(settings);
    }

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
}
