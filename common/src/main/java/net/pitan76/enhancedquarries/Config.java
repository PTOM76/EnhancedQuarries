package net.pitan76.enhancedquarries;

import net.pitan76.easyapi.FileControl;
import net.pitan76.easyapi.config.YamlConfig;
import net.pitan76.enhancedquarries.item.fillermodule.HorizontalLayerModule;
import net.pitan76.enhancedquarries.item.fillermodule.TorchModule;
import net.pitan76.enhancedquarries.item.fillermodule.VerticalLayerModule;
import net.pitan76.enhancedquarries.tile.ExperimentalQuarryTypeATile;
import net.pitan76.mcpitanlib.api.util.PlatformUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Config {
    public static File configDir = new File(PlatformUtil.getConfigFolderAsFile(), "enhanced_quarries");
    public static File configFile = new File(configDir, "config.yml");

    public static YamlConfig config = new YamlConfig(configFile);

    public static boolean client_marker_rendering_range_box = true;
    public static boolean client_builder_rendering_range_box = true;
    public static boolean client_scanner_rendering_range_box = true;
    public static boolean client_filler_rendering_range_box = true;

    // 試験的クァーリー Type-A の最適化のデフォルト値 (ブロックごとにShift+右クリックで変更できる)
    public static Map<String, Boolean> experimental_quarry_optimizations = new HashMap<>();

    // 1回の走査で見るブロック数の上限
    public static int experimental_quarry_scan_budget = 4096;

    // 掘り終わったあと、次に範囲全体を確認するまでの間隔 (tick)
    public static int experimental_quarry_rescan_interval = 200;

    public static boolean getExperimentalQuarryOptimization(String id) {
        Boolean value = experimental_quarry_optimizations.get(id);
        return value == null || value;
    }

    public static boolean initialized = false;

    public static void init() {
        if (initialized) return;
        initialized = true;

        if (!configDir.exists() || !configDir.isDirectory())
            configDir.mkdirs();

        TorchModule.interval = config.getIntOrDefault("module_interval", 6);
        VerticalLayerModule.interval = config.getIntOrDefault("vertical_layer_interval", 6);
        HorizontalLayerModule.interval = config.getIntOrDefault("horizontal_layer_interval", 6);
        client_marker_rendering_range_box = config.getBooleanOrDefault("client_marker_rendering_range_box", true);
        client_builder_rendering_range_box = config.getBooleanOrDefault("client_builder_rendering_range_box", true);
        client_scanner_rendering_range_box = config.getBooleanOrDefault("client_scanner_rendering_range_box", true);
        client_filler_rendering_range_box = config.getBooleanOrDefault("client_filler_rendering_range_box", true);

        for (ExperimentalQuarryTypeATile.Optimization optimization : ExperimentalQuarryTypeATile.Optimization.VALUES) {
            String key = "experimental_quarry_optimization_" + optimization.getId();
            experimental_quarry_optimizations.put(optimization.getId(), config.getBooleanOrDefault(key, true));
        }
        experimental_quarry_scan_budget = config.getIntOrDefault("experimental_quarry_scan_budget", 4096);
        experimental_quarry_rescan_interval = config.getIntOrDefault("experimental_quarry_rescan_interval", 200);

        if (!config.configMap.containsKey("reborn_energy_conversion_rate"))
            config.setDouble("reborn_energy_conversion_rate", 1.0);

        if (!config.configMap.containsKey("fe_conversion_rate"))
            config.setDouble("fe_conversion_rate", 1.0);

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
