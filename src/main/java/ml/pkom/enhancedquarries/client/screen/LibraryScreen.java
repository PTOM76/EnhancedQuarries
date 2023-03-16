package ml.pkom.enhancedquarries.client.screen;

import io.netty.buffer.Unpooled;
import ml.pkom.enhancedquarries.EnhancedQuarries;
import ml.pkom.mcpitanlibarch.api.client.SimpleHandledScreen;
import ml.pkom.mcpitanlibarch.api.util.TextUtil;
import ml.pkom.mcpitanlibarch.api.util.client.ScreenUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LibraryScreen extends SimpleHandledScreen {
    private static final Identifier GUI = EnhancedQuarries.id("textures/gui/library.png");

    TextFieldWidget nameBox;

    public LibraryScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        playerInventoryTitleY = 72;
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
    }

    @Override
    public void initOverride() {
        super.initOverride();
        nameBox = new TextFieldWidget(this.textRenderer, x + 85,  y + 50, 60, 9, TextUtil.literal(""));
        nameBox.setDrawsBackground(true);
        nameBox.setFocusUnlocked(true);
        nameBox.setTextFieldFocused(false);
        nameBox.setMaxLength(256);
        nameBox.setText("");
        addDrawableChild_compatibility(nameBox);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (nameBox.isFocused()) {
            if (keyCode != 256) {
                PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                buf.writeString(nameBox.getText());
                ClientPlayNetworking.send(EnhancedQuarries.id("blueprint_name"), buf);
            }
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (nameBox.isFocused()) {
            if (keyCode != 256) {
                return nameBox.keyPressed(keyCode, scanCode, modifiers);
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public void removed() {
        super.removed();
        if (nameBox.isFocused())
            ScreenUtil.setRepeatEvents(false);
    }
    
    @Override
    public void drawBackgroundOverride(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        ScreenUtil.setBackground(GUI);
        callDrawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    public void drawForegroundOverride(MatrixStack matrices, int mouseX, int mouseY) {
        x = (this.width - this.backgroundWidth) / 2;
        y = (this.height - this.backgroundHeight) / 2;
        this.titleX = backgroundWidth / 2 - textRenderer.getWidth(TextUtil.translatable("screen.enhanced_quarries.library.title")) / 2;
        this.titleY = 6;
        super.drawForegroundOverride(matrices, mouseX, mouseY);
    }

    @Override
    public void renderOverride(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        callRenderBackground(matrices);
        super.renderOverride(matrices, mouseX, mouseY, delta);
        callDrawMouseoverTooltip(matrices, mouseX, mouseY);
    }
}
