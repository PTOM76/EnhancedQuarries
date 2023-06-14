package ml.pkom.enhancedquarries.client.screen;

import ml.pkom.enhancedquarries.EnhancedQuarries;
import ml.pkom.enhancedquarries.screen.LibraryScreenHandler;
import ml.pkom.mcpitanlibarch.api.client.SimpleHandledScreen;
import ml.pkom.mcpitanlibarch.api.client.render.handledscreen.DrawBackgroundArgs;
import ml.pkom.mcpitanlibarch.api.client.render.handledscreen.DrawForegroundArgs;
import ml.pkom.mcpitanlibarch.api.client.render.handledscreen.DrawMouseoverTooltipArgs;
import ml.pkom.mcpitanlibarch.api.client.render.handledscreen.RenderArgs;
import ml.pkom.mcpitanlibarch.api.network.ClientNetworking;
import ml.pkom.mcpitanlibarch.api.network.PacketByteUtil;
import ml.pkom.mcpitanlibarch.api.util.TextUtil;
import ml.pkom.mcpitanlibarch.api.util.client.ScreenUtil;
import net.minecraft.client.gui.widget.TextFieldWidget;
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
        setBackgroundWidth(176);
        setBackgroundHeight(166);
    }

    @Override
    public void initOverride() {
        super.initOverride();
        nameBox = new TextFieldWidget(this.textRenderer, x + 85,  y + 40, 60, 9, TextUtil.literal(""));
        nameBox.setDrawsBackground(true);
        nameBox.setFocusUnlocked(true);
        ScreenUtil.TextFieldUtil.setFocused(nameBox, false);
        nameBox.setMaxLength(256);
        nameBox.setText("");
        addDrawableChild_compatibility(nameBox);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (nameBox.isFocused()) {
            if (keyCode != 256) {
                PacketByteBuf buf = PacketByteUtil.create();
                buf.writeString(nameBox.getText());
                ClientNetworking.send(EnhancedQuarries.id("blueprint_name"), buf);
                ((LibraryScreenHandler) handler).setBlueprintName(nameBox.getText());
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
    public void drawBackgroundOverride(DrawBackgroundArgs args) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        callDrawTexture(args.drawObjectDM, GUI, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    public void drawForegroundOverride(DrawForegroundArgs args) {
        x = (this.width - this.backgroundWidth) / 2;
        y = (this.height - this.backgroundHeight) / 2;
        this.titleX = backgroundWidth / 2 - textRenderer.getWidth(TextUtil.translatable("screen.enhanced_quarries.library.title")) / 2;
        this.titleY = 6;
        super.drawForegroundOverride(args);
    }

    @Override
    public void renderOverride(RenderArgs args) {
        callRenderBackground(args.drawObjectDM);
        super.renderOverride(args);
        callDrawMouseoverTooltip(new DrawMouseoverTooltipArgs(args.drawObjectDM, args.mouseX, args.mouseY));
    }
}
