package ml.pkom.enhancedquarries.block.base;

import ml.pkom.enhancedquarries.tile.base.BuilderTile;
import ml.pkom.mcpitanlibarch.api.block.CompatibleBlockSettings;
import ml.pkom.mcpitanlibarch.api.block.CompatibleMaterial;
import ml.pkom.mcpitanlibarch.api.event.block.StateReplacedEvent;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.ItemScatterer;

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
                ItemScatterer.spawn(e.world, e.pos, builder);
            }
            super.onStateReplaced(e);
        }
    }
}
