package ml.pkom.enhancedquarries;

import ml.pkom.enhancedquarries.item.*;
import ml.pkom.enhancedquarries.item.base.FillerModule;
import ml.pkom.enhancedquarries.item.base.FillerModuleItem;
import ml.pkom.enhancedquarries.item.fillermodule.*;
import ml.pkom.mcpitanlibarch.api.item.ExtendSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponents;
import net.minecraft.item.Item;

import static ml.pkom.enhancedquarries.EnhancedQuarries.registry;

public class Items {

    public static Item NORMAL_QUARRY = new BlockItem(Blocks.NORMAL_QUARRY, new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("normal_quarry")));
    public static Item ENHANCED_QUARRY = new BlockItem(Blocks.ENHANCED_QUARRY, new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("enhanced_quarry")));
    public static Item FLUID_QUARRY = new BlockItem(Blocks.FLUID_QUARRY, new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("fluid_quarry")));
    public static Item OPTIMUM_QUARRY = new BlockItem(Blocks.OPTIMUM_QUARRY, new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("optimum_quarry")));
    public static Item ENHANCED_OPTIMUM_QUARRY = new BlockItem(Blocks.ENHANCED_OPTIMUM_QUARRY, new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("enhanced_optimum_quarry")));
    public static Item FLUID_OPTIMUM_QUARRY = new BlockItem(Blocks.FLUID_OPTIMUM_QUARRY, new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("fluid_optimum_quarry")));

    public static Item NORMAL_FILLER = new BlockItem(Blocks.NORMAL_FILLER, new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("normal_filler")));
    public static Item ENHANCED_FILLER = new BlockItem(Blocks.ENHANCED_FILLER, new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("enhanced_filler")));
    public static Item ENHANCED_FILLER_WITH_CHEST = new BlockItem(Blocks.ENHANCED_FILLER_WITH_CHEST, new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("enhanced_filler_with_chest")));

    public static Item NORMAL_PUMP = new BlockItem(Blocks.NORMAL_PUMP, new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("normal_pump")));
    public static Item ENHANCED_PUMP = new BlockItem(Blocks.ENHANCED_PUMP, new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("enhanced_pump")));

    public static Item NORMAL_SCANNER = new BlockItem(Blocks.NORMAL_SCANNER, new ExtendSettings()); //.addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("normal_scanner")));
    public static Item NORMAL_BUILDER = new BlockItem(Blocks.NORMAL_BUILDER, new ExtendSettings()); //.addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("normal_builder")));
    public static Item NORMAL_LIBRARY = new BlockItem(Blocks.NORMAL_LIBRARY, new ExtendSettings()); //.addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("normal_library")));


    public static Item NORMAL_MARKER = new BlockItem(Blocks.NORMAL_MARKER, new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("normal_marker")));
    public static Item FRAME = new BlockItem(Blocks.FRAME, new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("frame")));
    public static Item BEDROCK_BREAK_MODULE = new BedrockBreakModule(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("bedrock_break_module")));
    public static Item SILK_TOUCH_MODULE = new SilkTouchModule(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("silk_touch_module")));
    public static Item LUCK_MODULE = new LuckEnchantModule(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("luck_enchant_module")));
    public static Item MOB_KILL_MODULE = new MobKillModule(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("mob_kill_module")));
    public static Item MOB_DELETE_MODULE = new MobDeleteModule(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("mob_delete_module")));

    // 全配置
    public static Item fillerALL_FILL = new FillerModule(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("filler_all_fill")));
    // 全消去
    public static Item fillerALL_DELETE = new FillerModule(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("filler_all_delete")));
    // 全撤去
    public static Item fillerALL_REMOVE = new FillerModule(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("filler_all_remove")));
    // 整地
    public static Item fillerLEVELING = new FillerModule(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("filler_leveling")));
    // ボックス
    public static Item fillerBOX = new FillerModule(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("filler_create_box")));
    // 壁
    public static Item fillerWALL = new FillerModule(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("filler_create_wall")));
    // 松明
    public static Item fillerTORCH = new FillerModule(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("filler_place_torch")));
    // 垂直レイヤー
    public static FillerModuleItem fillerVERTICAL_LAYER = new VerticalLayerModule(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("filler_vertical_layer")));
    // 平行レイヤー
    public static FillerModuleItem fillerHORIZONTAL_LAYER = new HorizontalLayerModule(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("filler_horizontal_layer")));
    // 積み上げ
    public static FillerModuleItem fillerTOWER = new TowerModule(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("filler_tower")));
    // 液体除去(未実装)
    public static FillerModuleItem fillerDELETE_FLUID = new DeleteFluidModule(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("filler_delete_fluid")));
    // 床張り替え
    public static FillerModuleItem fillerFLOOR_REPLACE = new FloorReplaceModule(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("filler_floor_replace")));
    // 階段
    public static FillerModuleItem fillerSTAIRS = new CreateStairsModule(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("filler_create_stairs")));
    // 四角錐
    public static FillerModuleItem fillerPYRAMID = new CreatePyramidModule(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("filler_create_pyramid")));
    // 階段撤去
    public static FillerModuleItem fillerCUT_STAIRS = new CutStairsModule(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("filler_cut_stairs")));
    // 四角錐撤去
    public static FillerModuleItem fillerCUT_PYRAMID = new CutPyramidModule(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("filler_cut_pyramid")));

    // 赤石ハンマー
    public static Item REDSTONE_HAMMER = new RedstoneHammer(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("redstone_hammer")));

    // 素材
    public static Item REFINED_GEAR = new Item(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("refined_gear")));
    public static Item ENHANCED_REFINED_GEAR = new Item(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("enhanced_refined_gear")));
    public static Item ENHANCED_PANEL = new Item(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("enhanced_panel")));

    // ----

    // 設計図
    public static Item BLUEPRINT = new Blueprint(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("blueprint")));
    public static Item EMPTY_BLUEPRINT = new Blueprint(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("empty_blueprint")));

    // ゆっくり饅頭
    public static Item REIMU_BUN = new Item(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("reimu_bun")).food(FoodComponents.COOKIE));
    public static Item MARISA_BUN = new Item(new ExtendSettings().addGroup(EnhancedQuarries.FILLER_PLUS_GROUP, EnhancedQuarries.id("marisa_bun")).food(FoodComponents.COOKIE));


    public static void init() {
        registry.registerItem(EnhancedQuarries.id("normal_quarry"), () -> NORMAL_QUARRY);
        registry.registerItem(EnhancedQuarries.id("enhanced_quarry"), () -> ENHANCED_QUARRY);
        registry.registerItem(EnhancedQuarries.id("fluid_quarry"), () -> FLUID_QUARRY);
        registry.registerItem(EnhancedQuarries.id("optimum_quarry"), () -> OPTIMUM_QUARRY);
        registry.registerItem(EnhancedQuarries.id("enhanced_optimum_quarry"), () -> ENHANCED_OPTIMUM_QUARRY);
        registry.registerItem(EnhancedQuarries.id("fluid_optimum_quarry"), () -> FLUID_OPTIMUM_QUARRY);

        registry.registerItem(EnhancedQuarries.id("normal_filler"), () -> NORMAL_FILLER);
        registry.registerItem(EnhancedQuarries.id("enhanced_filler"), () -> ENHANCED_FILLER);
        registry.registerItem(EnhancedQuarries.id("enhanced_filler_with_chest"), () -> ENHANCED_FILLER_WITH_CHEST);

        registry.registerItem(EnhancedQuarries.id("normal_pump"), () -> NORMAL_PUMP);
        registry.registerItem(EnhancedQuarries.id("enhanced_pump"), () -> ENHANCED_PUMP);

        registry.registerItem(EnhancedQuarries.id("normal_scanner"), () -> NORMAL_SCANNER);
        registry.registerItem(EnhancedQuarries.id("normal_builder"), () -> NORMAL_BUILDER);
        registry.registerItem(EnhancedQuarries.id("normal_library"), () -> NORMAL_LIBRARY);

        registry.registerItem(EnhancedQuarries.id("normal_marker"), () -> NORMAL_MARKER);
        registry.registerItem(EnhancedQuarries.id("frame"), () -> FRAME);

        registry.registerItem(EnhancedQuarries.id("bedrock_break_module"), () -> BEDROCK_BREAK_MODULE);
        registry.registerItem(EnhancedQuarries.id("silk_touch_module"), () -> SILK_TOUCH_MODULE);
        registry.registerItem(EnhancedQuarries.id("luck_enchant_module"), () -> LUCK_MODULE);
        registry.registerItem(EnhancedQuarries.id("mob_kill_module"), () -> MOB_KILL_MODULE);
        registry.registerItem(EnhancedQuarries.id("mob_delete_module"), () -> MOB_DELETE_MODULE);

        registry.registerItem(EnhancedQuarries.id("filler_all_fill"), () -> fillerALL_FILL);
        registry.registerItem(EnhancedQuarries.id("filler_all_delete"), () -> fillerALL_DELETE);
        registry.registerItem(EnhancedQuarries.id("filler_all_remove"), () -> fillerALL_REMOVE);
        registry.registerItem(EnhancedQuarries.id("filler_leveling"), () -> fillerLEVELING);
        registry.registerItem(EnhancedQuarries.id("filler_create_box"), () -> fillerBOX);
        registry.registerItem(EnhancedQuarries.id("filler_create_wall"), () -> fillerWALL);
        registry.registerItem(EnhancedQuarries.id("filler_place_torch"), () -> fillerTORCH);
        registry.registerItem(EnhancedQuarries.id("filler_vertical_layer"), () -> fillerVERTICAL_LAYER);
        registry.registerItem(EnhancedQuarries.id("filler_horizontal_layer"), () -> fillerHORIZONTAL_LAYER);
        registry.registerItem(EnhancedQuarries.id("filler_tower"), () -> fillerTOWER);
        registry.registerItem(EnhancedQuarries.id("filler_delete_fluid"), () -> fillerDELETE_FLUID);
        registry.registerItem(EnhancedQuarries.id("filler_floor_replace"), () -> fillerFLOOR_REPLACE);
        registry.registerItem(EnhancedQuarries.id("filler_create_stairs"), () -> fillerSTAIRS);
        registry.registerItem(EnhancedQuarries.id("filler_create_pyramid"), () -> fillerPYRAMID);
        registry.registerItem(EnhancedQuarries.id("filler_cut_stairs"), () -> fillerCUT_STAIRS);
        registry.registerItem(EnhancedQuarries.id("filler_cut_pyramid"), () -> fillerCUT_PYRAMID);

        registry.registerItem(EnhancedQuarries.id("redstone_hammer"), () -> REDSTONE_HAMMER);

        registry.registerItem(EnhancedQuarries.id("refined_gear"), () -> REFINED_GEAR);
        registry.registerItem(EnhancedQuarries.id("enhanced_refined_gear"), () -> ENHANCED_REFINED_GEAR);
        registry.registerItem(EnhancedQuarries.id("enhanced_panel"), () -> ENHANCED_PANEL);

        registry.registerItem(EnhancedQuarries.id("empty_blueprint"), () -> EMPTY_BLUEPRINT);
        registry.registerItem(EnhancedQuarries.id("blueprint"), () -> BLUEPRINT);

        registry.registerItem(EnhancedQuarries.id("reimu_bun"), () -> REIMU_BUN);
        registry.registerItem(EnhancedQuarries.id("marisa_bun"), () -> MARISA_BUN);
    }
}
