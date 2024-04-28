package net.pitan76.enhancedquarries.client.compat;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.pitan76.enhancedquarries.Config;
import net.pitan76.enhancedquarries.item.fillermodule.HorizontalLayerModule;
import net.pitan76.enhancedquarries.item.fillermodule.TorchModule;
import net.pitan76.enhancedquarries.item.fillermodule.VerticalLayerModule;
import net.pitan76.mcpitanlib.api.util.TextUtil;

public class ClothConfig {

    public static Screen create(Screen screen) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setTitle(TextUtil.translatable("title.enhanced_quarries.config"))
                .setParentScreen(screen);
        builder.setSavingRunnable(() -> {
            if (!Config.configDir.exists() || !Config.configDir.isDirectory())
                Config.configDir.mkdirs();
            Config.config.set("torch_interval", TorchModule.interval);
            Config.config.set("vertical_layer_interval", VerticalLayerModule.interval);
            Config.config.set("horizontal_layer_interval", HorizontalLayerModule.interval);
            Config.config.save(Config.configFile, true);
        });
        ConfigCategory general = builder.getOrCreateCategory(TextUtil.translatable("category.enhanced_quarries.general"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        general.addEntry(entryBuilder.startIntField(TextUtil.translatable("option.enhanced_quarries.torch_interval"), TorchModule.interval)
                .setDefaultValue(6)
                .setSaveConsumer(newValue -> TorchModule.interval = newValue)
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
