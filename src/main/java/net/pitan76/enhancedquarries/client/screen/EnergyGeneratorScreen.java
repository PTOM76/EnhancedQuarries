package net.pitan76.enhancedquarries.client.screen;

import net.pitan76.enhancedquarries.EnhancedQuarries;
import net.pitan76.enhancedquarries.client.screen.base.BaseHandledScreen;
import net.pitan76.enhancedquarries.screen.EnergyGeneratorScreenHandler;
import ml.pkom.mcpitanlibarch.api.client.render.handledscreen.DrawBackgroundArgs;
import ml.pkom.mcpitanlibarch.api.client.render.handledscreen.DrawForegroundArgs;
import ml.pkom.mcpitanlibarch.api.util.client.ScreenUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.pitan76.mcpitanlib.api.util.TextUtil;

public class EnergyGeneratorScreen extends BaseHandledScreen {

    public EnergyGeneratorScreenHandler screenHandler;

    public EnergyGeneratorScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        titleX = backgroundWidth / 2 - ScreenUtil.getWidth(title) / 2;
        titleY = 7;

        playerInventoryTitleY = 72;
        setBackgroundWidth(176);
        setBackgroundHeight(166);
        if (handler instanceof EnergyGeneratorScreenHandler)
            screenHandler = (EnergyGeneratorScreenHandler) handler;
    }

    @Override
    public Identifier getTexture() {
        return EnhancedQuarries.id("textures/gui/energy_generator.png");
    }

    @Override
    public void drawForegroundOverride(DrawForegroundArgs args) {
        super.drawForegroundOverride(args);
        int textWidth = ScreenUtil.getWidth(TextUtil.literal(String.format("%d / %d", screenHandler.energy, screenHandler.maxEnergy)));
        ScreenUtil.RendererUtil.drawText(textRenderer, args.drawObjectDM, TextUtil.literal(String.format("%d / %d EU", screenHandler.energy, screenHandler.maxEnergy)), 54 - textWidth / 2, 60, 4210752);
    }

    @Override
    public void drawBackgroundOverride(DrawBackgroundArgs args) {
        super.drawBackgroundOverride(args);
        drawEnergyBar(args);
    }

    public void drawEnergyBar(DrawBackgroundArgs args) {
        int x = 47 + this.x;
        int y = 11 + this.y;
        int width = 13;
        int height = 45;
        long energy = screenHandler.energy;
        long maxEnergy = screenHandler.maxEnergy;
        int energyBarHeight = (int) ((double) energy / (double) maxEnergy * (double) height);
        int energyBarY = y + height - energyBarHeight;
        callDrawTexture(args.drawObjectDM, getTexture(), x, energyBarY, 176, 0, width, energyBarHeight);
    }
}
