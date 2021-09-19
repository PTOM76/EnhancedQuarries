package ml.pkom.enhancedquarries;

import net.fabricmc.api.ClientModInitializer;

public class EnhancedQuarriesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenders.init();
        Screens.init();
    }
}