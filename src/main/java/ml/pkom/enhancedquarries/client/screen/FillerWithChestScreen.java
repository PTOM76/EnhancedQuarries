package ml.pkom.enhancedquarries.client.screen;

import ml.pkom.enhancedquarries.EnhancedQuarries;
import ml.pkom.mcpitanlibarch.api.client.render.handledscreen.DrawForegroundArgs;
import ml.pkom.mcpitanlibarch.api.util.client.ScreenUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class FillerWithChestScreen extends FillerScreen {

    public FillerWithChestScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        titleX = 45;
        titleY = 7;

        playerInventoryTitleY = 143;
        setBackgroundWidth(238);
        setBackgroundHeight(235);
    }

    @Override
    public Identifier getTexture() {
        return EnhancedQuarries.id("textures/gui/filler_with_chest.png");
    }

    @Override
    public void drawForegroundOverride(DrawForegroundArgs args) {
        super.drawForegroundOverride(args);
        ScreenUtil.RendererUtil.drawText(textRenderer, args.drawObjectDM, Blocks.CHEST.getName(), 180, 4, 4210752);
    }
}
