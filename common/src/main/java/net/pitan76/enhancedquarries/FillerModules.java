package net.pitan76.enhancedquarries;

import net.pitan76.enhancedquarries.registry.ModuleRegistry;

public class FillerModules {
    public static void init() {
        ModuleRegistry.register(EnhancedQuarries._id("filler_all_fill"), Items.fillerALL_FILL.getICompat());
        ModuleRegistry.register(EnhancedQuarries._id("filler_all_delete"), Items.fillerALL_DELETE.getICompat());
        ModuleRegistry.register(EnhancedQuarries._id("filler_all_remove"), Items.fillerALL_REMOVE.getICompat());
        ModuleRegistry.register(EnhancedQuarries._id("filler_leveling"), Items.fillerLEVELING.getICompat());
        ModuleRegistry.register(EnhancedQuarries._id("filler_create_box"), Items.fillerBOX.getICompat());
        ModuleRegistry.register(EnhancedQuarries._id("filler_create_wall"), Items.fillerWALL.getICompat());
        ModuleRegistry.register(EnhancedQuarries._id("filler_place_torch"), Items.fillerTORCH.getICompat());
        ModuleRegistry.register(EnhancedQuarries._id("vertical_layer"), Items.fillerVERTICAL_LAYER.getICompat());
        ModuleRegistry.register(EnhancedQuarries._id("horizontal_layer"), Items.fillerHORIZONTAL_LAYER.getICompat());
        ModuleRegistry.register(EnhancedQuarries._id("tower"), Items.fillerTOWER.getICompat());
        ModuleRegistry.register(EnhancedQuarries._id("delete_fluid"), Items.fillerDELETE_FLUID.getICompat());
        ModuleRegistry.register(EnhancedQuarries._id("floor_replace"), Items.fillerFLOOR_REPLACE.getICompat());
        ModuleRegistry.register(EnhancedQuarries._id("stairs"), Items.fillerSTAIRS.getICompat());
        ModuleRegistry.register(EnhancedQuarries._id("pyramid"), Items.fillerPYRAMID.getICompat());
        ModuleRegistry.register(EnhancedQuarries._id("cut_stairs"), Items.fillerCUT_STAIRS.getICompat());
        ModuleRegistry.register(EnhancedQuarries._id("cut_pyramid"), Items.fillerCUT_PYRAMID.getICompat());
    }
}
