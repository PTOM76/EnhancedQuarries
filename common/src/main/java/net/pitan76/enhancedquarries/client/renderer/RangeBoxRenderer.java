package net.pitan76.enhancedquarries.client.renderer;

import net.pitan76.enhancedquarries.tile.base.RangeTile;
import net.pitan76.mcpitanlib.api.client.registry.CompatRegistryClient;
import net.pitan76.mcpitanlib.api.client.render.CompatRenderLayer;
import net.pitan76.mcpitanlib.api.client.render.DrawObjectMV;
import net.pitan76.mcpitanlib.api.client.render.block.entity.event.BlockEntityRenderEvent;
import net.pitan76.mcpitanlib.api.client.render.block.entity.event.CompatBlockEntityRendererConstructArgs;
import net.pitan76.mcpitanlib.api.client.render.block.entity.v2.CompatBlockEntityRenderer;
import net.pitan76.mcpitanlib.api.tile.CompatBlockEntity;
import net.pitan76.mcpitanlib.api.util.client.render.VertexRenderingUtil;
import net.pitan76.mcpitanlib.midohra.util.math.BlockPos;

public abstract class RangeBoxRenderer<T extends CompatBlockEntity & RangeTile> extends CompatBlockEntityRenderer<T> {

    public RangeBoxRenderer(CompatBlockEntityRendererConstructArgs args) {
        super(args);
    }

    public RangeBoxRenderer(CompatRegistryClient.BlockEntityRendererFactory.Context ctx) {
        super(ctx);
    }

    public abstract float getRed();

    public abstract float getGreen();

    public abstract float getBlue();

    @Override
    public void render(BlockEntityRenderEvent<T> e) {
        T entity = e.getBlockEntity();
        if (entity == null) return;

        BlockPos pos1 = entity.getPos1();
        BlockPos pos2 = entity.getPos2();
        if (pos1 == null || pos2 == null) return;

        BlockPos pos = entity.getMidohraPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        double minX = pos1.getX() - x;
        double minY = pos1.getY() - y;
        double minZ = pos1.getZ() - z;
        double maxX = pos2.getX() - x + 1;
        double maxY = pos2.getY() - y + 1;
        double maxZ = pos2.getZ() - z + 1;

        e.push();
        DrawObjectMV drawObject = e.getDrawObject(CompatRenderLayer.LINES);
        VertexRenderingUtil.drawBox(drawObject, minX, minY, minZ, maxX, maxY, maxZ, getRed(), getGreen(), getBlue(), 1.0F);
        e.pop();
    }

    @Override
    public int getRenderDistanceOverride() {
        return 64;
    }

    @Override
    public boolean rendersOutsideBoundingBoxOverride(T blockEntity) {
        return true;
    }
}
