package net.pitan76.enhancedquarries;

import net.minecraft.block.Block;
import net.pitan76.enhancedquarries.block.*;
import net.pitan76.enhancedquarries.block.base.*;

import static net.pitan76.enhancedquarries.EnhancedQuarries._id;
import static net.pitan76.enhancedquarries.EnhancedQuarries.registry;

public class Blocks {

    public static Quarry NORMAL_QUARRY = new NormalQuarry(_id("normal_quarry"));
    public static Quarry ENHANCED_QUARRY = new EnhancedQuarry(_id("enhanced_quarry"));
    public static Quarry FLUID_QUARRY = new FluidQuarry(_id("fluid_quarry"));
    public static Quarry OPTIMUM_QUARRY = new OptimumQuarry(_id("optimum_quarry"));
    public static Quarry ENHANCED_OPTIMUM_QUARRY = new EnhancedOptimumQuarry(_id("enhanced_optimum_quarry"));
    public static Quarry FLUID_OPTIMUM_QUARRY = new FluidOptimumQuarry(_id("fluid_optimum_quarry"));

    public static Filler NORMAL_FILLER = new NormalFiller(_id("normal_filler"));
    public static Filler ENHANCED_FILLER = new EnhancedFiller(_id("enhanced_filler"));
    public static Filler ENHANCED_FILLER_WITH_CHEST = new EnhancedFillerWithChest(_id("enhanced_filler_with_chest"));

    public static Pump NORMAL_PUMP = new NormalPump(_id("normal_pump"));
    public static Pump ENHANCED_PUMP = new EnhancedPump(_id("enhanced_pump"));

    public static Scanner NORMAL_SCANNER = new NormalScanner(_id("normal_scanner"));
    public static Builder NORMAL_BUILDER = new NormalBuilder(_id("normal_builder"));
    public static Library NORMAL_LIBRARY = new NormalLibrary(_id("normal_library"));

    public static Block ENERGY_GENERATOR = new EnergyGenerator(_id("energy_generator"));

    public static Block NORMAL_MARKER = new NormalMarker(_id("normal_marker"));
    public static Block FRAME = new Frame(_id("frame"));

    public static void init() {
        registry.registerBlock(EnhancedQuarries._id("normal_quarry"), () -> NORMAL_QUARRY);
        registry.registerBlock(EnhancedQuarries._id("enhanced_quarry"), () -> ENHANCED_QUARRY);
        registry.registerBlock(EnhancedQuarries._id("fluid_quarry"), () -> FLUID_QUARRY);
        registry.registerBlock(EnhancedQuarries._id("optimum_quarry"), () -> OPTIMUM_QUARRY);
        registry.registerBlock(EnhancedQuarries._id("enhanced_optimum_quarry"), () -> ENHANCED_OPTIMUM_QUARRY);
        registry.registerBlock(EnhancedQuarries._id("fluid_optimum_quarry"), () -> FLUID_OPTIMUM_QUARRY);

        registry.registerBlock(EnhancedQuarries._id("normal_filler"), () -> NORMAL_FILLER);
        registry.registerBlock(EnhancedQuarries._id("enhanced_filler"), () -> ENHANCED_FILLER);
        registry.registerBlock(EnhancedQuarries._id("enhanced_filler_with_chest"), () -> ENHANCED_FILLER_WITH_CHEST);

        registry.registerBlock(EnhancedQuarries._id("normal_pump"), () -> NORMAL_PUMP);
        registry.registerBlock(EnhancedQuarries._id("enhanced_pump"), () -> ENHANCED_PUMP);

        registry.registerBlock(EnhancedQuarries._id("normal_scanner"), () -> NORMAL_SCANNER);
        registry.registerBlock(EnhancedQuarries._id("normal_builder"), () -> NORMAL_BUILDER);
        registry.registerBlock(EnhancedQuarries._id("normal_library"), () -> NORMAL_LIBRARY);

        registry.registerBlock(EnhancedQuarries._id("energy_generator"), () -> ENERGY_GENERATOR);

        registry.registerBlock(EnhancedQuarries._id("normal_marker"), () -> NORMAL_MARKER);
        registry.registerBlock(EnhancedQuarries._id("frame"), () -> FRAME);
    }
}
