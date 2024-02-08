package ml.pkom.enhancedquarries.client.screen.base;

import ml.pkom.mcpitanlibarch.api.client.SimpleHandledScreen;
import ml.pkom.mcpitanlibarch.api.client.render.handledscreen.DrawBackgroundArgs;
import ml.pkom.mcpitanlibarch.api.client.render.handledscreen.DrawForegroundArgs;
import ml.pkom.mcpitanlibarch.api.client.render.handledscreen.DrawMouseoverTooltipArgs;
import ml.pkom.mcpitanlibarch.api.client.render.handledscreen.RenderArgs;
import ml.pkom.mcpitanlibarch.api.util.client.RenderUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class BaseHandledScreen extends SimpleHandledScreen {
    public BaseHandledScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    public abstract Identifier getTexture();

    @Override
    public void drawBackgroundOverride(DrawBackgroundArgs args) {
        //int x = (this.width - this.backgroundWidth) / 2;
        //int y = (this.height - this.backgroundHeight) / 2;

        RenderUtil.setShaderToPositionTexProgram();
        RenderUtil.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        callDrawTexture(args.drawObjectDM, getTexture(), x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void drawForegroundOverride(DrawForegroundArgs args) {
        super.drawForegroundOverride(args);

        //x = (this.width - this.backgroundWidth) / 2;
        //y = (this.height - this.backgroundHeight) / 2;
    }

    @Override
    public void renderOverride(RenderArgs args) {
        this.callRenderBackground(args);
        super.renderOverride(args);
        this.callDrawMouseoverTooltip(new DrawMouseoverTooltipArgs(args.drawObjectDM, args.mouseX, args.mouseY));
    }

}
