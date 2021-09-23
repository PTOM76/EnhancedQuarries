package ml.pkom.enhancedquarries;

import ml.pkom.enhancedquarries.tile.*;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class Tiles {

    public static BlockEntityType<NormalQuarryTile> NORMAL_QUARRY_TILE;
    public static BlockEntityType<EnhancedQuarryTile> ENHANCED_QUARRY_TILE;
    public static BlockEntityType<FluidQuarryTile> FLUID_QUARRY_TILE;
    public static BlockEntityType<OptimumQuarryTile> OPTIMUM_QUARRY_TILE;

    public static BlockEntityType<NormalFillerTile> NORMAL_FILLER_TILE;
    public static BlockEntityType<EnhancedFillerTile> ENHANCED_FILLER_TILE;
    public static BlockEntityType<EnhancedFillerWithChestTile> ENHANCED_FILLER_WITH_CHEST_TILE;

    public static BlockEntityType<NormalPumpTile> NORMAL_PUMP;
    public static BlockEntityType<EnhancedPumpTile> ENHANCED_PUMP;

    public static void init() {
        NORMAL_QUARRY_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedQuarries.id("normal_quarry_tile"), create(NormalQuarryTile::new, Blocks.NORMAL_QUARRY));
        ENHANCED_QUARRY_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedQuarries.id("enhanced_quarry_tile"), create(EnhancedQuarryTile::new, Blocks.ENHANCED_QUARRY));
        FLUID_QUARRY_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedQuarries.id("fluid_quarry_tile"), create(FluidQuarryTile::new, Blocks.FLUID_QUARRY));
        OPTIMUM_QUARRY_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedQuarries.id("optimum_quarry_tile"), create(OptimumQuarryTile::new, Blocks.OPTIMUM_QUARRY));
        NORMAL_FILLER_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedQuarries.id("normal_filler_tile"), create(NormalFillerTile::new, Blocks.NORMAL_FILLER));
        ENHANCED_FILLER_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedQuarries.id("enhanced_filler_tile"), create(EnhancedFillerTile::new, Blocks.ENHANCED_FILLER));
        ENHANCED_FILLER_WITH_CHEST_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedQuarries.id("enhanced_filler_with_chest_tile"), create(EnhancedFillerWithChestTile::new, Blocks.ENHANCED_FILLER_WITH_CHEST));
        NORMAL_PUMP = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedQuarries.id("normal_pump"), create(NormalPumpTile::new, Blocks.NORMAL_PUMP));
        ENHANCED_PUMP = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedQuarries.id("enhanced_pump"), create(EnhancedPumpTile::new, Blocks.ENHANCED_PUMP));
    }

    public static <T extends BlockEntity> BlockEntityType create(Supplier supplier, Block... blocks) {
        return BlockEntityType.Builder.create(supplier, blocks).build(null);
    }

}
