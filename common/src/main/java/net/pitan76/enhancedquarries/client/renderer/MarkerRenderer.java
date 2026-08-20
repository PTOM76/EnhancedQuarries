package net.pitan76.enhancedquarries.client.renderer;

import net.minecraft.util.math.BlockPos;
import net.pitan76.enhancedquarries.tile.MarkerTile;
import net.pitan76.mcpitanlib.api.client.registry.CompatRegistryClient;
import net.pitan76.mcpitanlib.api.client.render.CompatRenderLayer;
import net.pitan76.mcpitanlib.api.client.render.DrawObjectMV;
import net.pitan76.mcpitanlib.api.client.render.block.entity.event.BlockEntityRenderEvent;
import net.pitan76.mcpitanlib.api.client.render.block.entity.event.CompatBlockEntityRendererConstructArgs;
import net.pitan76.mcpitanlib.api.client.render.block.entity.v2.CompatBlockEntityRenderer;
import net.pitan76.mcpitanlib.api.util.client.render.VertexRenderingUtil;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;

public class MarkerRenderer extends CompatBlockEntityRenderer<MarkerTile> {

    public MarkerRenderer(CompatBlockEntityRendererConstructArgs args) {
        super(args);
    }

    public MarkerRenderer(CompatRegistryClient.BlockEntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(BlockEntityRenderEvent<MarkerTile> e) {
        MarkerTile entity = e.getBlockEntity();
        if (e.blockEntity == null) return;

        BlockPos pos = entity.callGetPos();

        if (entity.maxX == null || entity.maxY == null || entity.maxZ == null
                || entity.minX == null || entity.minY == null || entity.minZ == null)
            return;

        int x = PosUtil.x(pos);
        int y = PosUtil.y(pos);
        int z = PosUtil.z(pos);

        double maxX = entity.maxX - x;
        double maxY = entity.maxY - y;
        double maxZ = entity.maxZ - z;
        double minX = entity.minX - x;
        double minY = entity.minY - y;
        double minZ = entity.minZ - z;

        e.push();
        DrawObjectMV drawObject = e.getDrawObject(CompatRenderLayer.LINES);
        VertexRenderingUtil.drawBox(drawObject, minX + 0.5, minY + 0.5, minZ + 0.5, maxX + 0.5, maxY + 0.5, maxZ + 0.5, 1F, 0.0F, 0.0F, 1.0F);
        e.pop();
    }

    @Override
    public int getRenderDistanceOverride() {
        return 64;
    }

    @Override
    public boolean rendersOutsideBoundingBoxOverride(MarkerTile blockEntity) {
        return true;
    }
}
