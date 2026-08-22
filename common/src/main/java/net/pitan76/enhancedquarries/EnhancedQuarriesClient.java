package net.pitan76.enhancedquarries;

import net.pitan76.enhancedquarries.client.BlockRenders;
import net.pitan76.enhancedquarries.client.Screens;
import net.pitan76.enhancedquarries.client.renderer.TileRenderers;
import net.pitan76.enhancedquarries.screen.EnergyGeneratorScreenHandler;
import net.pitan76.enhancedquarries.screen.LibraryScreenHandler;
import net.pitan76.mcpitanlib.api.network.v2.ClientNetworking;
import net.pitan76.mcpitanlib.midohra.network.CompatPacketByteBuf;

import java.util.ArrayList;
import java.util.List;

public class EnhancedQuarriesClient {

    private static List<String> readNames(CompatPacketByteBuf buf) {
        List<String> names = new ArrayList<>();
        int size = buf.readVarInt();
        for (int i = 0; i < size; i++)
            names.add(buf.readString());

        return names;
    }

    public static void initScreens() {
        Screens.init();
    }

    public static void initRenderers() {
        TileRenderers.init();
    }

    public static void init() {
        BlockRenders.init();

        ClientNetworking.registerReceiver(LibraryScreenHandler.LIST_PACKET_ID, (e) -> {
            CompatPacketByteBuf buf = e.getCompatBuf();

            List<String> blueprints = readNames(buf);
            List<String> templates = readNames(buf);

            if (e.getClientPlayer() == null) return;
            if (!(e.player.getCurrentScreenHandler() instanceof LibraryScreenHandler)) return;

            LibraryScreenHandler screenHandler = (LibraryScreenHandler) e.player.getCurrentScreenHandler();
            screenHandler.blueprintNames = blueprints;
            screenHandler.templateNames = templates;
        });

        ClientNetworking.registerReceiver(EnhancedQuarries._id("energy_generator_sync"), (e) -> {
            CompatPacketByteBuf buf = e.getCompatBuf();
            if (!buf.isReadable(EnergyGeneratorScreenHandler.EXTRA_DATA_SIZE)) return;

            long energy = buf.readLong();
            long maxEnergy = buf.readLong();
            int burnTime = buf.readInt();
            int maxBurnTime = buf.readInt();

            if (e.getClientPlayer() == null) return;
            if (e.player.getCurrentScreenHandler() instanceof EnergyGeneratorScreenHandler) {
                EnergyGeneratorScreenHandler screenHandler = (EnergyGeneratorScreenHandler) e.player.getCurrentScreenHandler();
                screenHandler.energy = energy;
                screenHandler.maxEnergy = maxEnergy;
                screenHandler.burnTime = burnTime;
                screenHandler.maxBurnTime = maxBurnTime;
            }
        });
    }
}
