package net.pitan76.enhancedquarries.block.base;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.ItemScatterer;
import net.pitan76.enhancedquarries.tile.base.BuilderTile;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.block.CompatibleMaterial;
import net.pitan76.mcpitanlib.api.event.block.StateReplacedEvent;

public abstract class Builder extends BaseBlock {

    public static CompatibleBlockSettings defaultSettings = CompatibleBlockSettings
            .of(CompatibleMaterial.METAL)
            .requiresTool()
            .strength(2, 8);

    public Builder(CompatibleBlockSettings settings) {
        super(defaultSettings);
    }

    public Builder() {
        super(defaultSettings);
    }

    @Override
    public void onStateReplaced(StateReplacedEvent e) {
        if (e.state.getBlock() != e.newState.getBlock()) {
            BlockEntity blockEntity = e.world.getBlockEntity(e.pos);
            if (blockEntity instanceof BuilderTile) {
                BuilderTile builder = (BuilderTile) blockEntity;
                if (builder.keepNbtOnDrop) {
                    super.onStateReplaced(e);
                    return;
                }

                ItemScatterer.spawn(e.world, e.pos, builder);
            }
            super.onStateReplaced(e);
        }
    }
}
