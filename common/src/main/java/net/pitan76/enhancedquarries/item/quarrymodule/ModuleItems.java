package net.pitan76.enhancedquarries.item.quarrymodule;

import net.pitan76.enhancedquarries.Items;
import net.pitan76.enhancedquarries.item.base.MachineModule;

public class ModuleItems {
    public static MachineModule BEDROCK_BREAK_MODULE;
    public static MachineModule SILK_TOUCH_MODULE;
    public static MachineModule LUCK_MODULE;
    public static MachineModule MOB_KILL_MODULE;
    public static MachineModule MOB_DELETE_MODULE;
    public static MachineModule EXP_COLLECT_MODULE;
    public static MachineModule DROP_REMOVAL_MODULE;

    // Items の値が入るのは登録後なので EnhancedQuarries.postInit() から呼ぶ
    public static void init() {
        BEDROCK_BREAK_MODULE = (MachineModule) Items.BEDROCK_BREAK_MODULE;
        SILK_TOUCH_MODULE = (MachineModule) Items.SILK_TOUCH_MODULE;
        LUCK_MODULE = (MachineModule) Items.LUCK_MODULE;
        MOB_KILL_MODULE = (MachineModule) Items.MOB_KILL_MODULE;
        MOB_DELETE_MODULE = (MachineModule) Items.MOB_DELETE_MODULE;
        EXP_COLLECT_MODULE = (MachineModule) Items.EXP_COLLECT_MODULE;
        DROP_REMOVAL_MODULE = (MachineModule) Items.DROPPED_ITEM_REMOVAL_MODULE;
    }
}
