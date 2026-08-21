package net.pitan76.enhancedquarries;

import net.pitan76.enhancedquarries.block.*;
import net.pitan76.enhancedquarries.block.base.EnergyGenerator;
import net.pitan76.enhancedquarries.block.base.Quarry;
import net.pitan76.enhancedquarries.item.Blueprint;
import net.pitan76.enhancedquarries.item.Template;
import net.pitan76.enhancedquarries.item.RedstoneHammer;
import net.pitan76.enhancedquarries.item.WrenchItem;
import net.pitan76.enhancedquarries.item.base.FillerModule;
import net.pitan76.enhancedquarries.item.base.MachineModule;
import net.pitan76.enhancedquarries.item.fillermodule.*;
import net.pitan76.enhancedquarries.item.quarrymodule.*;
import net.pitan76.mcpitanlib.api.item.v2.CompatItem;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.midohra.item.ITypedItemWrapper;
import net.pitan76.mcpitanlib.midohra.item.ItemWrapper;
import net.pitan76.mcpitanlib.midohra.item.TypedBlockItemWrapper;

import static net.pitan76.enhancedquarries.EnhancedQuarries.*;

public class Items {

    public static TypedBlockItemWrapper<NormalQuarry> NORMAL_QUARRY;
    public static TypedBlockItemWrapper<EnhancedQuarry> ENHANCED_QUARRY;
    public static TypedBlockItemWrapper<FluidQuarry> FLUID_QUARRY;
    public static TypedBlockItemWrapper<OptimumQuarry> OPTIMUM_QUARRY;
    public static TypedBlockItemWrapper<EnhancedOptimumQuarry> ENHANCED_OPTIMUM_QUARRY;
    public static TypedBlockItemWrapper<FluidOptimumQuarry> FLUID_OPTIMUM_QUARRY;

    public static TypedBlockItemWrapper<NormalFiller> NORMAL_FILLER;
    public static TypedBlockItemWrapper<EnhancedFiller> ENHANCED_FILLER;
    public static TypedBlockItemWrapper<EnhancedFillerWithChest> ENHANCED_FILLER_WITH_CHEST;

    public static TypedBlockItemWrapper<NormalPump> NORMAL_PUMP;
    public static TypedBlockItemWrapper<EnhancedPump> ENHANCED_PUMP;

    public static TypedBlockItemWrapper<NormalScanner> NORMAL_SCANNER;
    public static TypedBlockItemWrapper<NormalBuilder> NORMAL_BUILDER;
    public static TypedBlockItemWrapper<NormalLibrary> NORMAL_LIBRARY;

    public static TypedBlockItemWrapper<EnergyGenerator> ENERGY_GENERATOR;

    public static TypedBlockItemWrapper<NormalMarker> NORMAL_MARKER;
    public static TypedBlockItemWrapper<Frame> FRAME;


    public static ITypedItemWrapper<MachineModule> BEDROCK_BREAK_MODULE;
    public static ITypedItemWrapper<MachineModule> SILK_TOUCH_MODULE;
    public static ITypedItemWrapper<MachineModule> LUCK_MODULE;
    public static ITypedItemWrapper<MachineModule> MOB_KILL_MODULE;
    public static ITypedItemWrapper<MachineModule> MOB_DELETE_MODULE;
    public static ITypedItemWrapper<MachineModule> EXP_COLLECT_MODULE;
    public static ITypedItemWrapper<MachineModule> DROPPED_ITEM_REMOVAL_MODULE;

    // 全配置
    public static ITypedItemWrapper<FillerModule> fillerALL_FILL;
    // 全消去
    public static ITypedItemWrapper<FillerModule> fillerALL_DELETE;
    // 全撤去
    public static ITypedItemWrapper<FillerModule> fillerALL_REMOVE;
    // 整地
    public static ITypedItemWrapper<FillerModule> fillerLEVELING;
    // ボックス
    public static ITypedItemWrapper<FillerModule> fillerBOX;
    // 壁
    public static ITypedItemWrapper<FillerModule> fillerWALL;
    // 松明
    public static ITypedItemWrapper<FillerModule> fillerTORCH;
    // 垂直レイヤー
    public static ITypedItemWrapper<FillerModule> fillerVERTICAL_LAYER;
    // 平行レイヤー
    public static ITypedItemWrapper<FillerModule> fillerHORIZONTAL_LAYER;
    // 積み上げ
    public static ITypedItemWrapper<FillerModule> fillerTOWER;
    // 液体除去(未実装)
    public static ITypedItemWrapper<FillerModule> fillerDELETE_FLUID;
    // 床張り替え
    public static ITypedItemWrapper<FillerModule> fillerFLOOR_REPLACE;
    // 階段
    public static ITypedItemWrapper<FillerModule> fillerSTAIRS;
    // 四角錐
    public static ITypedItemWrapper<FillerModule> fillerPYRAMID;
    // 階段撤去
    public static ITypedItemWrapper<FillerModule> fillerCUT_STAIRS;
    // 四角錐撤去
    public static ITypedItemWrapper<FillerModule> fillerCUT_PYRAMID;

    // 赤石ハンマー
    public static ItemWrapper REDSTONE_HAMMER;

    // レンチ
    public static ItemWrapper WRENCH;

    // 素材
    public static ItemWrapper REFINED_GEAR;
    public static ItemWrapper ENHANCED_REFINED_GEAR;
    public static ItemWrapper ENHANCED_PANEL;

    // ----

    // 設計図
    public static ItemWrapper BLUEPRINT;
    public static ItemWrapper EMPTY_BLUEPRINT;

    // テンプレート
    public static ItemWrapper TEMPLATE;
    public static ItemWrapper EMPTY_TEMPLATE;

    // ゆっくり饅頭 (未実装)
    //public static Item REIMU_BUN = new ExtendItem(CompatibleItemSettings.of().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP).food(FoodComponents.COOKIE));
    //public static Item MARISA_BUN = new ExtendItem(CompatibleItemSettings.of().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP).food(FoodComponents.COOKIE));


    public static void init() {
        NORMAL_QUARRY = registry2.registerBlockItem(EnhancedQuarries._id("normal_quarry"), Blocks.NORMAL_QUARRY, CompatibleItemSettings.of(_id("normal_quarry")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
        ENHANCED_QUARRY = registry2.registerBlockItem(_id("enhanced_quarry"), Blocks.ENHANCED_QUARRY, CompatibleItemSettings.of(_id("enhanced_quarry")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
        FLUID_QUARRY = registry2.registerBlockItem(_id("fluid_quarry"), Blocks.FLUID_QUARRY, CompatibleItemSettings.of(_id("fluid_quarry")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
        OPTIMUM_QUARRY = registry2.registerBlockItem(_id("optimum_quarry"), Blocks.OPTIMUM_QUARRY, CompatibleItemSettings.of(_id("optimum_quarry")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
        ENHANCED_OPTIMUM_QUARRY = registry2.registerBlockItem(_id("enhanced_optimum_quarry"), Blocks.ENHANCED_OPTIMUM_QUARRY, CompatibleItemSettings.of(_id("enhanced_optimum_quarry")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
        FLUID_OPTIMUM_QUARRY = registry2.registerBlockItem(_id("fluid_optimum_quarry"), Blocks.FLUID_OPTIMUM_QUARRY, CompatibleItemSettings.of(_id("fluid_optimum_quarry")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));

        NORMAL_FILLER = registry2.registerBlockItem(_id("normal_filler"), Blocks.NORMAL_FILLER, CompatibleItemSettings.of(_id("normal_filler")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
        ENHANCED_FILLER = registry2.registerBlockItem(_id("enhanced_filler"), Blocks.ENHANCED_FILLER, CompatibleItemSettings.of(_id("enhanced_filler")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
        ENHANCED_FILLER_WITH_CHEST = registry2.registerBlockItem(_id("enhanced_filler_with_chest"), Blocks.ENHANCED_FILLER_WITH_CHEST, CompatibleItemSettings.of(_id("enhanced_filler_with_chest")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));

        NORMAL_PUMP = registry2.registerBlockItem(_id("normal_pump"), Blocks.NORMAL_PUMP, CompatibleItemSettings.of(_id("normal_pump")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
        ENHANCED_PUMP = registry2.registerBlockItem(_id("enhanced_pump"), Blocks.ENHANCED_PUMP, CompatibleItemSettings.of(_id("enhanced_pump")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));

        NORMAL_SCANNER = registry2.registerBlockItem(_id("normal_scanner"), Blocks.NORMAL_SCANNER, CompatibleItemSettings.of(_id("normal_scanner")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
        NORMAL_BUILDER = registry2.registerBlockItem(_id("normal_builder"), Blocks.NORMAL_BUILDER, CompatibleItemSettings.of(_id("normal_builder")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
        NORMAL_LIBRARY = registry2.registerBlockItem(_id("normal_library"), Blocks.NORMAL_LIBRARY, CompatibleItemSettings.of(_id("normal_library")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));

        ENERGY_GENERATOR = registry2.registerBlockItem(_id("energy_generator"), Blocks.ENERGY_GENERATOR, CompatibleItemSettings.of(_id("energy_generator")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));

        NORMAL_MARKER = registry2.registerBlockItem(_id("normal_marker"), Blocks.NORMAL_MARKER, CompatibleItemSettings.of(_id("normal_marker")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
        FRAME = registry2.registerBlockItem(_id("frame"), Blocks.FRAME, CompatibleItemSettings.of(_id("frame")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));


        BEDROCK_BREAK_MODULE = registry2.registerItem(_id("bedrock_break_module"), () -> new BedrockBreakModule(CompatibleItemSettings.of(_id("bedrock_break_module")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        SILK_TOUCH_MODULE = registry2.registerItem(_id("silk_touch_module"), () -> new SilkTouchModule(CompatibleItemSettings.of(_id("silk_touch_module")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        LUCK_MODULE = registry2.registerItem(_id("luck_enchant_module"), () -> new LuckEnchantModule(CompatibleItemSettings.of(_id("luck_enchant_module")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        MOB_KILL_MODULE = registry2.registerItem(_id("mob_kill_module"), () -> new MobKillModule(CompatibleItemSettings.of(_id("mob_kill_module")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        MOB_DELETE_MODULE = registry2.registerItem(_id("mob_delete_module"), () -> new MobDeleteModule(CompatibleItemSettings.of(_id("mob_delete_module")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        EXP_COLLECT_MODULE = registry2.registerItem(_id("exp_collect_module"), () -> new ExpCollectModule(CompatibleItemSettings.of(_id("exp_collect_module")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        DROPPED_ITEM_REMOVAL_MODULE = registry2.registerItem(_id("dropped_item_removal_module"), () -> new DropRemovalModule(CompatibleItemSettings.of(_id("dropped_item_removal_module")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));

        fillerALL_FILL = registry2.registerItem(_id("filler_all_fill"), () -> new AllFillModule(CompatibleItemSettings.of(_id("filler_all_fill")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        fillerALL_DELETE = registry2.registerItem(_id("filler_all_delete"), () -> new AllDeleteModule(CompatibleItemSettings.of(_id("filler_all_delete")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        fillerALL_REMOVE = registry2.registerItem(_id("filler_all_remove"), () -> new AllRemoveModule(CompatibleItemSettings.of(_id("filler_all_remove")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        fillerLEVELING = registry2.registerItem(_id("filler_leveling"), () -> new LevelingModule(CompatibleItemSettings.of(_id("filler_leveling")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        fillerBOX = registry2.registerItem(_id("filler_create_box"), () -> new BoxModule(CompatibleItemSettings.of(_id("filler_create_box")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        fillerWALL = registry2.registerItem(_id("filler_create_wall"), () -> new WallModule(CompatibleItemSettings.of(_id("filler_create_wall")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        fillerTORCH = registry2.registerItem(_id("filler_place_torch"), () -> new TorchModule(CompatibleItemSettings.of(_id("filler_place_torch")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        fillerVERTICAL_LAYER = registry2.registerItem(_id("filler_vertical_layer"), () -> new VerticalLayerModule(CompatibleItemSettings.of(_id("filler_vertical_layer")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        fillerHORIZONTAL_LAYER = registry2.registerItem(_id("filler_horizontal_layer"), () -> new HorizontalLayerModule(CompatibleItemSettings.of(_id("filler_horizontal_layer")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        fillerTOWER = registry2.registerItem(_id("filler_tower"), () -> new TowerModule(CompatibleItemSettings.of(_id("filler_tower")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        fillerDELETE_FLUID = registry2.registerItem(_id("filler_delete_fluid"), () -> new DeleteFluidModule(CompatibleItemSettings.of(_id("filler_delete_fluid")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        fillerFLOOR_REPLACE = registry2.registerItem(_id("filler_floor_replace"), () -> new FloorReplaceModule(CompatibleItemSettings.of(_id("filler_floor_replace")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        fillerSTAIRS = registry2.registerItem(_id("filler_create_stairs"), () -> new CreateStairsModule(CompatibleItemSettings.of(_id("filler_create_stairs")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        fillerPYRAMID = registry2.registerItem(_id("filler_create_pyramid"), () -> new CreatePyramidModule(CompatibleItemSettings.of(_id("filler_create_pyramid")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        fillerCUT_STAIRS = registry2.registerItem(_id("filler_cut_stairs"), () -> new CutStairsModule(CompatibleItemSettings.of(_id("filler_cut_stairs")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        fillerCUT_PYRAMID = registry2.registerItem(_id("filler_cut_pyramid"), () -> new CutPyramidModule(CompatibleItemSettings.of(_id("filler_cut_pyramid")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));

        REDSTONE_HAMMER = registry2.registerItem(_id("redstone_hammer"), () -> new RedstoneHammer(CompatibleItemSettings.of(_id("redstone_hammer")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        WRENCH = registry2.registerItem(_id("wrench"), () -> new WrenchItem(CompatibleItemSettings.of(_id("wrench")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));

        REFINED_GEAR = registry2.registerItem(_id("refined_gear"), () -> new CompatItem(CompatibleItemSettings.of(_id("refined_gear")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        ENHANCED_REFINED_GEAR = registry2.registerItem(_id("enhanced_refined_gear"), () -> new CompatItem(CompatibleItemSettings.of(_id("enhanced_refined_gear")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        ENHANCED_PANEL = registry2.registerItem(_id("enhanced_panel"), () -> new CompatItem(CompatibleItemSettings.of(_id("enhanced_panel")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));

        EMPTY_BLUEPRINT = registry2.registerItem(_id("empty_blueprint"), () -> new Blueprint(CompatibleItemSettings.of(_id("empty_blueprint")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        BLUEPRINT = registry2.registerItem(_id("blueprint"), () -> new Blueprint(CompatibleItemSettings.of(_id("blueprint")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));

        EMPTY_TEMPLATE = registry2.registerItem(_id("empty_template"), () -> new Template(CompatibleItemSettings.of(_id("empty_template")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));
        TEMPLATE = registry2.registerItem(_id("template"), () -> new Template(CompatibleItemSettings.of(_id("template")).addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP)));

        //registry2.registerItem(EnhancedQuarries.id("reimu_bun"), () -> REIMU_BUN);
        //registry2.registerItem(EnhancedQuarries.id("marisa_bun"), () -> MARISA_BUN);
    }
}
