package net.pitan76.enhancedquarries;

import net.fabricmc.api.ClientModInitializer;
import net.pitan76.enhancedquarries.client.renderer.TileRenderers;
import net.pitan76.enhancedquarries.screen.EnergyGeneratorScreenHandler;
import net.pitan76.mcpitanlib.api.network.ClientNetworking;
import net.pitan76.mcpitanlib.api.network.PacketByteUtil;

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