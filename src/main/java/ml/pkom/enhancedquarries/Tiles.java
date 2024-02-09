package ml.pkom.enhancedquarries;

import ml.pkom.enhancedquarries.tile.*;
import ml.pkom.enhancedquarries.tile.base.EnergyGeneratorTile;
import ml.pkom.mcpitanlibarch.api.event.registry.RegistryEvent;
import ml.pkom.mcpitanlibarch.api.tile.BlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

import static ml.pkom.enhancedquarries.EnhancedQuarries.registry;

public class Tiles {

    public static RegistryEvent<BlockEntityType<?>> NORMAL_QUARRY_TILE;
    public static RegistryEvent<BlockEntityType<?>> ENHANCED_QUARRY_TILE;
    public static RegistryEvent<BlockEntityType<?>> FLUID_QUARRY_TILE;
    public static RegistryEvent<BlockEntityType<?>> OPTIMUM_QUARRY_TILE;
    public static RegistryEvent<BlockEntityType<?>> ENHANCED_OPTIMUM_QUARRY_TILE;
    public static RegistryEvent<BlockEntityType<?>> FLUID_OPTIMUM_QUARRY_TILE;

    public static RegistryEvent<BlockEntityType<?>> NORMAL_FILLER_TILE;
    public static RegistryEvent<BlockEntityType<?>> ENHANCED_FILLER_TILE;
    public static RegistryEvent<BlockEntityType<?>> ENHANCED_FILLER_WITH_CHEST_TILE;

    public static RegistryEvent<BlockEntityType<?>> NORMAL_PUMP_TILE;
    public static RegistryEvent<BlockEntityType<?>> ENHANCED_PUMP_TILE;

    public static RegistryEvent<BlockEntityType<?>> NORMAL_SCANNER_TILE;
    public static RegistryEvent<BlockEntityType<?>> NORMAL_BUILDER_TILE;
    public static RegistryEvent<BlockEntityType<?>> NORMAL_LIBRARY_TILE;

    public static RegistryEvent<BlockEntityType<?>> NORMAL_MARKER;

    public static RegistryEvent<BlockEntityType<?>> ENERGY_GENERATOR_TILE;

    public static void init() {
        NORMAL_QUARRY_TILE = registry.registerBlockEntityType(EnhancedQuarries.id("normal_quarry_tile"), () -> create(NormalQuarryTile::new, Blocks.NORMAL_QUARRY));
        ENHANCED_QUARRY_TILE = registry.registerBlockEntityType(EnhancedQuarries.id("enhanced_quarry_tile"), () -> create(EnhancedQuarryTile::new, Blocks.ENHANCED_QUARRY));
        FLUID_QUARRY_TILE = registry.registerBlockEntityType(EnhancedQuarries.id("fluid_quarry_tile"), () -> create(FluidQuarryTile::new, Blocks.FLUID_QUARRY));

        OPTIMUM_QUARRY_TILE = registry.registerBlockEntityType(EnhancedQuarries.id("optimum_quarry_tile"), () -> create(OptimumQuarryTile::new, Blocks.OPTIMUM_QUARRY));
        ENHANCED_OPTIMUM_QUARRY_TILE = registry.registerBlockEntityType(EnhancedQuarries.id("enhanced_optimum_quarry_tile"), () -> create(EnhancedOptimumQuarryTile::new, Blocks.ENHANCED_OPTIMUM_QUARRY));
        FLUID_OPTIMUM_QUARRY_TILE = registry.registerBlockEntityType(EnhancedQuarries.id("fluid_optimum_quarry_tile"), () -> create(FluidOptimumQuarryTile::new, Blocks.FLUID_OPTIMUM_QUARRY));

        NORMAL_FILLER_TILE = registry.registerBlockEntityType(EnhancedQuarries.id("normal_filler_tile"), () -> create(NormalFillerTile::new, Blocks.NORMAL_FILLER));
        ENHANCED_FILLER_TILE = registry.registerBlockEntityType(EnhancedQuarries.id("enhanced_filler_tile"), () -> create(EnhancedFillerTile::new, Blocks.ENHANCED_FILLER));
        ENHANCED_FILLER_WITH_CHEST_TILE = registry.registerBlockEntityType(EnhancedQuarries.id("enhanced_filler_with_chest_tile"), () -> create(EnhancedFillerWithChestTile::new, Blocks.ENHANCED_FILLER_WITH_CHEST));

        NORMAL_PUMP_TILE = registry.registerBlockEntityType(EnhancedQuarries.id("normal_pump"), () -> create(NormalPumpTile::new, Blocks.NORMAL_PUMP));
        ENHANCED_PUMP_TILE = registry.registerBlockEntityType(EnhancedQuarries.id("enhanced_pump"), () -> create(EnhancedPumpTile::new, Blocks.ENHANCED_PUMP));

        NORMAL_SCANNER_TILE = registry.registerBlockEntityType(EnhancedQuarries.id("normal_scanner"), () -> create(NormalScannerTile::new, Blocks.NORMAL_SCANNER));
        NORMAL_BUILDER_TILE = registry.registerBlockEntityType(EnhancedQuarries.id("normal_builder"), () -> create(NormalBuilderTile::new, Blocks.NORMAL_BUILDER));
        NORMAL_LIBRARY_TILE = registry.registerBlockEntityType(EnhancedQuarries.id("normal_library"), () -> create(NormalLibraryTile::new, Blocks.NORMAL_LIBRARY));

        NORMAL_MARKER = registry.registerBlockEntityType(EnhancedQuarries.id("normal_marker"), () -> create(MarkerTile::new, Blocks.NORMAL_MARKER));

        ENERGY_GENERATOR_TILE = registry.registerBlockEntityType(EnhancedQuarries.id("energy_generator_tile"), () -> create(EnergyGeneratorTile::new, Blocks.ENERGY_GENERATOR));
    }

    public static <T extends BlockEntity> BlockEntityType<T> create(BlockEntityTypeBuilder.Factory<T> supplier, Block... blocks) {
        return BlockEntityTypeBuilder.create(supplier, blocks).build();
    }

}
