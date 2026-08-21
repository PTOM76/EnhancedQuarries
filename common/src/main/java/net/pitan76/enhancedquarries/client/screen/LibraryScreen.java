package net.pitan76.enhancedquarries.client.screen;

import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.pitan76.enhancedquarries.EnhancedQuarries;
import net.pitan76.enhancedquarries.client.screen.base.BaseHandledScreen;
import net.pitan76.enhancedquarries.screen.LibraryScreenHandler;
import net.pitan76.mcpitanlib.api.client.render.handledscreen.DrawForegroundArgs;
import net.pitan76.mcpitanlib.api.client.render.handledscreen.KeyEventArgs;
import net.pitan76.mcpitanlib.api.client.render.handledscreen.MouseClickedArgs;
import net.pitan76.mcpitanlib.api.client.render.handledscreen.MouseScrolledArgs;
import net.pitan76.mcpitanlib.api.network.PacketByteUtil;
import net.pitan76.mcpitanlib.api.network.v2.ClientNetworking;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.api.util.TextUtil;
import net.pitan76.mcpitanlib.api.util.client.ScreenUtil;
import net.pitan76.mcpitanlib.api.util.client.widget.TextFieldUtil;

import java.util.ArrayList;
import java.util.List;

public class LibraryScreen extends BaseHandledScreen<LibraryScreenHandler> {

    // テクスチャに描かれている左側のパネル
    public static final int LIST_X = 16;
    public static final int LIST_Y = 16;
    public static final int LIST_WIDTH = 62;
    public static final int ROW_HEIGHT = 10;
    public static final int VISIBLE_ROWS = 5;

    public static final int COLOR_BLUEPRINT = 0xFFFFFF;
    public static final int COLOR_TEMPLATE = 0x7FD8FF;
    public static final int COLOR_SELECTED = 0xFFFFA0;

    protected TextFieldWidget nameBox;

    protected int scroll = 0;

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
        TextFieldUtil.setText(nameBox, handler.blueprintName);
        addDrawableChild_compatibility(nameBox);
    }

    // ブループリントとテンプレートを1つの一覧にまとめる
    public List<String> getNames() {
        List<String> names = new ArrayList<>(handler.blueprintNames);
        names.addAll(handler.templateNames);
        return names;
    }

    public boolean isTemplate(int index) {
        return index >= handler.blueprintNames.size();
    }

    public int getMaxScroll() {
        return Math.max(0, getNames().size() - VISIBLE_ROWS);
    }

    @Override
    public void drawForegroundOverride(DrawForegroundArgs args) {
        super.drawForegroundOverride(args);

        List<String> names = getNames();
        if (scroll > getMaxScroll()) scroll = getMaxScroll();

        for (int row = 0; row < VISIBLE_ROWS; row++) {
            int index = scroll + row;
            if (index >= names.size()) break;

            String name = names.get(index);
            int rowY = LIST_Y + row * ROW_HEIGHT;

            int color = isTemplate(index) ? COLOR_TEMPLATE : COLOR_BLUEPRINT;
            if (name.equals(handler.blueprintName)) color = COLOR_SELECTED;

            ScreenUtil.RendererUtil.drawText(textRenderer, args.drawObjectDM,
                    TextUtil.literal(trim(name)), LIST_X, rowY, color);
        }
    }

    // パネルの幅に収まるよう末尾を省略する
    public String trim(String name) {
        if (ScreenUtil.getWidth(TextUtil.literal(name)) <= LIST_WIDTH) return name;

        String trimmed = name;
        while (!trimmed.isEmpty() && ScreenUtil.getWidth(TextUtil.literal(trimmed + "...")) > LIST_WIDTH) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        return trimmed + "...";
    }

    @Override
    public boolean mouseClicked(MouseClickedArgs args) {
        int localX = (int) args.getX() - x;
        int localY = (int) args.getY() - y;

        if (localX >= LIST_X && localX <= LIST_X + LIST_WIDTH
                && localY >= LIST_Y && localY < LIST_Y + VISIBLE_ROWS * ROW_HEIGHT) {
            int index = scroll + (localY - LIST_Y) / ROW_HEIGHT;
            List<String> names = getNames();

            if (index < names.size()) {
                select(names.get(index));
                return true;
            }
        }

        return super.mouseClicked(args);
    }

    public void select(String name) {
        TextFieldUtil.setText(nameBox, name);
        handler.setBlueprintName(name);
        sendName(name);
    }

    public void sendName(String name) {
        PacketByteBuf buf = PacketByteUtil.create();
        PacketByteUtil.writeString(buf, name);
        ClientNetworking.send(EnhancedQuarries._id("blueprint_name"), buf);
    }

    @Override
    public boolean mouseScrolled(MouseScrolledArgs args) {
        int localX = (int) args.getMouseX() - x;
        int localY = (int) args.getMouseY() - y;

        if (localX >= LIST_X && localX <= LIST_X + LIST_WIDTH
                && localY >= LIST_Y && localY < LIST_Y + VISIBLE_ROWS * ROW_HEIGHT) {
            scroll = Math.max(0, Math.min(getMaxScroll(), scroll - (int) Math.signum(args.getAmount())));
            return true;
        }

        return super.mouseScrolled(args);
    }

    @Override
    public boolean keyReleased(KeyEventArgs args) {
        if (TextFieldUtil.isFocused(nameBox)) {
            if (args.keyCode != 256) {
                String text = TextFieldUtil.getText(nameBox);
                sendName(text);
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
