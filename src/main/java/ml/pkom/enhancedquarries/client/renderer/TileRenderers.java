package ml.pkom.enhancedquarries.client.renderer;

import ml.pkom.enhancedquarries.Tiles;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;

public class TileRenderers {
    public static void init() {
        BlockEntityRendererRegistry.register(Tiles.NORMAL_MARKER, MarkerRenderer::new);
    }
}
