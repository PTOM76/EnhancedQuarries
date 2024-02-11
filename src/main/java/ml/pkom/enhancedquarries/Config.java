package ml.pkom.enhancedquarries;

import ml.pkom.easyapi.FileControl;
import ml.pkom.easyapi.config.YamlConfig;
import ml.pkom.enhancedquarries.item.fillermodule.HorizontalLayerModule;
import ml.pkom.enhancedquarries.item.fillermodule.VerticalLayerModule;
import ml.pkom.enhancedquarries.tile.base.FillerTile;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public class Config {
    public static File configDir = new File(FabricLoader.getInstance().getConfigDir().toFile(), "enhanced_quarries");
    public static File configFile = new File(configDir, "config.yml");

    public static YamlConfig config = new YamlConfig(configFile);

    public static boolean initialized = false;
    public static void init() {
        if (initialized) return;
        initialized = true;

        if (!configDir.exists() || !configDir.isDirectory())
            configDir.mkdirs();

        FillerTile.moduleInterval = config.getIntOrDefault("module_interval", 6);

        VerticalLayerModule.interval = config.getIntOrDefault("vertical_layer_interval", 6);

        HorizontalLayerModule.interval = config.getIntOrDefault("horizontal_layer_interval", 6);

        if (!config.configMap.containsKey("reborn_energy_conversion_rate"))
            config.setDouble("reborn_energy_conversion_rate", 1.0);

        save();
    }

    public static boolean reload() {
        if (FileControl.fileExists(getConfigFile())) {
            config.load(getConfigFile());
            return true;
        }
        return false;
    }

    public static File getConfigFile() {
        return configFile;
    }

    public static void setConfigDir(File configDir) {
        Config.configDir = configDir;
    }

    public static File getConfigDir() {
        return configDir;
    }

    public static void save() {
        config.save(getConfigFile(), true);
    }
}
