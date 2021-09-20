package ml.pkom.enhancedquarries;

import ml.pkom.enhancedquarries.screen.FillerScreen;
import ml.pkom.enhancedquarries.screen.FillerWithChestScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class Screens {
    @Environment(EnvType.CLIENT)
    public static void init() {
        ScreenRegistry.register(ScreenHandlers.FILLER_SCREEN_HANDLER_TYPE, FillerScreen::new);
        ScreenRegistry.register(ScreenHandlers.FILLER_WITH_CHEST_SCREEN_HANDLER_TYPE, FillerWithChestScreen::new);
    }
}
