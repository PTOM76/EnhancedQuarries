package net.pitan76.enhancedquarries.block;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.Direction;
import net.pitan76.enhancedquarries.EnhancedQuarries;
import net.pitan76.enhancedquarries.block.base.Library;
import net.pitan76.enhancedquarries.tile.NormalLibraryTile;
import net.pitan76.enhancedquarries.tile.base.LibraryTile;
import net.pitan76.mcpitanlib.api.block.ExtendBlockEntityProvider;
import net.pitan76.mcpitanlib.api.block.args.v2.PlacementStateArgs;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.event.block.AppendPropertiesArgs;
import net.pitan76.mcpitanlib.api.event.block.BlockUseEvent;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.state.property.CompatProperties;
import net.pitan76.mcpitanlib.api.state.property.DirectionProperty;
import net.pitan76.mcpitanlib.api.util.CompatActionResult;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.midohra.block.BlockState;
import org.jetbrains.annotations.Nullable;

public class NormalLibrary extends Library implements ExtendBlockEntityProvider {

    public static DirectionProperty FACING = CompatProperties.HORIZONTAL_FACING;

    public NormalLibrary() {
        this(EnhancedQuarries._id("normal_library"));
    }

    public NormalLibrary(CompatIdentifier id) {
        super(id);
       setDefaultState(getDefaultMidohraState().with(FACING, Direction.NORTH));
    }

    public NormalLibrary(CompatibleBlockSettings settings) {
        super(settings);
        setDefaultState(getDefaultMidohraState().with(FACING, Direction.NORTH));
    }

    @Override
    public CompatActionResult onRightClick(BlockUseEvent e) {
        if (e.isClient())
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
        return super.getPlacementState(args).with(FACING, args.getHorizontalPlayerFacing().getOpposite());
    }
}
