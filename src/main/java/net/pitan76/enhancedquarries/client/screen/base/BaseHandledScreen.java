package net.pitan76.enhancedquarries.client.screen.base;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.pitan76.mcpitanlib.api.client.CompatInventoryScreen;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;

public abstract class BaseHandledScreen extends CompatInventoryScreen {
    public BaseHandledScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    public abstract CompatIdentifier getCompatTexture();
}
