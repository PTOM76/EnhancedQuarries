package ml.pkom.enhancedquarries;

import ml.pkom.enhancedquarries.client.renderer.TileRenderers;
import net.fabricmc.api.ClientModInitializer;

public class EnhancedQuarriesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenders.init();
        Screens.init();

        TileRenderers.init();
    }
}