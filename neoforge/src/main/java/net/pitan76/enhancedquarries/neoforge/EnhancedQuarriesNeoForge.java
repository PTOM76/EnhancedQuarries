package net.pitan76.enhancedquarries.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.pitan76.enhancedquarries.EnhancedQuarries;
import net.pitan76.enhancedquarries.EnhancedQuarriesClient;
import net.pitan76.enhancedquarries.neoforge.compat.FEEnergyRegister;

@Mod(EnhancedQuarries.MOD_ID)
public class EnhancedQuarriesNeoForge {

    public EnhancedQuarriesNeoForge(ModContainer modContainer, IEventBus modEventBus, Dist dist) {
        modEventBus.addListener(FEEnergyRegister::register);

        new EnhancedQuarries();

        if (dist.isClient()) {
            EnhancedQuarriesClient.initScreens();
            modEventBus.addListener(EnhancedQuarriesNeoForge::onClientSetup);
        }
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(EnhancedQuarriesClient::init);
    }
}
