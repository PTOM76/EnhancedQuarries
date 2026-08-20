package net.pitan76.enhancedquarries.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.pitan76.enhancedquarries.EnhancedQuarriesClient;

public class EnhancedQuarriesClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EnhancedQuarriesClient.init();
    }
}
