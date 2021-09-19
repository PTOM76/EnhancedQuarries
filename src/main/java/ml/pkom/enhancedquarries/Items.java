package ml.pkom.enhancedquarries;

import ml.pkom.enhancedquarries.item.*;
import ml.pkom.enhancedquarries.item.base.FillerModule;
import ml.pkom.enhancedquarries.item.base.FillerModuleItem;
import ml.pkom.enhancedquarries.item.fillermodule.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class Items {

    public static Item NORMAL_QUARRY = new BlockItem(Blocks.NORMAL_QUARRY, new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    public static Item ENHANCED_QUARRY = new BlockItem(Blocks.ENHANCED_QUARRY, new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    public static Item FLUID_QUARRY = new BlockItem(Blocks.FLUID_QUARRY, new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));

    public static Item NORMAL_FILLER = new BlockItem(Blocks.NORMAL_FILLER, new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    public static Item ENHANCED_FILLER = new BlockItem(Blocks.ENHANCED_FILLER, new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));

    public static Item NORMAL_MARKER = new BlockItem(Blocks.NORMAL_MARKER, new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    public static Item FRAME = new BlockItem(Blocks.FRAME, new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    public static Item BEDROCK_BREAK_MODULE = new BedrockBreakModule(new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    public static Item SILK_TOUCH_MODULE = new SilkTouchModule(new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    public static Item LUCK_MODULE = new LuckEnchantModule(new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    public static Item MOB_KILL_MODULE = new MobKillModule(new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    public static Item MOB_DELETE_MODULE = new MobDeleteModule(new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));

    // 全配置
    public static Item fillerALL_FILL = new FillerModule(new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    // 全消去
    public static Item fillerALL_DELETE = new FillerModule(new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    // 全撤去
    public static Item fillerALL_REMOVE = new FillerModule(new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    // 整地
    public static Item fillerLEVELING = new FillerModule(new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    // ボックス
    public static Item fillerBOX = new FillerModule(new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    // 壁
    public static Item fillerWALL = new FillerModule(new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    // 松明
    public static Item fillerTORCH = new FillerModule(new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    // 垂直レイヤー
    public static FillerModuleItem fillerVERTICAL_LAYER = new VerticalLayerModule(new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    // 平行レイヤー
    public static FillerModuleItem fillerHORIZONTAL_LAYER = new HorizontalLayerModule(new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    // 積み上げ
    public static FillerModuleItem fillerTOWER = new TowerModule(new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    // 液体除去(未実装)
    public static FillerModuleItem fillerDELETE_FLUID = new DeleteFluidModule(new FabricItemSettings()); //.group(EnhancedQuarries.FILLER_PLUS_GROUP)
    // 床張り替え
    public static FillerModuleItem fillerFLOOR_REPLACE = new FloorReplaceModule(new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));



    // 素材
    public static Item REFINED_GEAR = new Item(new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    public static Item ENHANCED_REFINED_GEAR = new Item(new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    public static Item ENHANCED_PANEL = new Item(new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));

    // ----

    public static void init() {
        Registry.register(Registry.ITEM, EnhancedQuarries.id("normal_quarry"), NORMAL_QUARRY);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("enhanced_quarry"), ENHANCED_QUARRY);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("fluid_quarry"), FLUID_QUARRY);

        Registry.register(Registry.ITEM, EnhancedQuarries.id("normal_filler"), NORMAL_FILLER);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("enhanced_filler"), ENHANCED_FILLER);

        Registry.register(Registry.ITEM, EnhancedQuarries.id("normal_marker"), NORMAL_MARKER);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("frame"), FRAME);

        Registry.register(Registry.ITEM, EnhancedQuarries.id("bedrock_break_module"), BEDROCK_BREAK_MODULE);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("silk_touch_module"), SILK_TOUCH_MODULE);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("luck_enchant_module"), LUCK_MODULE);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("mob_kill_module"), MOB_KILL_MODULE);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("mob_delete_module"), MOB_DELETE_MODULE);

        Registry.register(Registry.ITEM, EnhancedQuarries.id("filler_all_fill"), fillerALL_FILL);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("filler_all_delete"), fillerALL_DELETE);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("filler_all_remove"), fillerALL_REMOVE);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("filler_leveling"), fillerLEVELING);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("filler_create_box"), fillerBOX);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("filler_create_wall"), fillerWALL);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("filler_place_torch"), fillerTORCH);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("filler_vertical_layer"), fillerVERTICAL_LAYER);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("filler_horizontal_layer"), fillerHORIZONTAL_LAYER);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("filler_tower"), fillerTOWER);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("filler_delete_fluid"), fillerDELETE_FLUID);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("filler_floor_replace"), fillerFLOOR_REPLACE);

        Registry.register(Registry.ITEM, EnhancedQuarries.id("refined_gear"), REFINED_GEAR);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("enhanced_refined_gear"), ENHANCED_REFINED_GEAR);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("enhanced_panel"), ENHANCED_PANEL);
    }
}
