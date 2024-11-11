package net.pitan76.enhancedquarries.block;

import net.pitan76.mcpitanlib.api.util.CompatActionResult;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
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
import net.pitan76.mcpitanlib.api.event.block.PlacementStateArgs;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import org.jetbrains.annotations.Nullable;

public class NormalLibrary extends Library implements ExtendBlockEntityProvider {

    public static DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public NormalLibrary() {
        super();
        getNewDefaultState().with(FACING, Direction.NORTH);
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
    public CompatActionResult onRightClick(BlockUseEvent e) {
        if (WorldUtil.isClient(e.world))
            return e.success();

        BlockEntity blockEntity = e.getBlockEntity();
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
    public BlockState getPlacementState(PlacementStateArgs args) {
        if (args.getPlayer() == null) super.getPlacementState(args);
        return getNewDefaultState().with(FACING, args.getPlayer().getHorizontalFacing().getOpposite());
    }
}
