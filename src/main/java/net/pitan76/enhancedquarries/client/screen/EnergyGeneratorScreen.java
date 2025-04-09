package net.pitan76.enhancedquarries.client.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.pitan76.enhancedquarries.EnhancedQuarries;
import net.pitan76.enhancedquarries.client.screen.base.BaseHandledScreen;
import net.pitan76.enhancedquarries.screen.EnergyGeneratorScreenHandler;
import net.pitan76.mcpitanlib.api.client.render.handledscreen.DrawBackgroundArgs;
import net.pitan76.mcpitanlib.api.client.render.handledscreen.DrawForegroundArgs;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.api.util.TextUtil;
import net.pitan76.mcpitanlib.api.util.client.RenderUtil;
import net.pitan76.mcpitanlib.api.util.client.ScreenUtil;
import net.pitan76.mcpitanlib.guilib.GuiTextures;

public class EnergyGeneratorScreen extends BaseHandledScreen<EnergyGeneratorScreenHandler> {

    public EnergyGeneratorScreenHandler screenHandler;

    public EnergyGeneratorScreen(EnergyGeneratorScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        setTitlePos(backgroundWidth / 2 - ScreenUtil.getWidth(title) / 2, 7);

        setPlayerInvTitleY(72);
        setBackgroundWidth(176);
        setBackgroundHeight(166);
        screenHandler = handler;
    }

    @Override
    public CompatIdentifier getCompatTexture() {
        return EnhancedQuarries._id("textures/gui/energy_generator.png");
    }

    @Override
    public void drawForegroundOverride(DrawForegroundArgs args) {
        super.drawForegroundOverride(args);
        int textWidth = ScreenUtil.getWidth(TextUtil.literal(String.format("%d / %d", screenHandler.energy, screenHandler.maxEnergy)));
        ScreenUtil.RendererUtil.drawText(callGetTextRenderer(), args.drawObjectDM, TextUtil.literal(String.format("%d / %d EU", screenHandler.energy, screenHandler.maxEnergy)), 54 - textWidth / 2, 60, 4210752);
    }

    @Override
    public void drawBackgroundOverride(DrawBackgroundArgs args) {
        super.drawBackgroundOverride(args);
        drawEnergyBar(args);

        if (screenHandler.maxBurnTime > 0) {
            double percentage = ((double) screenHandler.burnTime / screenHandler.maxBurnTime) * 100;
            if (percentage > 0) {
                int progress = (int) (percentage / 100 * 16);
                RenderUtil.RendererUtil.drawTexture(args.drawObjectDM, GuiTextures.BASE_FURNACE_BACKGROUND, x + backgroundWidth / 2 - 8, y + 41 + 16 - progress, 0, 182 + 16 - progress,16, progress);
            }
        }
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
        callDrawTexture(args.drawObjectDM, getCompatTexture(), x, energyBarY, 176, height - energyBarHeight, width, energyBarHeight);
    }
}
