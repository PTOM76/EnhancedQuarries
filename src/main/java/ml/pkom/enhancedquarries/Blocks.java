package ml.pkom.enhancedquarries;

import ml.pkom.enhancedquarries.block.*;
import ml.pkom.enhancedquarries.block.base.*;
import net.minecraft.block.Block;
import net.minecraft.util.registry.Registry;

public class Blocks {

    public static Quarry NORMAL_QUARRY = NormalQuarry.getQuarry();
    public static Quarry ENHANCED_QUARRY = EnhancedQuarry.getQuarry();
    public static Quarry FLUID_QUARRY = FluidQuarry.getQuarry();
    public static Quarry OPTIMUM_QUARRY = OptimumQuarry.getQuarry();
    public static Quarry ENHANCED_OPTIMUM_QUARRY = EnhancedOptimumQuarry.getQuarry();
    public static Quarry FLUID_OPTIMUM_QUARRY = FluidOptimumQuarry.getQuarry();

    public static Filler NORMAL_FILLER = NormalFiller.getFiller();
    public static Filler ENHANCED_FILLER = EnhancedFiller.getFiller();
    public static Filler ENHANCED_FILLER_WITH_CHEST = EnhancedFillerWithChest.getFiller();

    public static Pump NORMAL_PUMP = NormalPump.getPump();
    public static Pump ENHANCED_PUMP = EnhancedPump.getPump();

    public static Scanner NORMAL_SCANNER = NormalScanner.getScanner();
    public static Builder NORMAL_BUILDER = NormalBuilder.getBuilder();
    public static Library NORMAL_LIBRARY = NormalLibrary.getLibrary();

    public static Block NORMAL_MARKER = NormalMarker.getBlock();
    public static Block FRAME = Frame.getBlock();

    public static void init() {
        Registry.register(Registry.BLOCK, EnhancedQuarries.id("normal_quarry"), NORMAL_QUARRY);
        Registry.register(Registry.BLOCK, EnhancedQuarries.id("enhanced_quarry"), ENHANCED_QUARRY);
        Registry.register(Registry.BLOCK, EnhancedQuarries.id("fluid_quarry"), FLUID_QUARRY);
        Registry.register(Registry.BLOCK, EnhancedQuarries.id("optimum_quarry"), OPTIMUM_QUARRY);
        Registry.register(Registry.BLOCK, EnhancedQuarries.id("enhanced_optimum_quarry"), ENHANCED_OPTIMUM_QUARRY);
        Registry.register(Registry.BLOCK, EnhancedQuarries.id("fluid_optimum_quarry"), FLUID_OPTIMUM_QUARRY);

        Registry.register(Registry.BLOCK, EnhancedQuarries.id("normal_filler"), NORMAL_FILLER);
        Registry.register(Registry.BLOCK, EnhancedQuarries.id("enhanced_filler"), ENHANCED_FILLER);
        Registry.register(Registry.BLOCK, EnhancedQuarries.id("enhanced_filler_with_chest"), ENHANCED_FILLER_WITH_CHEST);

        Registry.register(Registry.BLOCK, EnhancedQuarries.id("normal_pump"), NORMAL_PUMP);
        Registry.register(Registry.BLOCK, EnhancedQuarries.id("enhanced_pump"), ENHANCED_PUMP);

        Registry.register(Registry.BLOCK, EnhancedQuarries.id("normal_scanner"), NORMAL_SCANNER);
        Registry.register(Registry.BLOCK, EnhancedQuarries.id("normal_builder"), NORMAL_BUILDER);
        Registry.register(Registry.BLOCK, EnhancedQuarries.id("normal_library"), NORMAL_LIBRARY);

        Registry.register(Registry.BLOCK, EnhancedQuarries.id("normal_marker"), NORMAL_MARKER);
        Registry.register(Registry.BLOCK, EnhancedQuarries.id("frame"), FRAME);
    }
}
