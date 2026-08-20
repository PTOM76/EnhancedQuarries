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
    public static ScreenHandlerType<DropRemovalModuleScreenHandler> DROP_REMOVAL_MODULE_SCREEN_HANDLER_TYPE = new SimpleScreenHandlerTypeBuilder<>(DropRemovalModuleScreenHandler::new).build();

    public static void init() {
        registry.registerScreenHandlerType(EnhancedQuarries._id("filler_menu"), () -> FILLER_SCREEN_HANDLER_TYPE);
        registry.registerScreenHandlerType(EnhancedQuarries._id("filler_with_chest_menu"), () -> FILLER_WITH_CHEST_SCREEN_HANDLER_TYPE);
        registry.registerScreenHandlerType(EnhancedQuarries._id("scanner_menu"), () -> SCANNER_SCREEN_HANDLER_TYPE);
        registry.registerScreenHandlerType(EnhancedQuarries._id("builder_menu"), () -> BUILDER_SCREEN_HANDLER_TYPE);
        registry.registerScreenHandlerType(EnhancedQuarries._id("library_menu"), () -> LIBRARY_SCREEN_HANDLER_TYPE);
        registry.registerScreenHandlerType(EnhancedQuarries._id("energy_generator_menu"), () -> ENERGY_GENERATOR_SCREEN_HANDLER_TYPE);
        registry.registerScreenHandlerType(EnhancedQuarries._id("dropped_item_removal_module_edit_menu"), () -> DROP_REMOVAL_MODULE_SCREEN_HANDLER_TYPE);
    }
}
