package ml.pkom.enhancedquarries.client.renderer;

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import ml.pkom.enhancedquarries.Tiles;
import ml.pkom.enhancedquarries.tile.MarkerTile;
import net.minecraft.block.entity.BlockEntityType;

public class TileRenderers {
    public static void init() {
        BlockEntityRendererRegistry.register((BlockEntityType<MarkerTile>) Tiles.NORMAL_MARKER.getOrNull(), MarkerRenderer::new);
    }
}
