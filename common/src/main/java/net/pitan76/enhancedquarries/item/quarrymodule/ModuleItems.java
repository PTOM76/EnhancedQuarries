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
        BEDROCK_BREAK_MODULE = Items.BEDROCK_BREAK_MODULE.getICompat();
        SILK_TOUCH_MODULE = Items.SILK_TOUCH_MODULE.getICompat();
        LUCK_MODULE = Items.LUCK_MODULE.getICompat();
        MOB_KILL_MODULE = Items.MOB_KILL_MODULE.getICompat();
        MOB_DELETE_MODULE = Items.MOB_DELETE_MODULE.getICompat();
        EXP_COLLECT_MODULE = Items.EXP_COLLECT_MODULE.getICompat();
        DROP_REMOVAL_MODULE = Items.DROPPED_ITEM_REMOVAL_MODULE.getICompat();
    }
}
