package net.pitan76.enhancedquarries.block.base;

import ml.pkom.mcpitanlibarch.api.block.CompatibleBlockSettings;
import ml.pkom.mcpitanlibarch.api.block.CompatibleMaterial;

public abstract class Pump extends BaseBlock {
    public static CompatibleBlockSettings defaultSettings = CompatibleBlockSettings
            .of(CompatibleMaterial.METAL)
            .requiresTool()
            .strength(2, 8);

    public Pump(CompatibleBlockSettings settings) {
        super(settings);
    }

    public Pump() {
        this(defaultSettings);
    }
}
