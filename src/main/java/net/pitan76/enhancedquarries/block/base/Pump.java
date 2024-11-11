package net.pitan76.enhancedquarries.block.base;

import net.pitan76.enhancedquarries.EnhancedQuarries;
import net.pitan76.mcpitanlib.api.block.CompatibleMaterial;
import net.pitan76.mcpitanlib.api.block.v2.BlockSettingsBuilder;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;

public abstract class Pump extends BaseBlock {
    public static BlockSettingsBuilder defaultSettings = new BlockSettingsBuilder()
        .material(CompatibleMaterial.METAL)
        .requiresTool()
        .strength(2, 8);

    public Pump(CompatibleBlockSettings settings) {
        super(settings);
    }

    public Pump(CompatIdentifier id) {
        this(defaultSettings.build(id));
    }

    public Pump() {
        this(EnhancedQuarries._id("normal_pump"));
    }
}
