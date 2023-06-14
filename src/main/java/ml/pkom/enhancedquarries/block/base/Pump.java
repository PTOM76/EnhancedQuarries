package ml.pkom.enhancedquarries.block.base;

import ml.pkom.mcpitanlibarch.api.block.CompatibleBlockSettings;
import ml.pkom.mcpitanlibarch.api.block.CompatibleMaterial;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import ml.pkom.enhancedquarries.tile.base.PumpTile;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public abstract class Pump extends BaseBlock implements BlockEntityProvider {
    public static CompatibleBlockSettings defaultSettings = CompatibleBlockSettings
            .of(CompatibleMaterial.METAL)
            .requiresTool()
            //.breakByTool(FabricToolTags.PICKAXES, 0)
            .strength(2, 8);

    // Custom Setting
    public Pump(CompatibleBlockSettings settings) {
        super(settings.build());
    }

    // Default Setting
    public Pump() {
        this(defaultSettings);
    }

    // 1.17.1へのポート用
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return createBlockEntity(new TileCreateEvent(pos, state));
    }

    public BlockEntity createBlockEntity(BlockView world) {
        return createBlockEntity(new TileCreateEvent(world));
    }

    public abstract BlockEntity createBlockEntity(TileCreateEvent event);

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : (w, p, s, tile) -> ((PumpTile) tile).tick(w, p, s, (PumpTile) tile);
    }
}
