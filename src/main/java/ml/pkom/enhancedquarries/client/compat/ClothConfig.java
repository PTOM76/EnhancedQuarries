package ml.pkom.enhancedquarries.client.compat;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import ml.pkom.enhancedquarries.item.fillermodule.HorizontalLayerModule;
import ml.pkom.enhancedquarries.item.fillermodule.VerticalLayerModule;
import ml.pkom.enhancedquarries.tile.base.FillerTile;
import ml.pkom.mcpitanlibarch.api.util.TextUtil;
import net.minecraft.client.gui.screen.Screen;

import static ml.pkom.enhancedquarries.Config.*;

public class ClothConfig {

    public static Screen create(Screen screen) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setTitle(TextUtil.translatable("title.enhanced_quarries.config"))
                .setParentScreen(screen);
        builder.setSavingRunnable(() -> {
            if (!configDir.exists() || !configDir.isDirectory())
                configDir.mkdirs();
            config.set("torch_interval", FillerTile.moduleInterval);
            config.set("vertical_layer_interval", VerticalLayerModule.interval);
            config.set("horizontal_layer_interval", HorizontalLayerModule.interval);
            config.save(configFile, true);
        });
        ConfigCategory general = builder.getOrCreateCategory(TextUtil.translatable("category.enhanced_quarries.general"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        general.addEntry(entryBuilder.startIntField(TextUtil.translatable("option.enhanced_quarries.torch_interval"), FillerTile.moduleInterval)
                .setDefaultValue(6)
                .setSaveConsumer(newValue -> FillerTile.moduleInterval = newValue)
                .build());
        general.addEntry(entryBuilder.startIntField(TextUtil.translatable("option.enhanced_quarries.vertical_layer_interval"), VerticalLayerModule.interval)
                .setDefaultValue(6)
                .setSaveConsumer(newValue -> VerticalLayerModule.interval = newValue)
                .build());
        general.addEntry(entryBuilder.startIntField(TextUtil.translatable("option.enhanced_quarries.horizontal_layer_interval"), HorizontalLayerModule.interval)
                .setDefaultValue(6)
                .setSaveConsumer(newValue -> HorizontalLayerModule.interval = newValue)
                .build());
        return builder.build();
    }
}
