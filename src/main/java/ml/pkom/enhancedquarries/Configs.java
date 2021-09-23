package ml.pkom.enhancedquarries;

import ml.pkom.enhancedquarries.easyapi.config.YamlConfig;
import ml.pkom.enhancedquarries.item.fillermodule.HorizontalLayerModule;
import ml.pkom.enhancedquarries.item.fillermodule.VerticalLayerModule;
import ml.pkom.enhancedquarries.tile.base.FillerTile;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public class Configs {
    public static File configDir = new File(FabricLoader.getInstance().getConfigDir().toFile(), "enhanced_quarries");
    public static File configFile = new File(configDir, "config.yml");
    public static YamlConfig yamlConfig = new YamlConfig(configFile);

    public static void init() {
        configDir.mkdirs();

        if (yamlConfig.configMap.containsKey("module_interval")) {
            FillerTile.moduleInterval = yamlConfig.getInt("module_interval");
        } else {
            yamlConfig.set("module_interval", FillerTile.moduleInterval);
        }

        if (yamlConfig.configMap.containsKey("vertical_layer_interval")) {
            VerticalLayerModule.interval = yamlConfig.getInt("vertical_layer_interval");
        } else {
            yamlConfig.set("vertical_layer_interval", VerticalLayerModule.interval);
        }

        if (yamlConfig.configMap.containsKey("horizontal_layer_interval")) {
            HorizontalLayerModule.interval = yamlConfig.getInt("horizontal_layer_interval");
        } else {
            yamlConfig.set("horizontal_layer_interval", HorizontalLayerModule.interval);
        }
        yamlConfig.save(configFile, true);

    }
}
