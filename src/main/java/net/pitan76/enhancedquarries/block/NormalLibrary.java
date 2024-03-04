package net.pitan76.enhancedquarries.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;
import net.pitan76.enhancedquarries.block.base.Library;
import net.pitan76.enhancedquarries.tile.NormalLibraryTile;
import net.pitan76.enhancedquarries.tile.base.LibraryTile;
import net.pitan76.mcpitanlib.api.block.ExtendBlockEntityProvider;
import net.pitan76.mcpitanlib.api.event.block.AppendPropertiesArgs;
import net.pitan76.mcpitanlib.api.event.block.BlockUseEvent;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
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
            return e.success();

        BlockEntity blockEntity = e.world.getBlockEntity(e.pos);
        if (blockEntity instanceof LibraryTile) {
            LibraryTile tile = (LibraryTile) blockEntity;
            e.player.openGuiScreen(tile);
            return e.consume();
        }

        return e.consume();
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(TileCreateEvent event) {
        return new NormalLibraryTile(event);
    }

    @Override
    public void appendProperties(AppendPropertiesArgs args) {
        args.addProperty(FACING);
        super.appendProperties(args);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        if (ctx.getPlayer() == null) super.getPlacementState(ctx);
        return getDefaultState().with(FACING, ctx.getPlayer().getHorizontalFacing().getOpposite());
    }
}
