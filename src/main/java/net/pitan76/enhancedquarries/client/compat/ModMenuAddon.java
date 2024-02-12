package net.pitan76.enhancedquarries.client.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;

public class ModMenuAddon implements ModMenuApi {

    public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
        if (FabricLoader.getInstance().isModLoaded("cloth-config") || FabricLoader.getInstance().isModLoaded("cloth_config") || FabricLoader.getInstance().isModLoaded("cloth-config2"))
            return ClothConfig::create;
        else
            return null;
    }
}
