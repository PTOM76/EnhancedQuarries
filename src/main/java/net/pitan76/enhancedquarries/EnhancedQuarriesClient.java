package net.pitan76.enhancedquarries;

import net.fabricmc.api.ClientModInitializer;
import net.pitan76.enhancedquarries.client.BlockRenders;
import net.pitan76.enhancedquarries.client.Screens;
import net.pitan76.enhancedquarries.client.renderer.TileRenderers;
import net.pitan76.enhancedquarries.screen.EnergyGeneratorScreenHandler;
import net.pitan76.mcpitanlib.api.network.v2.ClientNetworking;

public class EnhancedQuarriesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenders.init();
        Screens.init();

        TileRenderers.init();

        ClientNetworking.registerReceiver(EnhancedQuarries._id("energy_generator_sync"), (e) -> {
            long energy = e.getCompatBuf().readLong();
            int burnTime = e.getCompatBuf().readInt();
            int maxBurnTime = e.getCompatBuf().readInt();
            if (e.getClientPlayer() == null) return;
            if (e.player.getCurrentScreenHandler() instanceof EnergyGeneratorScreenHandler) {
                EnergyGeneratorScreenHandler screenHandler = (EnergyGeneratorScreenHandler) e.player.getCurrentScreenHandler();
                screenHandler.energy = energy;
                screenHandler.burnTime = burnTime;
                screenHandler.maxBurnTime = maxBurnTime;
            }
        });
    }
}