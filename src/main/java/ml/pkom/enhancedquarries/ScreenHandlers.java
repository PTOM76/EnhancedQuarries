package ml.pkom.enhancedquarries;

import ml.pkom.enhancedquarries.screen.FillerScreenHandler;
import ml.pkom.enhancedquarries.screen.FillerWithChestScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

public class ScreenHandlers {
    public static ScreenHandlerType<FillerScreenHandler> FILLER_SCREEN_HANDLER_TYPE;
    public static ScreenHandlerType<FillerWithChestScreenHandler> FILLER_WITH_CHEST_SCREEN_HANDLER_TYPE;

    public static void init() {
        FILLER_SCREEN_HANDLER_TYPE = ScreenHandlerRegistry.registerSimple(EnhancedQuarries.id("filler"), FillerScreenHandler::new);
        FILLER_WITH_CHEST_SCREEN_HANDLER_TYPE = ScreenHandlerRegistry.registerSimple(EnhancedQuarries.id("filler_with_chest"), FillerWithChestScreenHandler::new);
    }
}
