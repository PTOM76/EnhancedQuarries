package net.pitan76.enhancedquarries;

import net.minecraft.item.Item;
import net.pitan76.enhancedquarries.item.Blueprint;
import net.pitan76.enhancedquarries.item.RedstoneHammer;
import net.pitan76.enhancedquarries.item.WrenchItem;
import net.pitan76.enhancedquarries.item.base.FillerModule;
import net.pitan76.enhancedquarries.item.fillermodule.*;
import net.pitan76.enhancedquarries.item.quarrymodule.*;
import net.pitan76.mcpitanlib.api.item.v2.CompatItem;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.util.item.ItemUtil;

import static net.pitan76.enhancedquarries.EnhancedQuarries._id;
import static net.pitan76.enhancedquarries.EnhancedQuarries.registry;

public class Items {

    public static Item NORMAL_QUARRY;
    public static Item ENHANCED_QUARRY;
    public static Item FLUID_QUARRY;
    public static Item OPTIMUM_QUARRY;
    public static Item ENHANCED_OPTIMUM_QUARRY;
    public static Item FLUID_OPTIMUM_QUARRY;

    public static Item NORMAL_FILLER;
    public static Item ENHANCED_FILLER;
    public static Item ENHANCED_FILLER_WITH_CHEST;

    public static Item NORMAL_PUMP;
    public static Item ENHANCED_PUMP;

    public static Item NORMAL_SCANNER;
    public static Item NORMAL_BUILDER;
    public static Item NORMAL_LIBRARY;

    public static Item ENERGY_GENERATOR;

    public static Item NORMAL_MARKER;
    public static Item FRAME;


    public static Item BEDROCK_BREAK_MODULE;
    public static Item SILK_TOUCH_MODULE;
    public static Item LUCK_MODULE;
    public static Item MOB_KILL_MODULE;
    public static Item MOB_DELETE_MODULE;
    public static Item EXP_COLLECT_MODULE;
    public static Item DROPPED_ITEM_REMOVAL_MODULE;

    // 全配置
    public static FillerModule fillerALL_FILL;
    // 全消去
    public static FillerModule fillerALL_DELETE;
    // 全撤去
    public static FillerModule fillerALL_REMOVE;
    // 整地
    public static FillerModule fillerLEVELING;
    // ボックス
    public static FillerModule fillerBOX;
    // 壁
    public static FillerModule fillerWALL;
    // 松明
    public static FillerModule fillerTORCH;
    // 垂直レイヤー
    public static FillerModule fillerVERTICAL_LAYER;
    // 平行レイヤー
    public static FillerModule fillerHORIZONTAL_LAYER;
    // 積み上げ
    public static FillerModule fillerTOWER;
    // 液体除去(未実装)
    public static FillerModule fillerDELETE_FLUID;
    // 床張り替え
    public static FillerModule fillerFLOOR_REPLACE;
    // 階段
    public static FillerModule fillerSTAIRS;
    // 四角錐
    public static FillerModule fillerPYRAMID;
    // 階段撤去
    public static FillerModule fillerCUT_STAIRS;
    // 四角錐撤去
    public static FillerModule fillerCUT_PYRAMID;

    // 赤石ハンマー
    public static Item REDSTONE_HAMMER;

    // レンチ
    public static Item WRENCH;

    // 素材
    public static Item REFINED_GEAR;
    public static Item ENHANCED_REFINED_GEAR;
    public static Item ENHANCED_PANEL;

    // ----

    // 設計図
    public static Item BLUEPRINT;
    public static Item EMPTY_BLUEPRINT;

    // ゆっくり饅頭 (未実装)
    //public static Item REIMU_BUN = new ExtendItem(CompatibleItemSettings.of().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP).food(FoodComponents.COOKIE));
    //public static Item MARISA_BUN = new ExtendItem(CompatibleItemSettings.of().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP).food(FoodComponents.COOKIE));


    public static void init() {
        registry.registerItem(EnhancedQuarries._id("normal_quarry"), () -> NORMAL_QUARRY = ItemUtil.create(Blocks.NORMAL_QUARRY, CompatibleItemSettings.of(_id("normal_quarry")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("enhanced_quarry"), () -> ENHANCED_QUARRY = ItemUtil.create(Blocks.ENHANCED_QUARRY, CompatibleItemSettings.of(_id("enhanced_quarry")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("fluid_quarry"), () -> FLUID_QUARRY = ItemUtil.create(Blocks.FLUID_QUARRY, CompatibleItemSettings.of(_id("fluid_quarry")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("optimum_quarry"), () -> OPTIMUM_QUARRY = ItemUtil.create(Blocks.OPTIMUM_QUARRY, CompatibleItemSettings.of(_id("optimum_quarry")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("enhanced_optimum_quarry"), () -> ENHANCED_OPTIMUM_QUARRY = ItemUtil.create(Blocks.ENHANCED_OPTIMUM_QUARRY, CompatibleItemSettings.of(_id("enhanced_optimum_quarry")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("fluid_optimum_quarry"), () -> FLUID_OPTIMUM_QUARRY = ItemUtil.create(Blocks.FLUID_OPTIMUM_QUARRY, CompatibleItemSettings.of(_id("fluid_optimum_quarry")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));

        registry.registerItem(EnhancedQuarries._id("normal_filler"), () -> NORMAL_FILLER = ItemUtil.create(Blocks.NORMAL_FILLER, CompatibleItemSettings.of(_id("normal_filler")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("enhanced_filler"), () -> ENHANCED_FILLER = ItemUtil.create(Blocks.ENHANCED_FILLER, CompatibleItemSettings.of(_id("enhanced_filler")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("enhanced_filler_with_chest"), () -> ENHANCED_FILLER_WITH_CHEST = ItemUtil.create(Blocks.ENHANCED_FILLER_WITH_CHEST, CompatibleItemSettings.of(_id("enhanced_filler_with_chest")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));

        registry.registerItem(EnhancedQuarries._id("normal_pump"), () -> NORMAL_PUMP = ItemUtil.create(Blocks.NORMAL_PUMP, CompatibleItemSettings.of(_id("normal_pump")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("enhanced_pump"), () -> ENHANCED_PUMP = ItemUtil.create(Blocks.ENHANCED_PUMP, CompatibleItemSettings.of(_id("enhanced_pump")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));

        registry.registerItem(EnhancedQuarries._id("normal_scanner"), () -> NORMAL_SCANNER = ItemUtil.create(Blocks.NORMAL_SCANNER, CompatibleItemSettings.of(_id("normal_scanner")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("normal_builder"), () -> NORMAL_BUILDER = ItemUtil.create(Blocks.NORMAL_BUILDER, CompatibleItemSettings.of(_id("normal_builder")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("normal_library"), () -> NORMAL_LIBRARY = ItemUtil.create(Blocks.NORMAL_LIBRARY, CompatibleItemSettings.of(_id("normal_library")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));

        registry.registerItem(EnhancedQuarries._id("energy_generator"), () -> ENERGY_GENERATOR = ItemUtil.create(Blocks.ENERGY_GENERATOR, CompatibleItemSettings.of(_id("energy_generator")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));

        registry.registerItem(EnhancedQuarries._id("normal_marker"), () -> NORMAL_MARKER = ItemUtil.create(Blocks.NORMAL_MARKER, CompatibleItemSettings.of(_id("normal_marker")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("frame"), () -> FRAME = ItemUtil.create(Blocks.FRAME, CompatibleItemSettings.of(_id("frame")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));


        registry.registerItem(EnhancedQuarries._id("bedrock_break_module"), () -> BEDROCK_BREAK_MODULE = new BedrockBreakModule(CompatibleItemSettings.of(_id("bedrock_break_module")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("silk_touch_module"), () -> SILK_TOUCH_MODULE = new SilkTouchModule(CompatibleItemSettings.of(_id("silk_touch_module")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("luck_enchant_module"), () -> LUCK_MODULE = new LuckEnchantModule(CompatibleItemSettings.of(_id("luck_enchant_module")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("mob_kill_module"), () -> MOB_KILL_MODULE = new MobKillModule(CompatibleItemSettings.of(_id("mob_kill_module")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("mob_delete_module"), () -> MOB_DELETE_MODULE = new MobDeleteModule(CompatibleItemSettings.of(_id("mob_delete_module")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("exp_collect_module"), () -> EXP_COLLECT_MODULE = new ExpCollectModule(CompatibleItemSettings.of(_id("exp_collect_module")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("dropped_item_removal_module"), () -> DROPPED_ITEM_REMOVAL_MODULE = new DropRemovalModule(CompatibleItemSettings.of(_id("dropped_item_removal_module")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));

        registry.registerItem(EnhancedQuarries._id("filler_all_fill"), () -> fillerALL_FILL = new AllFillModule(CompatibleItemSettings.of(_id("filler_all_fill")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("filler_all_delete"), () -> fillerALL_DELETE = new AllDeleteModule(CompatibleItemSettings.of(_id("filler_all_delete")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("filler_all_remove"), () -> fillerALL_REMOVE = new AllRemoveModule(CompatibleItemSettings.of(_id("filler_all_remove")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("filler_leveling"), () -> fillerLEVELING = new LevelingModule(CompatibleItemSettings.of(_id("filler_leveling")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("filler_create_box"), () -> fillerBOX = new BoxModule(CompatibleItemSettings.of(_id("filler_create_box")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("filler_create_wall"), () -> fillerWALL = new WallModule(CompatibleItemSettings.of(_id("filler_create_wall")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("filler_place_torch"), () -> fillerTORCH = new TorchModule(CompatibleItemSettings.of(_id("filler_place_torch")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("filler_vertical_layer"), () -> fillerVERTICAL_LAYER = new VerticalLayerModule(CompatibleItemSettings.of(_id("filler_vertical_layer")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("filler_horizontal_layer"), () -> fillerHORIZONTAL_LAYER = new HorizontalLayerModule(CompatibleItemSettings.of(_id("filler_horizontal_layer")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("filler_tower"), () -> fillerTOWER = new TowerModule(CompatibleItemSettings.of(_id("filler_tower")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("filler_delete_fluid"), () -> fillerDELETE_FLUID = new DeleteFluidModule(CompatibleItemSettings.of(_id("filler_delete_fluid")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("filler_floor_replace"), () -> fillerFLOOR_REPLACE = new FloorReplaceModule(CompatibleItemSettings.of(_id("filler_floor_replace")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("filler_create_stairs"), () -> fillerSTAIRS = new CreateStairsModule(CompatibleItemSettings.of(_id("filler_create_stairs")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("filler_create_pyramid"), () -> fillerPYRAMID = new CreatePyramidModule(CompatibleItemSettings.of(_id("filler_create_pyramid")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("filler_cut_stairs"), () -> fillerCUT_STAIRS = new CutStairsModule(CompatibleItemSettings.of(_id("filler_cut_stairs")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("filler_cut_pyramid"), () -> fillerCUT_PYRAMID = new CutPyramidModule(CompatibleItemSettings.of(_id("filler_cut_pyramid")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));

        registry.registerItem(EnhancedQuarries._id("redstone_hammer"), () -> REDSTONE_HAMMER = new RedstoneHammer(CompatibleItemSettings.of(_id("redstone_hammer")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("wrench"), () -> WRENCH = new WrenchItem(CompatibleItemSettings.of(_id("wrench")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));

        registry.registerItem(EnhancedQuarries._id("refined_gear"), () -> REFINED_GEAR = new CompatItem(CompatibleItemSettings.of(_id("refined_gear")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("enhanced_refined_gear"), () -> ENHANCED_REFINED_GEAR = new CompatItem(CompatibleItemSettings.of(_id("enhanced_refined_gear")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("enhanced_panel"), () -> ENHANCED_PANEL = new CompatItem(CompatibleItemSettings.of(_id("enhanced_panel")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));

        registry.registerItem(EnhancedQuarries._id("empty_blueprint"), () -> EMPTY_BLUEPRINT = new Blueprint(CompatibleItemSettings.of(_id("empty_blueprint")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        registry.registerItem(EnhancedQuarries._id("blueprint"), () -> BLUEPRINT = new Blueprint(CompatibleItemSettings.of(_id("blueprint")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));

        //registry.registerItem(EnhancedQuarries.id("reimu_bun"), () -> REIMU_BUN);
        //registry.registerItem(EnhancedQuarries.id("marisa_bun"), () -> MARISA_BUN);
    }
}
