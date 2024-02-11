package ml.pkom.enhancedquarries;

import ml.pkom.enhancedquarries.client.renderer.TileRenderers;
import ml.pkom.enhancedquarries.screen.EnergyGeneratorScreenHandler;
import ml.pkom.mcpitanlibarch.api.network.ClientNetworking;
import ml.pkom.mcpitanlibarch.api.network.PacketByteUtil;
import net.fabricmc.api.ClientModInitializer;

import java.util.Objects;

public class EnhancedQuarriesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenders.init();
        Screens.init();

        TileRenderers.init();

        ClientNetworking.registerReceiver(EnhancedQuarries.id("energy_generator"), (client, p, buf) -> {
            long energy = PacketByteUtil.readLong(buf);
            long maxEnergy = PacketByteUtil.readLong(buf);
            if (p == null) return;
            if (p.currentScreenHandler instanceof EnergyGeneratorScreenHandler) {
                EnergyGeneratorScreenHandler screenHandler = (EnergyGeneratorScreenHandler) p.currentScreenHandler;
                screenHandler.energy = energy;
                screenHandler.maxEnergy = maxEnergy;
            }
        });
    }
}