package ml.pkom.enhancedquarries;

import ml.pkom.enhancedquarries.client.screen.*;
import ml.pkom.mcpitanlibarch.api.client.registry.ArchRegistryClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class Screens {
    @Environment(EnvType.CLIENT)
    public static void init() {
        ArchRegistryClient.registerScreen(ScreenHandlers.FILLER_SCREEN_HANDLER_TYPE, FillerScreen::new);
        ArchRegistryClient.registerScreen(ScreenHandlers.FILLER_WITH_CHEST_SCREEN_HANDLER_TYPE, FillerWithChestScreen::new);
        ArchRegistryClient.registerScreen(ScreenHandlers.SCANNER_SCREEN_HANDLER_TYPE, ScannerScreen::new);
        ArchRegistryClient.registerScreen(ScreenHandlers.BUILDER_SCREEN_HANDLER_TYPE, BuilderScreen::new);
        ArchRegistryClient.registerScreen(ScreenHandlers.LIBRARY_SCREEN_HANDLER_TYPE, LibraryScreen::new);
        ArchRegistryClient.registerScreen(ScreenHandlers.ENERGY_GENERATOR_SCREEN_HANDLER_TYPE, EnergyGeneratorScreen::new);
    }
}
