package net.pitan76.enhancedquarries.client.renderer;

import net.minecraft.block.entity.BlockEntityType;
import net.pitan76.enhancedquarries.Config;
import net.pitan76.enhancedquarries.Tiles;
import net.pitan76.enhancedquarries.tile.MarkerTile;
import net.pitan76.enhancedquarries.tile.base.BuilderTile;
import net.pitan76.enhancedquarries.tile.base.FillerTile;
import net.pitan76.enhancedquarries.tile.base.ScannerTile;
import net.pitan76.mcpitanlib.api.client.registry.CompatRegistryClient;

public class TileRenderers {

    public static void init() {
        Config.init();

        if (Config.client_marker_rendering_range_box)
            CompatRegistryClient.registerCompatBlockEntityRenderer(() -> (BlockEntityType<MarkerTile>) Tiles.NORMAL_MARKER.getOrNull(), MarkerRenderer::new);

        if (Config.client_builder_rendering_range_box)
            CompatRegistryClient.registerCompatBlockEntityRenderer(() -> (BlockEntityType<BuilderTile>) Tiles.NORMAL_BUILDER_TILE.getOrNull(), BuilderRenderer::new);

        if (Config.client_scanner_rendering_range_box)
            CompatRegistryClient.registerCompatBlockEntityRenderer(() -> (BlockEntityType<ScannerTile>) Tiles.NORMAL_SCANNER_TILE.getOrNull(), ScannerRenderer::new);

        if (Config.client_filler_rendering_range_box) {
            CompatRegistryClient.registerCompatBlockEntityRenderer(() -> (BlockEntityType<FillerTile>) Tiles.NORMAL_FILLER_TILE.getOrNull(), FillerRenderer::new);
            CompatRegistryClient.registerCompatBlockEntityRenderer(() -> (BlockEntityType<FillerTile>) Tiles.ENHANCED_FILLER_TILE.getOrNull(), FillerRenderer::new);
            CompatRegistryClient.registerCompatBlockEntityRenderer(() -> (BlockEntityType<FillerTile>) Tiles.ENHANCED_FILLER_WITH_CHEST_TILE.getOrNull(), FillerRenderer::new);
        }
    }
}
