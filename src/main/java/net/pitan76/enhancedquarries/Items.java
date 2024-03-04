package net.pitan76.enhancedquarries;

import net.minecraft.item.Item;
import net.pitan76.enhancedquarries.item.Blueprint;
import net.pitan76.enhancedquarries.item.RedstoneHammer;
import net.pitan76.enhancedquarries.item.WrenchItem;
import net.pitan76.enhancedquarries.item.base.FillerModule;
import net.pitan76.enhancedquarries.item.fillermodule.*;
import net.pitan76.enhancedquarries.item.quarrymodule.*;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.ExtendItem;
import net.pitan76.mcpitanlib.api.util.ItemUtil;

import static net.pitan76.enhancedquarries.EnhancedQuarries.registry;

public class Items {

    public static Item NORMAL_QUARRY = ItemUtil.ofBlock(Blocks.NORMAL_QUARRY, new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    public static Item ENHANCED_QUARRY = ItemUtil.ofBlock(Blocks.ENHANCED_QUARRY, new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    public static Item FLUID_QUARRY = ItemUtil.ofBlock(Blocks.FLUID_QUARRY, new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    public static Item OPTIMUM_QUARRY = ItemUtil.ofBlock(Blocks.OPTIMUM_QUARRY, new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    public static Item ENHANCED_OPTIMUM_QUARRY = ItemUtil.ofBlock(Blocks.ENHANCED_OPTIMUM_QUARRY, new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    public static Item FLUID_OPTIMUM_QUARRY = ItemUtil.ofBlock(Blocks.FLUID_OPTIMUM_QUARRY, new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));

    public static Item NORMAL_FILLER = ItemUtil.ofBlock(Blocks.NORMAL_FILLER, new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    public static Item ENHANCED_FILLER = ItemUtil.ofBlock(Blocks.ENHANCED_FILLER, new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    public static Item ENHANCED_FILLER_WITH_CHEST = ItemUtil.ofBlock(Blocks.ENHANCED_FILLER_WITH_CHEST, new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));

    public static Item NORMAL_PUMP = ItemUtil.ofBlock(Blocks.NORMAL_PUMP, new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    public static Item ENHANCED_PUMP = ItemUtil.ofBlock(Blocks.ENHANCED_PUMP, new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));

    public static Item NORMAL_SCANNER = ItemUtil.ofBlock(Blocks.NORMAL_SCANNER, new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    public static Item NORMAL_BUILDER = ItemUtil.ofBlock(Blocks.NORMAL_BUILDER, new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    public static Item NORMAL_LIBRARY = ItemUtil.ofBlock(Blocks.NORMAL_LIBRARY, new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));

    public static Item ENERGY_GENERATOR = ItemUtil.ofBlock(Blocks.ENERGY_GENERATOR, new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));

    public static Item NORMAL_MARKER = ItemUtil.ofBlock(Blocks.NORMAL_MARKER, new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    public static Item FRAME = ItemUtil.ofBlock(Blocks.FRAME, new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));


    public static Item BEDROCK_BREAK_MODULE = new BedrockBreakModule(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    public static Item SILK_TOUCH_MODULE = new SilkTouchModule(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    public static Item LUCK_MODULE = new LuckEnchantModule(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    public static Item MOB_KILL_MODULE = new MobKillModule(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    public static Item MOB_DELETE_MODULE = new MobDeleteModule(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    public static Item EXP_COLLECT_MODULE = new ExpCollectModule(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));

    // 全配置
    public static Item fillerALL_FILL = new GenericFillerModule(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    // 全消去
    public static Item fillerALL_DELETE = new GenericFillerModule(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    // 全撤去
    public static Item fillerALL_REMOVE = new GenericFillerModule(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    // 整地
    public static Item fillerLEVELING = new GenericFillerModule(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    // ボックス
    public static Item fillerBOX = new GenericFillerModule(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    // 壁
    public static Item fillerWALL = new GenericFillerModule(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    // 松明
    public static Item fillerTORCH = new GenericFillerModule(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    // 垂直レイヤー
    public static FillerModule fillerVERTICAL_LAYER = new VerticalLayerModule(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    // 平行レイヤー
    public static FillerModule fillerHORIZONTAL_LAYER = new HorizontalLayerModule(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    // 積み上げ
    public static FillerModule fillerTOWER = new TowerModule(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    // 液体除去(未実装)
    public static FillerModule fillerDELETE_FLUID = new DeleteFluidModule(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    // 床張り替え
    public static FillerModule fillerFLOOR_REPLACE = new FloorReplaceModule(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    // 階段
    public static FillerModule fillerSTAIRS = new CreateStairsModule(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    // 四角錐
    public static FillerModule fillerPYRAMID = new CreatePyramidModule(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    // 階段撤去
    public static FillerModule fillerCUT_STAIRS = new CutStairsModule(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    // 四角錐撤去
    public static FillerModule fillerCUT_PYRAMID = new CutPyramidModule(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));

    // 赤石ハンマー
    public static Item REDSTONE_HAMMER = new RedstoneHammer(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));

    // レンチ
    public static Item WRENCH = new WrenchItem(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));

    // 素材
    public static Item REFINED_GEAR = new ExtendItem(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    public static Item ENHANCED_REFINED_GEAR = new ExtendItem(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    public static Item ENHANCED_PANEL = new ExtendItem(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));

    // ----

    // 設計図
    public static Item BLUEPRINT = new Blueprint(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));
    public static Item EMPTY_BLUEPRINT = new Blueprint(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP));

    // ゆっくり饅頭 (未実装)
    //public static Item REIMU_BUN = new ExtendItem(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP).food(FoodComponents.COOKIE));
    //public static Item MARISA_BUN = new ExtendItem(new CompatibleItemSettings().addGroup(EnhancedQuarries.ENHANCED_QUARRIES_GROUP).food(FoodComponents.COOKIE));


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

        registry.registerItem(EnhancedQuarries.id("energy_generator"), () -> ENERGY_GENERATOR);

        registry.registerItem(EnhancedQuarries.id("normal_marker"), () -> NORMAL_MARKER);
        registry.registerItem(EnhancedQuarries.id("frame"), () -> FRAME);


        registry.registerItem(EnhancedQuarries.id("bedrock_break_module"), () -> BEDROCK_BREAK_MODULE);
        registry.registerItem(EnhancedQuarries.id("silk_touch_module"), () -> SILK_TOUCH_MODULE);
        registry.registerItem(EnhancedQuarries.id("luck_enchant_module"), () -> LUCK_MODULE);
        registry.registerItem(EnhancedQuarries.id("mob_kill_module"), () -> MOB_KILL_MODULE);
        registry.registerItem(EnhancedQuarries.id("mob_delete_module"), () -> MOB_DELETE_MODULE);
        registry.registerItem(EnhancedQuarries.id("exp_collect_module"), () -> EXP_COLLECT_MODULE);

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
        registry.registerItem(EnhancedQuarries.id("wrench"), () -> WRENCH);

        registry.registerItem(EnhancedQuarries.id("refined_gear"), () -> REFINED_GEAR);
        registry.registerItem(EnhancedQuarries.id("enhanced_refined_gear"), () -> ENHANCED_REFINED_GEAR);
        registry.registerItem(EnhancedQuarries.id("enhanced_panel"), () -> ENHANCED_PANEL);

        registry.registerItem(EnhancedQuarries.id("empty_blueprint"), () -> EMPTY_BLUEPRINT);
        registry.registerItem(EnhancedQuarries.id("blueprint"), () -> BLUEPRINT);

        //registry.registerItem(EnhancedQuarries.id("reimu_bun"), () -> REIMU_BUN);
        //registry.registerItem(EnhancedQuarries.id("marisa_bun"), () -> MARISA_BUN);
    }
}
