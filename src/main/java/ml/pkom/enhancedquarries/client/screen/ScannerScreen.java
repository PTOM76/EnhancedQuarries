package ml.pkom.enhancedquarries.client.screen;

import ml.pkom.enhancedquarries.EnhancedQuarries;
import ml.pkom.enhancedquarries.client.screen.base.BaseHandledScreen;
import ml.pkom.mcpitanlibarch.api.client.render.handledscreen.DrawForegroundArgs;
import ml.pkom.mcpitanlibarch.api.util.TextUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ScannerScreen extends BaseHandledScreen {

    public ScannerScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        titleX = backgroundWidth / 2 - textRenderer.getWidth(TextUtil.translatable("screen.enhanced_quarries.scanner.title")) / 2;
        titleY = 7;

        playerInventoryTitleY = 72;
        setBackgroundWidth(176);
        setBackgroundHeight(166);
    }

    @Override
    public Identifier getTexture() {
        return EnhancedQuarries.id("textures/gui/scanner.png");
    }

    @Override
    public void drawForegroundOverride(DrawForegroundArgs args) {
        super.drawForegroundOverride(args);
        //ScreenUtil.RendererUtil.drawText(textRenderer, args.drawObjectDM, TextUtil.translatable("screen.enhanced_quarries.scanner.title"), backgroundWidth / 2 - textRenderer.getWidth(TextUtil.translatable("screen.enhanced_quarries.scanner.title")) / 2, 7, 4210752);
    }
}
