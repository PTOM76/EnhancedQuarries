package net.pitan76.enhancedquarries.client.renderer;

import net.fabricmc.fabric.api.blockview.v2.FabricBlockView;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.pitan76.enhancedquarries.block.NormalMarker;
import net.pitan76.enhancedquarries.event.BlockStatePos;
import net.pitan76.enhancedquarries.tile.MarkerTile;
import net.pitan76.mcpitanlib.api.client.registry.CompatRegistryClient;
import net.pitan76.mcpitanlib.api.client.render.block.entity.event.BlockEntityRenderEvent;
import net.pitan76.mcpitanlib.api.client.render.block.entity.event.CompatBlockEntityRendererConstructArgs;
import net.pitan76.mcpitanlib.api.client.render.block.entity.v2.CompatBlockEntityRenderer;
import net.pitan76.mcpitanlib.api.util.IdentifierUtil;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

// 未完成というかわからない；；しね
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
        BlockPos pos = entity.getPos();

        if (entity.maxX == null || entity.maxY == null || entity.maxZ == null || entity.minX == null || entity.minY == null || entity.minZ == null)
            return;

        double maxX = entity.maxX - pos.getX();
        double maxY = entity.maxY - pos.getY();
        double maxZ = entity.maxZ - pos.getZ();
        double minX = entity.minX - pos.getX();
        double minY = entity.minY - pos.getY();
        double minZ = entity.minZ - pos.getZ();

        e.push();
        VertexConsumer buffer = e.getVertexConsumer(RenderLayer.getLines());
        WorldRenderer.drawBox(e.matrices, buffer, minX + 0.5, minY + 0.5, minZ + 0.5, maxX + 0.5, maxY + 0.5, maxZ + 0.5, 1F, 0.0F, 0.0F, 1.0F);
        e.pop();
    }

    @Override
    public int getRenderDistance() {
        return 64;
    }

    @Override
    public boolean rendersOutsideBoundingBox(MarkerTile blockEntity) {
        return true;
    }

    @Override
    public boolean isInRenderDistance(MarkerTile blockEntity, Vec3d pos) {
        return super.isInRenderDistance(blockEntity, pos);
    }
}
