package net.pitan76.enhancedquarries;

import net.pitan76.enhancedquarries.client.renderer.TileRenderers;
import net.pitan76.enhancedquarries.screen.EnergyGeneratorScreenHandler;
import ml.pkom.mcpitanlibarch.api.network.ClientNetworking;
import ml.pkom.mcpitanlibarch.api.network.PacketByteUtil;
import net.fabricmc.api.ClientModInitializer;

public class EnhancedQuarriesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenders.init();
        Screens.init();

        TileRenderers.init();

        ClientNetworking.registerReceiver(EnhancedQuarries.id("energy_generator"), (client, p, buf) -> {
            long energy = PacketByteUtil.readLong(buf);
            if (p == null) return;
            if (p.currentScreenHandler instanceof EnergyGeneratorScreenHandler) {
                EnergyGeneratorScreenHandler screenHandler = (EnergyGeneratorScreenHandler) p.currentScreenHandler;
                screenHandler.energy = energy;
            }
        });
    }
}