package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.Blocks;
import ml.pkom.enhancedquarries.EnhancedQuarries;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class Tiles {

    public static BlockEntityType<NormalQuarryTile> NORMAL_QUARRY_TILE;
    public static BlockEntityType<EnhancedQuarryTile> ENHANCED_QUARRY_TILE;
    public static BlockEntityType<FluidQuarryTile> FLUID_QUARRY_TILE;
    public static BlockEntityType<NormalFillerTile> NORMAL_FILLER_TILE;

    public static void init() {
        NORMAL_QUARRY_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedQuarries.id("normal_quarry_tile"), create(NormalQuarryTile::new, Blocks.NORMAL_QUARRY));
        ENHANCED_QUARRY_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedQuarries.id("enhanced_quarry_tile"), create(EnhancedQuarryTile::new, Blocks.ENHANCED_QUARRY));
        FLUID_QUARRY_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedQuarries.id("fluid_quarry_tile"), create(FluidQuarryTile::new, Blocks.FLUID_QUARRY));
        NORMAL_FILLER_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedQuarries.id("normal_filler_tile"), create(NormalFillerTile::new, Blocks.NORMAL_FILLER));
    }

    public static <T extends BlockEntity> BlockEntityType create(Supplier supplier, Block... blocks) {
        return BlockEntityType.Builder.create(supplier, blocks).build(null);
    }

}
