package net.pitan76.enhancedquarries.block.base;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.ItemScatterer;
import net.pitan76.enhancedquarries.tile.base.LibraryTile;
import net.pitan76.mcpitanlib.api.block.CompatibleMaterial;
import net.pitan76.mcpitanlib.api.block.v2.BlockSettingsBuilder;
import net.pitan76.mcpitanlib.api.block.v2.CompatBlock;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.event.block.StateReplacedEvent;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;

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
        if (e.state.getBlock() != e.newState.getBlock()) {
            BlockEntity blockEntity = e.world.getBlockEntity(e.pos);
            if (blockEntity instanceof LibraryTile) {
                LibraryTile library = (LibraryTile) blockEntity;
                ItemScatterer.spawn(e.world, e.pos, library.getInventory());
            }
            super.onStateReplaced(e);
        }
    }
}
