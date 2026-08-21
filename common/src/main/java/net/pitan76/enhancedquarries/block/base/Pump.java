package net.pitan76.enhancedquarries.block.base;

import net.pitan76.enhancedquarries.EnhancedQuarries;
import net.pitan76.enhancedquarries.Items;
import net.pitan76.enhancedquarries.tile.base.PumpTile;
import net.pitan76.mcpitanlib.api.event.block.BlockUseEvent;
import net.pitan76.mcpitanlib.api.util.CompatActionResult;
import net.pitan76.mcpitanlib.api.block.CompatibleMaterial;
import net.pitan76.mcpitanlib.api.block.v2.BlockSettingsBuilder;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.midohra.block.entity.BlockEntityWrapper;

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

    @Override
    public CompatActionResult onRightClick(BlockUseEvent e) {
        if (e.getStackM().instanceOf(Items.WRENCH)) return e.pass();
        if (e.isClient()) return e.success();

        BlockEntityWrapper blockEntity = e.getBlockEntityWrapper();
        if (!blockEntity.instanceOf(PumpTile.class)) return e.pass();

        PumpTile pumpTile = blockEntity.getCompatBlockEntity(PumpTile.class);
        if (pumpTile.tryFillBucket(e.getPlayer(), e.getHand()))
            return e.consume();

        return e.pass();
    }
}
