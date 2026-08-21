package net.pitan76.enhancedquarries.block.base;

import net.pitan76.enhancedquarries.tile.base.LibraryTile;
import net.pitan76.mcpitanlib.api.block.CompatibleMaterial;
import net.pitan76.mcpitanlib.api.block.v2.BlockSettingsBuilder;
import net.pitan76.mcpitanlib.api.block.v2.CompatBlock;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.event.block.ItemScattererUtil;
import net.pitan76.mcpitanlib.api.event.block.StateReplacedEvent;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.midohra.block.entity.BlockEntityWrapper;

public abstract class Library extends CompatBlock {

    public static BlockSettingsBuilder defaultSettings = new BlockSettingsBuilder()
            .material(CompatibleMaterial.METAL)
            .requiresTool()
            .strength(2, 8);

    public Library(CompatibleBlockSettings settings) {
        super(settings);
    }

    public Library(CompatIdentifier id) {
        this(defaultSettings.build(id));
    }

    @Override
    public void onStateReplaced(StateReplacedEvent e) {
        if (!e.isSameState()) {
            BlockEntityWrapper blockEntity = e.getBlockEntityWrapper();
            if (blockEntity.instanceOf(LibraryTile.class)) {
                LibraryTile library = blockEntity.getCompatBlockEntity(LibraryTile.class);
                ItemScattererUtil.spawn(e.world, e.pos, library.getInventory());
            }
            super.onStateReplaced(e);
        }
    }
}
