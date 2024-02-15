package net.pitan76.enhancedquarries.client.renderer;

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import net.minecraft.block.entity.BlockEntityType;
import net.pitan76.enhancedquarries.Tiles;
import net.pitan76.enhancedquarries.tile.MarkerTile;

public class TileRenderers {
    public static void init() {
        BlockEntityRendererRegistry.register((BlockEntityType<MarkerTile>) Tiles.NORMAL_MARKER.getOrNull(), MarkerRenderer::new);
    }
}
