package net.pitan76.enhancedquarries.block.base;

import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.block.CompatibleMaterial;

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
