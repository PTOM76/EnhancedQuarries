package ml.pkom.enhancedquarries;

import ml.pkom.enhancedquarries.screen.FillerScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

public class ScreenHandlers {
    public static ScreenHandlerType<FillerScreenHandler> FILLER_SCREEN_HANDLER_TYPE;

    public static void init() {
        FILLER_SCREEN_HANDLER_TYPE = ScreenHandlerRegistry.registerSimple(EnhancedQuarries.id("filler"), FillerScreenHandler::new);
    }
}
