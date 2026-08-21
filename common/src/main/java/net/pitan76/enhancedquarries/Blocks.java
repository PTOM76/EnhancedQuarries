package net.pitan76.enhancedquarries;

import net.pitan76.enhancedquarries.block.*;
import net.pitan76.enhancedquarries.block.base.*;
import net.pitan76.mcpitanlib.midohra.block.ITypedBlockWrapper;

import static net.pitan76.enhancedquarries.EnhancedQuarries.*;

public class Blocks {

    public static ITypedBlockWrapper<Quarry> NORMAL_QUARRY;
    public static ITypedBlockWrapper<Quarry> ENHANCED_QUARRY;
    public static ITypedBlockWrapper<Quarry> FLUID_QUARRY;
    public static ITypedBlockWrapper<Quarry> OPTIMUM_QUARRY;
    public static ITypedBlockWrapper<Quarry> ENHANCED_OPTIMUM_QUARRY;
    public static ITypedBlockWrapper<Quarry> FLUID_OPTIMUM_QUARRY;

    public static ITypedBlockWrapper<Filler> NORMAL_FILLER;
    public static ITypedBlockWrapper<Filler> ENHANCED_FILLER;
    public static ITypedBlockWrapper<Filler> ENHANCED_FILLER_WITH_CHEST;

    public static ITypedBlockWrapper<Pump> NORMAL_PUMP;
    public static ITypedBlockWrapper<Pump> ENHANCED_PUMP;

    public static ITypedBlockWrapper<Scanner> NORMAL_SCANNER;
    public static ITypedBlockWrapper<Builder> NORMAL_BUILDER;
    public static ITypedBlockWrapper<Library> NORMAL_LIBRARY;

    public static ITypedBlockWrapper<EnergyGenerator> ENERGY_GENERATOR;

    public static ITypedBlockWrapper<NormalMarker> NORMAL_MARKER;
    public static ITypedBlockWrapper<Frame> FRAME;

    public static void init() {
        NORMAL_QUARRY = registry2.registerBlock(_id("normal_quarry"), () -> new NormalQuarry(_id("normal_quarry")));
        ENHANCED_QUARRY = registry2.registerBlock(_id("enhanced_quarry"), () -> new EnhancedQuarry(_id("enhanced_quarry")));
        FLUID_QUARRY = registry2.registerBlock(_id("fluid_quarry"), () -> new FluidQuarry(_id("fluid_quarry")));
        OPTIMUM_QUARRY = registry2.registerBlock(_id("optimum_quarry"), () -> new OptimumQuarry(_id("optimum_quarry")));
        ENHANCED_OPTIMUM_QUARRY = registry2.registerBlock(_id("enhanced_optimum_quarry"), () -> new EnhancedOptimumQuarry(_id("enhanced_optimum_quarry")));
        FLUID_OPTIMUM_QUARRY = registry2.registerBlock(_id("fluid_optimum_quarry"), () -> new FluidOptimumQuarry(_id("fluid_optimum_quarry")));

        NORMAL_FILLER = registry2.registerBlock(_id("normal_filler"), () -> new NormalFiller(_id("normal_filler")));
        ENHANCED_FILLER = registry2.registerBlock(_id("enhanced_filler"), () -> new EnhancedFiller(_id("enhanced_filler")));
        ENHANCED_FILLER_WITH_CHEST = registry2.registerBlock(_id("enhanced_filler_with_chest"), () -> new EnhancedFillerWithChest(_id("enhanced_filler_with_chest")));

        NORMAL_PUMP = registry2.registerBlock(_id("normal_pump"), () -> new NormalPump(_id("normal_pump")));
        ENHANCED_PUMP = registry2.registerBlock(_id("enhanced_pump"), () -> new EnhancedPump(_id("enhanced_pump")));

        NORMAL_SCANNER = registry2.registerBlock(_id("normal_scanner"), () -> new NormalScanner(_id("normal_scanner")));
        NORMAL_BUILDER = registry2.registerBlock(_id("normal_builder"), () -> new NormalBuilder(_id("normal_builder")));
        NORMAL_LIBRARY = registry2.registerBlock(_id("normal_library"), () -> new NormalLibrary(_id("normal_library")));

        ENERGY_GENERATOR = registry2.registerBlock(_id("energy_generator"), () -> new EnergyGenerator(_id("energy_generator")));

        NORMAL_MARKER = registry2.registerBlock(_id("normal_marker"), () -> new NormalMarker(_id("normal_marker")));
        FRAME = registry2.registerBlock(_id("frame"), () -> new Frame(_id("frame")));
    }
}
