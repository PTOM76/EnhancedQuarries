package net.pitan76.enhancedquarries;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.pitan76.enhancedquarries.tile.*;
import net.pitan76.enhancedquarries.tile.base.EnergyGeneratorTile;
import net.pitan76.mcpitanlib.api.registry.result.RegistryResult;
import net.pitan76.mcpitanlib.api.tile.BlockEntityTypeBuilder;

import static net.pitan76.enhancedquarries.EnhancedQuarries.registry;

public class Tiles {

    public static RegistryResult<BlockEntityType<?>> NORMAL_QUARRY_TILE;
    public static RegistryResult<BlockEntityType<?>> ENHANCED_QUARRY_TILE;
    public static RegistryResult<BlockEntityType<?>> FLUID_QUARRY_TILE;
    public static RegistryResult<BlockEntityType<?>> OPTIMUM_QUARRY_TILE;
    public static RegistryResult<BlockEntityType<?>> ENHANCED_OPTIMUM_QUARRY_TILE;
    public static RegistryResult<BlockEntityType<?>> FLUID_OPTIMUM_QUARRY_TILE;

    public static RegistryResult<BlockEntityType<?>> NORMAL_FILLER_TILE;
    public static RegistryResult<BlockEntityType<?>> ENHANCED_FILLER_TILE;
    public static RegistryResult<BlockEntityType<?>> ENHANCED_FILLER_WITH_CHEST_TILE;

    public static RegistryResult<BlockEntityType<?>> NORMAL_PUMP_TILE;
    public static RegistryResult<BlockEntityType<?>> ENHANCED_PUMP_TILE;

    public static RegistryResult<BlockEntityType<?>> NORMAL_SCANNER_TILE;
    public static RegistryResult<BlockEntityType<?>> NORMAL_BUILDER_TILE;
    public static RegistryResult<BlockEntityType<?>> NORMAL_LIBRARY_TILE;

    public static RegistryResult<BlockEntityType<?>> NORMAL_MARKER;

    public static RegistryResult<BlockEntityType<?>> ENERGY_GENERATOR_TILE;

    public static void init() {
        NORMAL_QUARRY_TILE = registry.registerBlockEntityType(EnhancedQuarries._id("normal_quarry_tile"), () -> create(NormalQuarryTile::new, Blocks.NORMAL_QUARRY));
        ENHANCED_QUARRY_TILE = registry.registerBlockEntityType(EnhancedQuarries._id("enhanced_quarry_tile"), () -> create(EnhancedQuarryTile::new, Blocks.ENHANCED_QUARRY));
        FLUID_QUARRY_TILE = registry.registerBlockEntityType(EnhancedQuarries._id("fluid_quarry_tile"), () -> create(FluidQuarryTile::new, Blocks.FLUID_QUARRY));

        OPTIMUM_QUARRY_TILE = registry.registerBlockEntityType(EnhancedQuarries._id("optimum_quarry_tile"), () -> create(OptimumQuarryTile::new, Blocks.OPTIMUM_QUARRY));
        ENHANCED_OPTIMUM_QUARRY_TILE = registry.registerBlockEntityType(EnhancedQuarries._id("enhanced_optimum_quarry_tile"), () -> create(EnhancedOptimumQuarryTile::new, Blocks.ENHANCED_OPTIMUM_QUARRY));
        FLUID_OPTIMUM_QUARRY_TILE = registry.registerBlockEntityType(EnhancedQuarries._id("fluid_optimum_quarry_tile"), () -> create(FluidOptimumQuarryTile::new, Blocks.FLUID_OPTIMUM_QUARRY));

        NORMAL_FILLER_TILE = registry.registerBlockEntityType(EnhancedQuarries._id("normal_filler_tile"), () -> create(NormalFillerTile::new, Blocks.NORMAL_FILLER));
        ENHANCED_FILLER_TILE = registry.registerBlockEntityType(EnhancedQuarries._id("enhanced_filler_tile"), () -> create(EnhancedFillerTile::new, Blocks.ENHANCED_FILLER));
        ENHANCED_FILLER_WITH_CHEST_TILE = registry.registerBlockEntityType(EnhancedQuarries._id("enhanced_filler_with_chest_tile"), () -> create(EnhancedFillerWithChestTile::new, Blocks.ENHANCED_FILLER_WITH_CHEST));

        NORMAL_PUMP_TILE = registry.registerBlockEntityType(EnhancedQuarries._id("normal_pump"), () -> create(NormalPumpTile::new, Blocks.NORMAL_PUMP));
        ENHANCED_PUMP_TILE = registry.registerBlockEntityType(EnhancedQuarries._id("enhanced_pump"), () -> create(EnhancedPumpTile::new, Blocks.ENHANCED_PUMP));

        NORMAL_SCANNER_TILE = registry.registerBlockEntityType(EnhancedQuarries._id("normal_scanner"), () -> create(NormalScannerTile::new, Blocks.NORMAL_SCANNER));
        NORMAL_BUILDER_TILE = registry.registerBlockEntityType(EnhancedQuarries._id("normal_builder"), () -> create(NormalBuilderTile::new, Blocks.NORMAL_BUILDER));
        NORMAL_LIBRARY_TILE = registry.registerBlockEntityType(EnhancedQuarries._id("normal_library"), () -> create(NormalLibraryTile::new, Blocks.NORMAL_LIBRARY));

        NORMAL_MARKER = registry.registerBlockEntityType(EnhancedQuarries._id("normal_marker"), () -> create(MarkerTile::new, Blocks.NORMAL_MARKER));

        ENERGY_GENERATOR_TILE = registry.registerBlockEntityType(EnhancedQuarries._id("energy_generator_tile"), () -> create(EnergyGeneratorTile::new, Blocks.ENERGY_GENERATOR));
    }

    public static <T extends BlockEntity> BlockEntityType<T> create(BlockEntityTypeBuilder.Factory<T> supplier, Block... blocks) {
        return BlockEntityTypeBuilder.create(supplier, blocks).build();
    }

}
