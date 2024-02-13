package net.pitan76.enhancedquarries.client.screen;

import net.pitan76.enhancedquarries.EnhancedQuarries;
import net.pitan76.enhancedquarries.client.screen.base.BaseHandledScreen;
import ml.pkom.mcpitanlibarch.api.client.render.handledscreen.DrawForegroundArgs;
import ml.pkom.mcpitanlibarch.api.util.client.ScreenUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.pitan76.mcpitanlib.api.util.TextUtil;

public class FillerScreen extends BaseHandledScreen {

    public FillerScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        titleX = 45;
        titleY = 7;

        playerInventoryTitleY = 143;
        setBackgroundWidth(176);
        setBackgroundHeight(235);
    }

    @Override
    public Identifier getTexture() {
        return EnhancedQuarries.id("textures/gui/filler.png");
    }

    @Override
    public void drawForegroundOverride(DrawForegroundArgs args) {
        super.drawForegroundOverride(args);
        ScreenUtil.RendererUtil.drawText(textRenderer, args.drawObjectDM, TextUtil.translatable("screen.enhanced_quarries.filler.title2"), 8, 75, 4210752);
    }
}
