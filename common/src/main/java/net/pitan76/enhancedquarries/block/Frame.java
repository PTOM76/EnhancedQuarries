package net.pitan76.enhancedquarries.block;

import net.minecraft.block.Waterloggable;
import net.minecraft.util.shape.VoxelShape;
import net.pitan76.enhancedquarries.Blocks;
import net.pitan76.mcpitanlib.api.block.CompatBlockRenderType;
import net.pitan76.mcpitanlib.api.block.CompatibleMaterial;
import net.pitan76.mcpitanlib.api.block.args.RenderTypeArgs;
import net.pitan76.mcpitanlib.api.block.args.SideInvisibleArgs;
import net.pitan76.mcpitanlib.api.block.v2.BlockSettingsBuilder;
import net.pitan76.mcpitanlib.api.block.v2.CompatBlock;
import net.pitan76.mcpitanlib.api.event.block.*;
import net.pitan76.mcpitanlib.api.event.block.result.BlockBreakResult;
import net.pitan76.mcpitanlib.api.state.property.BooleanProperty;
import net.pitan76.mcpitanlib.api.state.property.CompatProperties;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.mcpitanlib.midohra.block.BlockState;
import net.pitan76.mcpitanlib.midohra.util.math.BlockPos;
import net.pitan76.mcpitanlib.midohra.util.math.Direction;
import net.pitan76.mcpitanlib.midohra.world.*;
import org.jetbrains.annotations.Nullable;

public class Frame extends CompatBlock implements Waterloggable {
    public static BooleanProperty CONNECT_NORTH = BooleanProperty.of("north");
    public static BooleanProperty CONNECT_SOUTH = BooleanProperty.of("south");
    public static BooleanProperty CONNECT_WEST = BooleanProperty.of("west");
    public static BooleanProperty CONNECT_EAST = BooleanProperty.of("east");
    public static BooleanProperty CONNECT_UP = BooleanProperty.of("up");
    public static BooleanProperty CONNECT_DOWN = BooleanProperty.of("down");

    public static VoxelShape CENTER = VoxelShapeUtil.cuboid(0.25, 0.25, 0.25, 0.75, 0.75, 0.75);
    public static VoxelShape NORTH = VoxelShapeUtil.cuboid(0.25, 0.25, 0, 0.75, 0.75, 0.25);
    public static VoxelShape SOUTH = VoxelShapeUtil.cuboid(0.25, 0.25, .75, 0.75, 0.75, 1);
    public static VoxelShape WEST = VoxelShapeUtil.cuboid(0, 0.25, 0.25, .25, 0.75, 0.75);
    public static VoxelShape EAST = VoxelShapeUtil.cuboid(.75, 0.25, 0.25, 1, 0.75, 0.75);
    public static VoxelShape UP = VoxelShapeUtil.cuboid(0.25, .75, 0.25, 0.75, 1, 0.75);
    public static VoxelShape DOWN = VoxelShapeUtil.cuboid(0.25, 0, 0.25, 0.75, .25, 0.75);

    public Frame(CompatIdentifier id) {
        super(new BlockSettingsBuilder(id).material(CompatibleMaterial.METAL).strength(1, 4).build().nonOpaque());
        setDefaultState(getDefaultMidohraState()
                .with(CONNECT_NORTH, false)
                .with(CONNECT_SOUTH, false)
                .with(CONNECT_WEST, false)
                .with(CONNECT_EAST, false)
                .with(CONNECT_UP, false)
                .with(CONNECT_DOWN, false)
        );
    }

    @Override
    public void appendProperties(AppendPropertiesArgs args) {
        args.addProperty(CompatProperties.WATERLOGGED, CONNECT_UP, CONNECT_DOWN, CONNECT_NORTH, CONNECT_SOUTH, CONNECT_EAST, CONNECT_WEST);
        super.appendProperties(args);
    }

    @Override
    public VoxelShape getOutlineShape(net.pitan76.mcpitanlib.api.block.args.v2.OutlineShapeEvent e) {
        return CENTER;
    }

    @Override
    public boolean isSideInvisible(SideInvisibleArgs args) {
        return true;
    }

    @Override
    public @Nullable BlockState getPlacementState(net.pitan76.mcpitanlib.api.block.args.v2.PlacementStateArgs args) {
        return getPlacementStateByTile(args.getWorld(), args.getPos());
    }

    public static net.minecraft.block.BlockState getPlacementStateByTile(net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos) {
        return getPlacementStateDefine(World.of(world), BlockPos.of(pos)).toMinecraft();
    }

    public static BlockState getPlacementStateByTile(World world, BlockPos pos) {
        return getPlacementStateDefine(world, pos);
    }

    public static BlockState getPlacementStateDefine(net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos) {
        return getPlacementStateDefine(World.of(world), BlockPos.of(pos));
    }

    public static BlockState getPlacementStateDefine(World world, BlockPos pos) {
        return BlockStateUtil.getMidohraDefaultState(Blocks.FRAME)
                .with(CONNECT_NORTH, canConnect(world, pos.north()))
                .with(CONNECT_SOUTH, canConnect(world, pos.south()))
                .with(CONNECT_EAST, canConnect(world, pos.east()))
                .with(CONNECT_WEST, canConnect(world, pos.west()))
                .with(CONNECT_UP, canConnect(world, pos.up()))
                .with(CONNECT_DOWN, canConnect(world, pos.down()));
    }

    @Override
    public BlockState getStateForNeighborUpdate(net.pitan76.mcpitanlib.api.block.args.v2.StateForNeighborUpdateArgs args) {
        IWorldView world = args.getWorldView();
        BlockPos pos = args.getPos();
        BlockState state = args.getBlockState();
        Direction direction = Direction.of(args.getDirection());

        if (args.getWorld().isClient())
            return super.getStateForNeighborUpdate(args);

        try {
            if (direction.equals(Direction.NORTH))
                return state.with(CONNECT_NORTH, canConnect(world, pos.offset(direction)));
            if (direction.equals(Direction.SOUTH))
                return state.with(CONNECT_SOUTH, canConnect(world, pos.offset(direction)));
            if (direction.equals(Direction.WEST))
                return state.with(CONNECT_WEST, canConnect(world, pos.offset(direction)));
            if (direction.equals(Direction.EAST))
                return state.with(CONNECT_EAST, canConnect(world, pos.offset(direction)));
            if (direction.equals(Direction.UP))
                return state.with(CONNECT_UP, canConnect(world, pos.offset(direction)));
            if (direction.equals(Direction.DOWN))
                return state.with(CONNECT_DOWN, canConnect(world, pos.offset(direction)));
        } catch (IllegalArgumentException e) {
            return super.getStateForNeighborUpdate(args);
        }
        return super.getStateForNeighborUpdate(args);
    }

    public static boolean canConnect(IWorldView world, BlockPos blockPos) {
        return world.getBlockState(blockPos).getBlock().get() == Blocks.FRAME;
    }

    @Override
    public BlockBreakResult onBreak(BlockBreakEvent e) {
        BlockBreakResult result = super.onBreak(e);
        breakConnectFrames(e.getWorld(), e.getPos());
        return result;
    }

    public static void breakConnectFrames(net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos) {
        breakConnectFrames(World.of(world), BlockPos.of(pos));
    }

    public static void breakConnectFrames(World world, BlockPos pos) {
        if (!world.isClient()) {
            if (!(world.getBlockState(pos).getBlock().get() instanceof Frame)) return;
            world.removeBlock(pos, false);
            if (world.getBlockState(pos.north()).getBlock().get() instanceof Frame) breakConnectFrames(world, pos.north());
            if (world.getBlockState(pos.south()).getBlock().get() instanceof Frame) breakConnectFrames(world, pos.south());
            if (world.getBlockState(pos.east()).getBlock().get() instanceof Frame) breakConnectFrames(world, pos.east());
            if (world.getBlockState(pos.west()).getBlock().get() instanceof Frame) breakConnectFrames(world, pos.west());
            if (world.getBlockState(pos.up()).getBlock().get() instanceof Frame) breakConnectFrames(world, pos.up());
            if (world.getBlockState(pos.down()).getBlock().get() instanceof Frame) breakConnectFrames(world, pos.down());
        }
    }

    @Override
    public CompatBlockRenderType getRenderType(RenderTypeArgs args) {
        return CompatBlockRenderType.MODEL;
    }
}
