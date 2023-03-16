package ml.pkom.enhancedquarries.client.renderer;

import ml.pkom.enhancedquarries.EnhancedQuarries;
import ml.pkom.enhancedquarries.block.NormalMarker;
import ml.pkom.enhancedquarries.event.BlockStatePos;
import ml.pkom.enhancedquarries.tile.MarkerTile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

// 未完成というかわからない；；(do not know)
public class MarkerRenderer implements BlockEntityRenderer<MarkerTile> {

    public MarkerRenderer(BlockEntityRendererFactory.Context context) {}

    @Override
    public void render(MarkerTile entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockPos pos = entity.getPos();

        List<BlockStatePos> markerList = new ArrayList<>();
        NormalMarker.searchMarker(entity.getWorld(), pos, markerList);
        if (markerList.size() < 3) return;
        VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getCutoutMipped());

        MinecraftClient.getInstance().getProfiler().push(EnhancedQuarries.MOD_ID);
        MinecraftClient.getInstance().getProfiler().push("MarkerRenderer");
        matrices.push();

        matrices.translate(-pos.getX(), -pos.getY(), -pos.getZ());


        for (BlockStatePos statePos : markerList) {
            BlockPos anotherPos = statePos.getBlockPos();
            // 絶対値abs
            int offsetX = Math.abs(pos.getX() - anotherPos.getX());
            int offsetY = Math.abs(pos.getY() - anotherPos.getY());
            int offsetZ = Math.abs(pos.getZ() - anotherPos.getZ());

            buffer.vertex(offsetX, offsetY, offsetZ).color(255, 0 ,0, 255);
        }

        matrices.pop();
        MinecraftClient.getInstance().getProfiler().pop();
        MinecraftClient.getInstance().getProfiler().pop();
    }

    @Override
    public int getRenderDistance() {
        return 256;
    }
}
