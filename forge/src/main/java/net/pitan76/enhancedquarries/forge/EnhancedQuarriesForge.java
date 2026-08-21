package net.pitan76.enhancedquarries.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.pitan76.enhancedquarries.EnhancedQuarries;
import net.pitan76.enhancedquarries.EnhancedQuarriesClient;

@Mod(EnhancedQuarries.MOD_ID)
public class EnhancedQuarriesForge {
    public EnhancedQuarriesForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        new EnhancedQuarries();

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            EnhancedQuarriesClient.initScreens();
            modEventBus.addListener(EnhancedQuarriesForge::onClientSetup);
        });
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(EnhancedQuarriesClient::init);
    }
}
