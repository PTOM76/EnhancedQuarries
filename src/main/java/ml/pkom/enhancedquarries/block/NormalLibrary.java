package ml.pkom.enhancedquarries.block;

import ml.pkom.enhancedquarries.block.base.Library;
import ml.pkom.enhancedquarries.tile.NormalLibraryTile;
import ml.pkom.enhancedquarries.tile.base.LibraryTile;
import ml.pkom.mcpitanlibarch.api.block.ExtendBlockEntityProvider;
import ml.pkom.mcpitanlibarch.api.event.block.BlockUseEvent;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class NormalLibrary extends Library implements ExtendBlockEntityProvider {

    public static DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public NormalLibrary() {
        super();
        getStateManager().getDefaultState().with(FACING, Direction.NORTH);
    }

    // instance
    public static Library INSTANCE = new NormalLibrary();

    public static Library getInstance() {
        return INSTANCE;
    }

    public static Library getLibrary() {
        return getInstance();
    }
    // ----

    @Override
    public ActionResult onRightClick(BlockUseEvent e) {
        if (e.world.isClient())
            return ActionResult.SUCCESS;

        BlockEntity blockEntity = e.world.getBlockEntity(e.pos);
        if (blockEntity instanceof LibraryTile) {
            LibraryTile tile = (LibraryTile) blockEntity;
            e.player.openGuiScreen(tile);
            return ActionResult.CONSUME;
        }

        return ActionResult.CONSUME;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(TileCreateEvent event) {
        return new NormalLibraryTile(event);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        super.appendProperties(builder);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        if (ctx.getPlayer() == null) super.getPlacementState(ctx);
        return getDefaultState().with(FACING, ctx.getPlayer().getHorizontalFacing().getOpposite());
    }
}
