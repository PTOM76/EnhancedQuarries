package ml.pkom.enhancedquarries.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.util.math.BlockPos;

// 未完成というかわからない；；(do not know)
public class MarkerRenderer {
    public static void render(BlockPos pos) {

        RenderSystem.enableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableTexture();
        RenderSystem.disableBlend();
        RenderSystem.lineWidth(1.0F);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder render = tessellator.getBuffer();

        render.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        tessellator.draw();

        RenderSystem.enableBlend();
        RenderSystem.enableTexture();
    }
}
