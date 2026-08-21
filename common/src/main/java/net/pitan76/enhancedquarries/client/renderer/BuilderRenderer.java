package net.pitan76.enhancedquarries.client.renderer;

import net.minecraft.util.math.BlockPos;
import net.pitan76.enhancedquarries.tile.base.BuilderTile;
import net.pitan76.mcpitanlib.api.client.registry.CompatRegistryClient;
import net.pitan76.mcpitanlib.api.client.render.CompatRenderLayer;
import net.pitan76.mcpitanlib.api.client.render.DrawObjectMV;
import net.pitan76.mcpitanlib.api.client.render.block.entity.event.BlockEntityRenderEvent;
import net.pitan76.mcpitanlib.api.client.render.block.entity.event.CompatBlockEntityRendererConstructArgs;
import net.pitan76.mcpitanlib.api.client.render.block.entity.v2.CompatBlockEntityRenderer;
import net.pitan76.mcpitanlib.api.util.client.render.VertexRenderingUtil;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;

public class BuilderRenderer extends CompatBlockEntityRenderer<BuilderTile> {

    public BuilderRenderer(CompatBlockEntityRendererConstructArgs args) {
        super(args);
    }

    public BuilderRenderer(CompatRegistryClient.BlockEntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(BlockEntityRenderEvent<BuilderTile> e) {
        BuilderTile entity = e.getBlockEntity();
        if (entity == null) return;

        net.pitan76.mcpitanlib.midohra.util.math.BlockPos pos1 = entity.getPos1();
        net.pitan76.mcpitanlib.midohra.util.math.BlockPos pos2 = entity.getPos2();
        if (pos1 == null || pos2 == null) return;

        BlockPos pos = entity.callGetPos();
        int x = PosUtil.x(pos);
        int y = PosUtil.y(pos);
        int z = PosUtil.z(pos);

        double minX = pos1.getX() - x;
        double minY = pos1.getY() - y;
        double minZ = pos1.getZ() - z;
        double maxX = pos2.getX() - x + 1;
        double maxY = pos2.getY() - y + 1;
        double maxZ = pos2.getZ() - z + 1;

        e.push();
        DrawObjectMV drawObject = e.getDrawObject(CompatRenderLayer.LINES);
        VertexRenderingUtil.drawBox(drawObject, minX, minY, minZ, maxX, maxY, maxZ, 0.0F, 0.6F, 1.0F, 1.0F);
        e.pop();
    }

    @Override
    public int getRenderDistanceOverride() {
        return 64;
    }

    @Override
    public boolean rendersOutsideBoundingBoxOverride(BuilderTile blockEntity) {
        return true;
    }
}
