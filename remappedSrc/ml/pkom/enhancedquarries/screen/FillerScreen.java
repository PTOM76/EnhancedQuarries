package ml.pkom.enhancedquarries.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import ml.pkom.enhancedquarries.EnhancedQuarries;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class FillerScreen extends HandledScreen<FillerScreenHandler> {
    private static final Identifier GUI = EnhancedQuarries.id("textures/gui/filler.png");

    public FillerScreen(FillerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        playerInventoryTitleY = 143;
        this.backgroundWidth = 176;
        this.backgroundHeight = 241;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        MinecraftClient.getInstance().getTextureManager().bindTexture(GUI);
        drawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        super.drawForeground(matrices, mouseX, mouseY);
        x = (this.width - this.backgroundWidth) / 2;
        y = (this.height - this.backgroundHeight) / 2;
        this.textRenderer.draw(matrices, new TranslatableText("screen.enhanced_quarries.filler.title"), 45, 7, 4210752);
        this.textRenderer.draw(matrices, new TranslatableText("screen.enhanced_quarries.filler.title2"), 8, 75, 4210752);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }
}
