package net.pitan76.enhancedquarries;

import net.minecraft.screen.ScreenHandlerType;
import net.pitan76.enhancedquarries.screen.*;
import net.pitan76.mcpitanlib.api.gui.ExtendedScreenHandlerTypeBuilder;
import net.pitan76.mcpitanlib.api.gui.SimpleScreenHandlerTypeBuilder;

import static net.pitan76.enhancedquarries.EnhancedQuarries.registry;

public class ScreenHandlers {
    public static ScreenHandlerType<FillerScreenHandler> FILLER_SCREEN_HANDLER_TYPE = new SimpleScreenHandlerTypeBuilder<>(FillerScreenHandler::new).build();
    public static ScreenHandlerType<FillerWithChestScreenHandler> FILLER_WITH_CHEST_SCREEN_HANDLER_TYPE = new SimpleScreenHandlerTypeBuilder<>(FillerWithChestScreenHandler::new).build();
    public static ScreenHandlerType<ScannerScreenHandler> SCANNER_SCREEN_HANDLER_TYPE = new SimpleScreenHandlerTypeBuilder<>(ScannerScreenHandler::new).build();
    public static ScreenHandlerType<BuilderScreenHandler> BUILDER_SCREEN_HANDLER_TYPE = new SimpleScreenHandlerTypeBuilder<>(BuilderScreenHandler::new).build();
    public static ScreenHandlerType<LibraryScreenHandler> LIBRARY_SCREEN_HANDLER_TYPE = new SimpleScreenHandlerTypeBuilder<>(LibraryScreenHandler::new).build();
    public static ScreenHandlerType<EnergyGeneratorScreenHandler> ENERGY_GENERATOR_SCREEN_HANDLER_TYPE = new ExtendedScreenHandlerTypeBuilder<>(EnergyGeneratorScreenHandler::new).build();
    public static ScreenHandlerType<DroppedItemRemovalModuleEditScreenHandler> DROPPED_ITEM_REMOVAL_MODULE_EDIT_SCREEN_HANDLER_TYPE = new SimpleScreenHandlerTypeBuilder<>(DroppedItemRemovalModuleEditScreenHandler::new).build();

    public static void init() {
        registry.registerScreenHandlerType(EnhancedQuarries.id("filler"), () -> FILLER_SCREEN_HANDLER_TYPE);
        registry.registerScreenHandlerType(EnhancedQuarries.id("filler_with_chest"), () -> FILLER_WITH_CHEST_SCREEN_HANDLER_TYPE);
        registry.registerScreenHandlerType(EnhancedQuarries.id("scanner"), () -> SCANNER_SCREEN_HANDLER_TYPE);
        registry.registerScreenHandlerType(EnhancedQuarries.id("builder"), () -> BUILDER_SCREEN_HANDLER_TYPE);
        registry.registerScreenHandlerType(EnhancedQuarries.id("library"), () -> LIBRARY_SCREEN_HANDLER_TYPE);
        registry.registerScreenHandlerType(EnhancedQuarries.id("energy_generator"), () -> ENERGY_GENERATOR_SCREEN_HANDLER_TYPE);
        registry.registerScreenHandlerType(EnhancedQuarries.id("dropped_item_removal_module_edit"), () -> DROPPED_ITEM_REMOVAL_MODULE_EDIT_SCREEN_HANDLER_TYPE);
    }
}
