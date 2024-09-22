package net.pitan76.enhancedquarries.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.pitan76.enhancedquarries.ScreenHandlers;
import net.pitan76.enhancedquarries.client.screen.*;
import net.pitan76.mcpitanlib.api.client.registry.CompatRegistryClient;

public class Screens {
    @Environment(EnvType.CLIENT)
    public static void init() {
        CompatRegistryClient.registerScreen(ScreenHandlers.FILLER_SCREEN_HANDLER_TYPE, FillerScreen::new);
        CompatRegistryClient.registerScreen(ScreenHandlers.FILLER_WITH_CHEST_SCREEN_HANDLER_TYPE, FillerWithChestScreen::new);
        CompatRegistryClient.registerScreen(ScreenHandlers.SCANNER_SCREEN_HANDLER_TYPE, ScannerScreen::new);
        CompatRegistryClient.registerScreen(ScreenHandlers.BUILDER_SCREEN_HANDLER_TYPE, BuilderScreen::new);
        CompatRegistryClient.registerScreen(ScreenHandlers.LIBRARY_SCREEN_HANDLER_TYPE, LibraryScreen::new);
        CompatRegistryClient.registerScreen(ScreenHandlers.ENERGY_GENERATOR_SCREEN_HANDLER_TYPE, EnergyGeneratorScreen::new);
        CompatRegistryClient.registerScreen(ScreenHandlers.DROP_REMOVAL_MODULE_SCREEN_HANDLER_TYPE, DropRemovalModuleScreen::new);
    }
}
