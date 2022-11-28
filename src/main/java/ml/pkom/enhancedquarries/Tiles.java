package ml.pkom.enhancedquarries;

import ml.pkom.enhancedquarries.tile.*;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public class Tiles {

    public static BlockEntityType<NormalQuarryTile> NORMAL_QUARRY_TILE;
    public static BlockEntityType<EnhancedQuarryTile> ENHANCED_QUARRY_TILE;
    public static BlockEntityType<FluidQuarryTile> FLUID_QUARRY_TILE;
    public static BlockEntityType<OptimumQuarryTile> OPTIMUM_QUARRY_TILE;
    public static BlockEntityType<EnhancedOptimumQuarryTile> ENHANCED_OPTIMUM_QUARRY_TILE;
    public static BlockEntityType<FluidOptimumQuarryTile> FLUID_OPTIMUM_QUARRY_TILE;

    public static BlockEntityType<NormalFillerTile> NORMAL_FILLER_TILE;
    public static BlockEntityType<EnhancedFillerTile> ENHANCED_FILLER_TILE;
    public static BlockEntityType<EnhancedFillerWithChestTile> ENHANCED_FILLER_WITH_CHEST_TILE;

    public static BlockEntityType<NormalPumpTile> NORMAL_PUMP_TILE;
    public static BlockEntityType<EnhancedPumpTile> ENHANCED_PUMP_TILE;

    public static BlockEntityType<NormalScannerTile> NORMAL_SCANNER_TILE;
    public static BlockEntityType<NormalBuilderTile> NORMAL_BUILDER_TILE;
    public static BlockEntityType<NormalLibraryTile> NORMAL_LIBRARY_TILE;

    public static BlockEntityType<MarkerTile> NORMAL_MARKER;

    public static void init() {
        NORMAL_QUARRY_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedQuarries.id("normal_quarry_tile"), create(NormalQuarryTile::new, Blocks.NORMAL_QUARRY));
        ENHANCED_QUARRY_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedQuarries.id("enhanced_quarry_tile"), create(EnhancedQuarryTile::new, Blocks.ENHANCED_QUARRY));
        FLUID_QUARRY_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedQuarries.id("fluid_quarry_tile"), create(FluidQuarryTile::new, Blocks.FLUID_QUARRY));

        OPTIMUM_QUARRY_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedQuarries.id("optimum_quarry_tile"), create(OptimumQuarryTile::new, Blocks.OPTIMUM_QUARRY));
        ENHANCED_OPTIMUM_QUARRY_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedQuarries.id("enhanced_optimum_quarry_tile"), create(EnhancedOptimumQuarryTile::new, Blocks.ENHANCED_OPTIMUM_QUARRY));
        FLUID_OPTIMUM_QUARRY_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedQuarries.id("fluid_optimum_quarry_tile"), create(FluidOptimumQuarryTile::new, Blocks.FLUID_OPTIMUM_QUARRY));

        NORMAL_FILLER_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedQuarries.id("normal_filler_tile"), create(NormalFillerTile::new, Blocks.NORMAL_FILLER));
        ENHANCED_FILLER_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedQuarries.id("enhanced_filler_tile"), create(EnhancedFillerTile::new, Blocks.ENHANCED_FILLER));
        ENHANCED_FILLER_WITH_CHEST_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedQuarries.id("enhanced_filler_with_chest_tile"), create(EnhancedFillerWithChestTile::new, Blocks.ENHANCED_FILLER_WITH_CHEST));

        NORMAL_PUMP_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedQuarries.id("normal_pump"), create(NormalPumpTile::new, Blocks.NORMAL_PUMP));
        ENHANCED_PUMP_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedQuarries.id("enhanced_pump"), create(EnhancedPumpTile::new, Blocks.ENHANCED_PUMP));

        NORMAL_SCANNER_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedQuarries.id("normal_scanner"), create(NormalScannerTile::new, Blocks.NORMAL_SCANNER));
        NORMAL_BUILDER_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedQuarries.id("normal_builder"), create(NormalBuilderTile::new, Blocks.NORMAL_BUILDER));
        NORMAL_LIBRARY_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedQuarries.id("normal_library"), create(NormalLibraryTile::new, Blocks.NORMAL_LIBRARY));

        NORMAL_MARKER = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedQuarries.id("normal_marker"), create(MarkerTile::new, Blocks.NORMAL_MARKER));
    }

    public static <T extends BlockEntity> BlockEntityType<T> create(FabricBlockEntityTypeBuilder.Factory<T> supplier, Block... blocks) {
        return FabricBlockEntityTypeBuilder.create(supplier, blocks).build(null);
    }

}
