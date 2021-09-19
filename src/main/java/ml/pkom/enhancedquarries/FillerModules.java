package ml.pkom.enhancedquarries;

import ml.pkom.enhancedquarries.registry.Registry;

public class FillerModules {
    public static void init() {
        Registry.register(EnhancedQuarries.id("vertical_layer"), Items.fillerVERTICAL_LAYER);
        Registry.register(EnhancedQuarries.id("horizontal_layer"), Items.fillerHORIZONTAL_LAYER);
        Registry.register(EnhancedQuarries.id("tower"), Items.fillerTOWER);
        Registry.register(EnhancedQuarries.id("delete_fluid"), Items.fillerDELETE_FLUID);
        Registry.register(EnhancedQuarries.id("floor_replace"), Items.fillerFLOOR_REPLACE);
    }
}
