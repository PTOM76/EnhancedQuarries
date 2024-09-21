package net.pitan76.enhancedquarries;

import net.minecraft.block.Block;
import net.pitan76.enhancedquarries.block.*;
import net.pitan76.enhancedquarries.block.base.*;

import static net.pitan76.enhancedquarries.EnhancedQuarries.registry;

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

    public static Block ENERGY_GENERATOR = EnergyGenerator.getEnergyGenerator();

    public static Block NORMAL_MARKER = NormalMarker.getBlock();
    public static Block FRAME = Frame.getBlock();

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
