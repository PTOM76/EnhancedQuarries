package ml.pkom.enhancedquarries.block.base;

import ml.pkom.enhancedquarries.tile.base.BuilderTile;
import ml.pkom.mcpitanlibarch.api.block.CompatibleBlockSettings;
import ml.pkom.mcpitanlibarch.api.block.CompatibleMaterial;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BuilderTile) {
                BuilderTile builder = (BuilderTile) blockEntity;
                ItemScatterer.spawn(world, pos, builder);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
}
