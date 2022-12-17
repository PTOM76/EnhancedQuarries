package ml.pkom.enhancedquarries.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import ml.pkom.enhancedquarries.EnhancedQuarries;
import ml.pkom.enhancedquarries.screen.BuilderScreenHandler;
import ml.pkom.enhancedquarries.screen.FillerScreenHandler;
import ml.pkom.mcpitanlibarch.api.util.TextUtil;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BuilderScreen extends HandledScreen<BuilderScreenHandler> {
    private static final Identifier GUI = EnhancedQuarries.id("textures/gui/builder.png");

    public BuilderScreen(BuilderScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        playerInventoryTitleY = 142;
        this.backgroundWidth = 238;
        this.backgroundHeight = 235;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI);
        drawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        super.drawForeground(matrices, mouseX, mouseY);
        x = (this.width - this.backgroundWidth) / 2;
        y = (this.height - this.backgroundHeight) / 2;
        this.textRenderer.draw(matrices, TextUtil.translatable("screen.enhanced_quarries.builder.title"), backgroundWidth / 2 - textRenderer.getWidth(TextUtil.translatable("screen.enhanced_quarries.builder.title")) / 2, 7, 4210752);
        this.textRenderer.draw(matrices, TextUtil.translatable("screen.enhanced_quarries.builder.title2"), 8, 75, 4210752);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }
}
