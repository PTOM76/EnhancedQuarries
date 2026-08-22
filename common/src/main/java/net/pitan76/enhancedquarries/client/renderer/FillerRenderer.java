package net.pitan76.enhancedquarries.client.renderer;

import net.pitan76.enhancedquarries.tile.base.FillerTile;
import net.pitan76.mcpitanlib.api.client.registry.CompatRegistryClient;
import net.pitan76.mcpitanlib.api.client.render.block.entity.event.CompatBlockEntityRendererConstructArgs;

public class FillerRenderer extends RangeBoxRenderer<FillerTile> {

    public FillerRenderer(CompatBlockEntityRendererConstructArgs args) {
        super(args);
    }

    public FillerRenderer(CompatRegistryClient.BlockEntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public float getRed() {
        return 0.0F;
    }

    @Override
    public float getGreen() {
        return 0.6F;
    }

    @Override
    public float getBlue() {
        return 1.0F;
    }
}
