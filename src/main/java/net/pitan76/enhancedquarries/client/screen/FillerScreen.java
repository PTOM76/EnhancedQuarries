package net.pitan76.enhancedquarries.client.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.pitan76.enhancedquarries.EnhancedQuarries;
import net.pitan76.enhancedquarries.client.screen.base.BaseHandledScreen;
import net.pitan76.enhancedquarries.screen.FillerScreenHandler;
import net.pitan76.mcpitanlib.api.client.render.handledscreen.DrawForegroundArgs;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.api.util.TextUtil;
import net.pitan76.mcpitanlib.api.util.client.ScreenUtil;

public class FillerScreen extends BaseHandledScreen<FillerScreenHandler> {

    public FillerScreen(FillerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        setTitlePos(45, 8);

        playerInventoryTitleY = 143;
        setBackgroundWidth(176);
        setBackgroundHeight(235);
    }

    @Override
    public CompatIdentifier getCompatTexture() {
        return EnhancedQuarries._id("textures/gui/filler.png");
    }

    @Override
    public void drawForegroundOverride(DrawForegroundArgs args) {
        super.drawForegroundOverride(args);
        ScreenUtil.RendererUtil.drawText(textRenderer, args.drawObjectDM, TextUtil.translatable("screen.enhanced_quarries.filler.title2"), 8, 75, 4210752);
    }
}
