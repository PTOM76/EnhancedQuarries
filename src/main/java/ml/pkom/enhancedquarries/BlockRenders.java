package ml.pkom.enhancedquarries;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class BlockRenders {
    public static void init() {
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.FRAME, RenderLayer.getCutout());
    }
}
