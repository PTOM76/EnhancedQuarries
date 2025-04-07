package net.pitan76.enhancedquarries.client.screen;

import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.pitan76.enhancedquarries.EnhancedQuarries;
import net.pitan76.enhancedquarries.client.screen.base.BaseHandledScreen;
import net.pitan76.enhancedquarries.screen.LibraryScreenHandler;
import net.pitan76.mcpitanlib.api.client.render.handledscreen.KeyEventArgs;
import net.pitan76.mcpitanlib.api.network.PacketByteUtil;
import net.pitan76.mcpitanlib.api.network.v2.ClientNetworking;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.api.util.TextUtil;
import net.pitan76.mcpitanlib.api.util.client.ScreenUtil;

public class LibraryScreen extends BaseHandledScreen<LibraryScreenHandler> {

    protected TextFieldWidget nameBox;

    public LibraryScreen(LibraryScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        setTitlePos(backgroundWidth / 2 - ScreenUtil.getWidth(title) / 2, 6);
        playerInventoryTitleY = 72;
        setBackgroundWidth(176);
        setBackgroundHeight(166);
    }

    @Override
    public CompatIdentifier getCompatTexture() {
        return EnhancedQuarries._id("textures/gui/library.png");
    }

    @Override
    public void initOverride() {
        super.initOverride();
        nameBox = new TextFieldWidget(this.textRenderer, x + 85,  y + 40, 60, 9, TextUtil.literal(""));
        nameBox.setDrawsBackground(true);
        nameBox.setFocusUnlocked(true);
        ScreenUtil.TextFieldUtil.setFocused(nameBox, false);
        ScreenUtil.TextFieldUtil.setMaxLength(nameBox, 256);
        nameBox.setText("");
        addDrawableChild_compatibility(nameBox);
    }

    @Override
    public boolean keyReleased(KeyEventArgs args) {
        if (nameBox.isFocused()) {
            if (args.keyCode != 256) {
                PacketByteBuf buf = PacketByteUtil.create();
                PacketByteUtil.writeString(buf, nameBox.getText());
                ClientNetworking.send(EnhancedQuarries._id("blueprint_name"), buf);
                handler.setBlueprintName(nameBox.getText());
            }
        }
        return super.keyReleased(args);
    }

    @Override
    public boolean keyPressed(KeyEventArgs args) {
        if (nameBox.isFocused()) {
            if (args.keyCode != 256) {
                return nameBox.keyPressed(args.keyCode, args.scanCode, args.modifiers);
            }
        }
        return super.keyPressed(args);
    }

    @Override
    public void removedOverride() {
        super.removedOverride();
        if (nameBox.isFocused())
            ScreenUtil.setRepeatEvents(false);
    }
}
