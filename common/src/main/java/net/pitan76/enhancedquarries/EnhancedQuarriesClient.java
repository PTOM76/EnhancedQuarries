package net.pitan76.enhancedquarries;

import net.minecraft.network.PacketByteBuf;
import net.pitan76.enhancedquarries.client.BlockRenders;
import net.pitan76.enhancedquarries.client.Screens;
import net.pitan76.enhancedquarries.client.renderer.TileRenderers;
import net.pitan76.enhancedquarries.screen.EnergyGeneratorScreenHandler;
import net.pitan76.enhancedquarries.screen.LibraryScreenHandler;
import net.pitan76.mcpitanlib.api.network.PacketByteUtil;
import net.pitan76.mcpitanlib.api.network.v2.ClientNetworking;

import java.util.ArrayList;
import java.util.List;

public class EnhancedQuarriesClient {

    private static List<String> readNames(PacketByteBuf buf) {
        List<String> names = new ArrayList<>();
        int size = PacketByteUtil.readVarInt(buf);
        for (int i = 0; i < size; i++)
            names.add(PacketByteUtil.readString(buf));

        return names;
    }

    public static void initScreens() {
        Screens.init();
    }

    public static void init() {
        BlockRenders.init();

        TileRenderers.init();

        ClientNetworking.registerReceiver(LibraryScreenHandler.LIST_PACKET_ID, (e) -> {
            java.util.List<String> blueprints = readNames(e.getBuf());
            java.util.List<String> templates = readNames(e.getBuf());

            if (e.getClientPlayer() == null) return;
            if (!(e.player.getCurrentScreenHandler() instanceof LibraryScreenHandler)) return;

            LibraryScreenHandler screenHandler = (LibraryScreenHandler) e.player.getCurrentScreenHandler();
            screenHandler.blueprintNames = blueprints;
            screenHandler.templateNames = templates;
        });

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
