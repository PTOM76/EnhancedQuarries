package net.pitan76.enhancedquarries;

import net.pitan76.enhancedquarries.registry.ModuleRegistry;

public class FillerModules {
    public static void init() {
        ModuleRegistry.register(EnhancedQuarries._id("filler_all_fill"), Items.fillerALL_FILL);
        ModuleRegistry.register(EnhancedQuarries._id("filler_all_delete"), Items.fillerALL_DELETE);
        ModuleRegistry.register(EnhancedQuarries._id("filler_all_remove"), Items.fillerALL_REMOVE);
        ModuleRegistry.register(EnhancedQuarries._id("filler_leveling"), Items.fillerLEVELING);
        ModuleRegistry.register(EnhancedQuarries._id("filler_create_box"), Items.fillerBOX);
        ModuleRegistry.register(EnhancedQuarries._id("filler_create_wall"), Items.fillerWALL);
        ModuleRegistry.register(EnhancedQuarries._id("filler_place_torch"), Items.fillerTORCH);
        ModuleRegistry.register(EnhancedQuarries._id("vertical_layer"), Items.fillerVERTICAL_LAYER);
        ModuleRegistry.register(EnhancedQuarries._id("horizontal_layer"), Items.fillerHORIZONTAL_LAYER);
        ModuleRegistry.register(EnhancedQuarries._id("tower"), Items.fillerTOWER);
        ModuleRegistry.register(EnhancedQuarries._id("delete_fluid"), Items.fillerDELETE_FLUID);
        ModuleRegistry.register(EnhancedQuarries._id("floor_replace"), Items.fillerFLOOR_REPLACE);
        ModuleRegistry.register(EnhancedQuarries._id("stairs"), Items.fillerSTAIRS);
        ModuleRegistry.register(EnhancedQuarries._id("pyramid"), Items.fillerPYRAMID);
        ModuleRegistry.register(EnhancedQuarries._id("cut_stairs"), Items.fillerCUT_STAIRS);
        ModuleRegistry.register(EnhancedQuarries._id("cut_pyramid"), Items.fillerCUT_PYRAMID);
    }
}
