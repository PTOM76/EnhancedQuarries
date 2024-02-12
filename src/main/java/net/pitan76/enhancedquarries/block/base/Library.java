package net.pitan76.enhancedquarries.block.base;

import net.pitan76.enhancedquarries.tile.base.LibraryTile;
import ml.pkom.mcpitanlibarch.api.block.CompatibleBlockSettings;
import ml.pkom.mcpitanlibarch.api.block.CompatibleMaterial;
import ml.pkom.mcpitanlibarch.api.block.ExtendBlock;
import ml.pkom.mcpitanlibarch.api.event.block.StateReplacedEvent;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.ItemScatterer;

public abstract class Library extends ExtendBlock {

    public static CompatibleBlockSettings defaultSettings = CompatibleBlockSettings
            .of(CompatibleMaterial.METAL)
            .requiresTool()
            .strength(2, 8);

    public Library() {
        super(defaultSettings);
    }

    @Override
    public void onStateReplaced(StateReplacedEvent e) {
        if (e.state.getBlock() != e.newState.getBlock()) {
            BlockEntity blockEntity = e.world.getBlockEntity(e.pos);
            if (blockEntity instanceof LibraryTile) {
                LibraryTile library = (LibraryTile)blockEntity;
                ItemScatterer.spawn(e.world, e.pos, library.getInventory());
            }
            super.onStateReplaced(e);
        }
    }
}
