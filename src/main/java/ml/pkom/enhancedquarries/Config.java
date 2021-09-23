package ml.pkom.enhancedquarries;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import ml.pkom.enhancedquarries.item.fillermodule.HorizontalLayerModule;
import ml.pkom.enhancedquarries.item.fillermodule.VerticalLayerModule;
import ml.pkom.enhancedquarries.tile.base.FillerTile;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

public class Config implements ModMenuApi {

    public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setTitle(new TranslatableText("title.enhanced_quarries.config"))
                    .setParentScreen(parent);
            builder.setSavingRunnable(() -> {
                Configs.configDir.mkdirs();
                Configs.yamlConfig.set("torch_interval", FillerTile.moduleInterval);
                Configs.yamlConfig.set("vertical_layer_interval", VerticalLayerModule.interval);
                Configs.yamlConfig.set("horizontal_layer_interval", HorizontalLayerModule.interval);
                Configs.yamlConfig.save(Configs.configFile, true);
            });
            ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("category.enhanced_quarries.general"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();
            general.addEntry(entryBuilder.startIntField(new TranslatableText("option.enhanced_quarries.torch_interval"), FillerTile.moduleInterval)
                    .setDefaultValue(6)
                    .setSaveConsumer(newValue -> FillerTile.moduleInterval = newValue)
                    .build());
            general.addEntry(entryBuilder.startIntField(new TranslatableText("option.enhanced_quarries.vertical_layer_interval"), VerticalLayerModule.interval)
                    .setDefaultValue(6)
                    .setSaveConsumer(newValue -> VerticalLayerModule.interval = newValue)
                    .build());
            general.addEntry(entryBuilder.startIntField(new TranslatableText("option.enhanced_quarries.horizontal_layer_interval"), HorizontalLayerModule.interval)
                    .setDefaultValue(6)
                    .setSaveConsumer(newValue -> HorizontalLayerModule.interval = newValue)
                    .build());
            return builder.build();
        };
    }
}
