package net.pitan76.enhancedquarries.client.renderer;

import net.minecraft.block.entity.BlockEntityType;
import net.pitan76.enhancedquarries.Tiles;
import net.pitan76.enhancedquarries.tile.MarkerTile;
import net.pitan76.mcpitanlib.api.client.registry.CompatRegistryClient;

public class TileRenderers {
    public static void init() {
        CompatRegistryClient.registerCompatBlockEntityRenderer((BlockEntityType<MarkerTile>) Tiles.NORMAL_MARKER.getOrNull(), MarkerRenderer::new);
    }
}
