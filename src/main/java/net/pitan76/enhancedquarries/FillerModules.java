package net.pitan76.enhancedquarries;

import net.pitan76.enhancedquarries.registry.Registry;

import static net.pitan76.enhancedquarries.EnhancedQuarries.registry;

public class FillerModules {
    public static void init() {
        Registry.register(EnhancedQuarries.id("filler_all_fill"), Items.fillerALL_FILL);
        Registry.register(EnhancedQuarries.id("filler_all_delete"), Items.fillerALL_DELETE);
        Registry.register(EnhancedQuarries.id("filler_all_remove"), Items.fillerALL_REMOVE);
        Registry.register(EnhancedQuarries.id("filler_leveling"), Items.fillerLEVELING);
        Registry.register(EnhancedQuarries.id("filler_create_box"), Items.fillerBOX);
        Registry.register(EnhancedQuarries.id("filler_create_wall"), Items.fillerWALL);
        Registry.register(EnhancedQuarries.id("filler_place_torch"), Items.fillerTORCH);
        Registry.register(EnhancedQuarries.id("vertical_layer"), Items.fillerVERTICAL_LAYER);
        Registry.register(EnhancedQuarries.id("horizontal_layer"), Items.fillerHORIZONTAL_LAYER);
        Registry.register(EnhancedQuarries.id("tower"), Items.fillerTOWER);
        Registry.register(EnhancedQuarries.id("delete_fluid"), Items.fillerDELETE_FLUID);
        Registry.register(EnhancedQuarries.id("floor_replace"), Items.fillerFLOOR_REPLACE);
        Registry.register(EnhancedQuarries.id("stairs"), Items.fillerSTAIRS);
        Registry.register(EnhancedQuarries.id("pyramid"), Items.fillerPYRAMID);
        Registry.register(EnhancedQuarries.id("cut_stairs"), Items.fillerCUT_STAIRS);
        Registry.register(EnhancedQuarries.id("cut_pyramid"), Items.fillerCUT_PYRAMID);
    }
}
