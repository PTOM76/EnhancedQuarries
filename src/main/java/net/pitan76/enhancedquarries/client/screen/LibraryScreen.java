package net.pitan76.enhancedquarries.client.screen;

import net.pitan76.enhancedquarries.EnhancedQuarries;
import net.pitan76.enhancedquarries.client.screen.base.BaseHandledScreen;
import net.pitan76.enhancedquarries.screen.LibraryScreenHandler;
import ml.pkom.mcpitanlibarch.api.client.render.handledscreen.KeyEventArgs;
import ml.pkom.mcpitanlibarch.api.network.ClientNetworking;
import ml.pkom.mcpitanlibarch.api.network.PacketByteUtil;
import ml.pkom.mcpitanlibarch.api.util.client.ScreenUtil;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.pitan76.mcpitanlib.api.util.TextUtil;

public class LibraryScreen extends BaseHandledScreen {

    protected TextFieldWidget nameBox;

    public LibraryScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.titleX = backgroundWidth / 2 - ScreenUtil.getWidth(title) / 2;
        this.titleY = 6;

        playerInventoryTitleY = 72;
        setBackgroundWidth(176);
        setBackgroundHeight(166);
    }

    @Override
    public Identifier getTexture() {
        return EnhancedQuarries.id("textures/gui/library.png");
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
    public boolean keyReleased(KeyEventArgs args) {
        if (nameBox.isFocused()) {
            if (args.keyCode != 256) {
                PacketByteBuf buf = PacketByteUtil.create();
                PacketByteUtil.writeString(buf, nameBox.getText());
                ClientNetworking.send(EnhancedQuarries.id("blueprint_name"), buf);
                ((LibraryScreenHandler) handler).setBlueprintName(nameBox.getText());
            }
        }
        return super.keyReleased(args);
    }

    public boolean keyPressed(KeyEventArgs args) {
        if (nameBox.isFocused()) {
            if (args.keyCode != 256) {
                return nameBox.keyPressed(args.keyCode, args.scanCode, args.modifiers);
            }
        }
        return super.keyPressed(args);
    }

    public void removed() {
        super.removed();
        if (nameBox.isFocused())
            ScreenUtil.setRepeatEvents(false);
    }
}
