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
import net.pitan76.mcpitanlib.api.util.client.widget.TextFieldUtil;

public class LibraryScreen extends BaseHandledScreen<LibraryScreenHandler> {

    protected TextFieldWidget nameBox;

    public LibraryScreen(LibraryScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        setTitlePos(backgroundWidth / 2 - ScreenUtil.getWidth(title) / 2, 6);
        setPlayerInvTitleY(72);
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
        nameBox = TextFieldUtil.create(callGetTextRenderer(), x + 85,  y + 40, 60, 9, TextUtil.literal(""));
        TextFieldUtil.setDrawsBackground(nameBox, true);
        TextFieldUtil.setFocusUnlocked(nameBox, true);
        TextFieldUtil.setFocused(nameBox, false);
        TextFieldUtil.setMaxLength(nameBox, 256);
        TextFieldUtil.setText(nameBox, "");
        addDrawableChild_compatibility(nameBox);
    }

    @Override
    public boolean keyReleased(KeyEventArgs args) {
        if (TextFieldUtil.isFocused(nameBox)) {
            if (args.keyCode != 256) {
                String text = TextFieldUtil.getText(nameBox);
                PacketByteBuf buf = PacketByteUtil.create();
                PacketByteUtil.writeString(buf, text);
                ClientNetworking.send(EnhancedQuarries._id("blueprint_name"), buf);
                handler.setBlueprintName(text);
            }
        }
        return super.keyReleased(args);
    }

    @Override
    public boolean keyPressed(KeyEventArgs args) {
        if (TextFieldUtil.isFocused(nameBox)) {
            if (args.keyCode != 256) {
                return TextFieldUtil.keyPressed(nameBox, args.keyCode, args.scanCode, args.modifiers);
            }
        }
        return super.keyPressed(args);
    }

    @Override
    public void removedOverride() {
        super.removedOverride();
        if (TextFieldUtil.isFocused(nameBox))
            ScreenUtil.setRepeatEvents(false);
    }
}
