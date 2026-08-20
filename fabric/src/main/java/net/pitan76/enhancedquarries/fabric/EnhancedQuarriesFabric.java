package net.pitan76.enhancedquarries.fabric;

import net.fabricmc.api.ModInitializer;
import net.pitan76.enhancedquarries.EnhancedQuarries;

public class EnhancedQuarriesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        new EnhancedQuarries();
    }
}
