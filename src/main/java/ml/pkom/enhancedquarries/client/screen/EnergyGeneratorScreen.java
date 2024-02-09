package ml.pkom.enhancedquarries.client.screen;

import ml.pkom.enhancedquarries.EnhancedQuarries;
import ml.pkom.enhancedquarries.client.screen.base.BaseHandledScreen;
import ml.pkom.enhancedquarries.screen.EnergyGeneratorScreenHandler;
import ml.pkom.mcpitanlibarch.api.client.render.handledscreen.DrawBackgroundArgs;
import ml.pkom.mcpitanlibarch.api.client.render.handledscreen.DrawForegroundArgs;
import ml.pkom.mcpitanlibarch.api.util.TextUtil;
import ml.pkom.mcpitanlibarch.api.util.client.ScreenUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class EnergyGeneratorScreen extends BaseHandledScreen {

    public EnergyGeneratorScreenHandler screenHandler;

    public EnergyGeneratorScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        titleX = backgroundWidth / 2 - ScreenUtil.getWidth(title) / 2;
        titleY = 7;

        playerInventoryTitleY = 72;
        setBackgroundWidth(176);
        setBackgroundHeight(166);
        if (handler instanceof EnergyGeneratorScreenHandler) {
            screenHandler = (EnergyGeneratorScreenHandler) handler;
        }
    }

    @Override
    public Identifier getTexture() {
        return EnhancedQuarries.id("textures/gui/energy_generator.png");
    }

    @Override
    public void drawForegroundOverride(DrawForegroundArgs args) {
        super.drawForegroundOverride(args);
        ScreenUtil.RendererUtil.drawText(textRenderer, args.drawObjectDM, TextUtil.literal(String.format("%d / %d", screenHandler.energy, screenHandler.maxEnergy)), 48, 60, 4210752);
    }

    @Override
    public void drawBackgroundOverride(DrawBackgroundArgs args) {
        super.drawBackgroundOverride(args);
        drawEnergyBar(args);
    }

    public void drawEnergyBar(DrawBackgroundArgs args) {
        int x = 48;
        int y = 12;
        int width = 12;
        int height = 44;
        long energy = screenHandler.energy;
        long maxEnergy = screenHandler.maxEnergy;
        int energyBarHeight = (int) ((double) energy / (double) maxEnergy * (double) height);
        int energyBarY = y + height - energyBarHeight;
        callDrawTexture(args.drawObjectDM, getTexture(), x, energyBarY, 192, 0, width, energyBarHeight);
    }
}
