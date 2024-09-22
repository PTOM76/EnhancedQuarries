package net.pitan76.enhancedquarries.client.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.pitan76.enhancedquarries.client.screen.base.BaseHandledScreen;
import net.pitan76.enhancedquarries.screen.DropRemovalModuleScreenHandler;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;

public class DropRemovalModuleScreen extends BaseHandledScreen {
    public DropRemovalModuleScreenHandler screenHandler;

    public DropRemovalModuleScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.screenHandler = (DropRemovalModuleScreenHandler) handler;
    }

    @Override
    public CompatIdentifier getCompatTexture() {
        return CompatIdentifier.of("textures/gui/container/hopper.png");
    }
}
