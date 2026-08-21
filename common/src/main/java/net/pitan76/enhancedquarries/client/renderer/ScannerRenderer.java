package net.pitan76.enhancedquarries.client.renderer;

import net.pitan76.enhancedquarries.tile.base.ScannerTile;
import net.pitan76.mcpitanlib.api.client.registry.CompatRegistryClient;
import net.pitan76.mcpitanlib.api.client.render.block.entity.event.CompatBlockEntityRendererConstructArgs;

public class ScannerRenderer extends RangeBoxRenderer<ScannerTile> {

    public ScannerRenderer(CompatBlockEntityRendererConstructArgs args) {
        super(args);
    }

    public ScannerRenderer(CompatRegistryClient.BlockEntityRendererFactory.Context ctx) {
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
