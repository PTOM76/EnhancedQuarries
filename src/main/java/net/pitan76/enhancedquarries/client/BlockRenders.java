package net.pitan76.enhancedquarries.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.pitan76.enhancedquarries.Blocks;

public class BlockRenders {
    public static void init() {
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.FRAME, RenderLayer.getCutout());
    }
}
