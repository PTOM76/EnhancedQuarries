package ml.pkom.enhancedquarries;

import ml.pkom.enhancedquarries.item.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class Items {

    public static Item NORMAL_FILLER = new BlockItem(Blocks.NORMAL_FILLER, new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    public static Item NORMAL_QUARRY = new BlockItem(Blocks.NORMAL_QUARRY, new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    public static Item ENHANCED_QUARRY = new BlockItem(Blocks.ENHANCED_QUARRY, new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    public static Item FLUID_QUARRY = new BlockItem(Blocks.FLUID_QUARRY, new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));

    public static Item NORMAL_MARKER = new BlockItem(Blocks.NORMAL_MARKER, new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    public static Item FRAME = new BlockItem(Blocks.FRAME, new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    public static Item BEDROCK_BREAK_MODULE = new BedrockBreakModule(new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    public static Item SILK_TOUCH_MODULE = new SilkTouchModule(new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    public static Item LUCK_MODULE = new LuckEnchantModule(new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    public static Item MOB_KILL_MODULE = new MobKillModule(new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));
    public static Item MOB_DELETE_MODULE = new MobDeleteModule(new FabricItemSettings().group(EnhancedQuarries.FILLER_PLUS_GROUP));

    public static void init() {
        Registry.register(Registry.ITEM, EnhancedQuarries.id("normal_filler"), NORMAL_FILLER);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("normal_quarry"), NORMAL_QUARRY);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("enhanced_quarry"), ENHANCED_QUARRY);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("fluid_quarry"), FLUID_QUARRY);

        Registry.register(Registry.ITEM, EnhancedQuarries.id("normal_marker"), NORMAL_MARKER);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("frame"), FRAME);

        Registry.register(Registry.ITEM, EnhancedQuarries.id("bedrock_break_module"), BEDROCK_BREAK_MODULE);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("silk_touch_module"), SILK_TOUCH_MODULE);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("luck_enchant_module"), LUCK_MODULE);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("mob_kill_module"), MOB_KILL_MODULE);
        Registry.register(Registry.ITEM, EnhancedQuarries.id("mob_delete_module"), MOB_DELETE_MODULE);
    }
}
