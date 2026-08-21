package net.pitan76.enhancedquarries;

import net.minecraft.block.Block;
import net.pitan76.enhancedquarries.block.*;
import net.pitan76.enhancedquarries.block.base.*;
import net.pitan76.mcpitanlib.midohra.block.ITypedBlockWrapper;

import static net.pitan76.enhancedquarries.EnhancedQuarries.*;

public class Blocks {

    public static ITypedBlockWrapper<Quarry> NORMAL_QUARRY;
    public static Quarry ENHANCED_QUARRY;
    public static Quarry FLUID_QUARRY;
    public static Quarry OPTIMUM_QUARRY;
    public static Quarry ENHANCED_OPTIMUM_QUARRY;
    public static Quarry FLUID_OPTIMUM_QUARRY;

    public static Filler NORMAL_FILLER;
    public static Filler ENHANCED_FILLER;
    public static Filler ENHANCED_FILLER_WITH_CHEST;

    public static Pump NORMAL_PUMP;
    public static Pump ENHANCED_PUMP;

    public static Scanner NORMAL_SCANNER;
    public static Builder NORMAL_BUILDER;
    public static Library NORMAL_LIBRARY;

    public static Block ENERGY_GENERATOR;

    public static Block NORMAL_MARKER;
    public static Block FRAME;

    public static void init() {
        NORMAL_QUARRY = registry2.registerBlock(EnhancedQuarries._id("normal_quarry"), () -> new NormalQuarry(_id("normal_quarry")));
        registry.registerBlock(EnhancedQuarries._id("enhanced_quarry"), () -> ENHANCED_QUARRY = new EnhancedQuarry(_id("enhanced_quarry")));
        registry.registerBlock(EnhancedQuarries._id("fluid_quarry"), () -> FLUID_QUARRY = new FluidQuarry(_id("fluid_quarry")));
        registry.registerBlock(EnhancedQuarries._id("optimum_quarry"), () -> OPTIMUM_QUARRY = new OptimumQuarry(_id("optimum_quarry")));
        registry.registerBlock(EnhancedQuarries._id("enhanced_optimum_quarry"), () -> ENHANCED_OPTIMUM_QUARRY = new EnhancedOptimumQuarry(_id("enhanced_optimum_quarry")));
        registry.registerBlock(EnhancedQuarries._id("fluid_optimum_quarry"), () -> FLUID_OPTIMUM_QUARRY = new FluidOptimumQuarry(_id("fluid_optimum_quarry")));

        registry.registerBlock(EnhancedQuarries._id("normal_filler"), () -> NORMAL_FILLER = new NormalFiller(_id("normal_filler")));
        registry.registerBlock(EnhancedQuarries._id("enhanced_filler"), () -> ENHANCED_FILLER = new EnhancedFiller(_id("enhanced_filler")));
        registry.registerBlock(EnhancedQuarries._id("enhanced_filler_with_chest"), () -> ENHANCED_FILLER_WITH_CHEST = new EnhancedFillerWithChest(_id("enhanced_filler_with_chest")));

        registry.registerBlock(EnhancedQuarries._id("normal_pump"), () -> NORMAL_PUMP = new NormalPump(_id("normal_pump")));
        registry.registerBlock(EnhancedQuarries._id("enhanced_pump"), () -> ENHANCED_PUMP = new EnhancedPump(_id("enhanced_pump")));

        registry.registerBlock(EnhancedQuarries._id("normal_scanner"), () -> NORMAL_SCANNER = new NormalScanner(_id("normal_scanner")));
        registry.registerBlock(EnhancedQuarries._id("normal_builder"), () -> NORMAL_BUILDER = new NormalBuilder(_id("normal_builder")));
        registry.registerBlock(EnhancedQuarries._id("normal_library"), () -> NORMAL_LIBRARY = new NormalLibrary(_id("normal_library")));

        registry.registerBlock(EnhancedQuarries._id("energy_generator"), () -> ENERGY_GENERATOR = new EnergyGenerator(_id("energy_generator")));

        registry.registerBlock(EnhancedQuarries._id("normal_marker"), () -> NORMAL_MARKER = new NormalMarker(_id("normal_marker")));
        registry.registerBlock(EnhancedQuarries._id("frame"), () -> FRAME = new Frame(_id("frame")));
    }
}
