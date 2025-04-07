package net.pitan76.enhancedquarries.client.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.pitan76.enhancedquarries.EnhancedQuarries;
import net.pitan76.enhancedquarries.client.screen.base.BaseHandledScreen;
import net.pitan76.enhancedquarries.screen.ScannerScreenHandler;
import net.pitan76.mcpitanlib.api.client.render.handledscreen.DrawForegroundArgs;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.api.util.client.ScreenUtil;

public class ScannerScreen extends BaseHandledScreen<ScannerScreenHandler> {

    public ScannerScreen(ScannerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        setTitlePos(backgroundWidth / 2 - ScreenUtil.getWidth(title) / 2, 7);

        playerInventoryTitleY = 72;
        setBackgroundWidth(176);
        setBackgroundHeight(166);
    }

    @Override
    public CompatIdentifier getCompatTexture() {
        return EnhancedQuarries._id("textures/gui/scanner.png");
    }

    @Override
    public void drawForegroundOverride(DrawForegroundArgs args) {
        super.drawForegroundOverride(args);
    }
}
