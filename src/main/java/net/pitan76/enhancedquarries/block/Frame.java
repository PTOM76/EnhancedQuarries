package net.pitan76.enhancedquarries.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.pitan76.mcpitanlib.api.block.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.block.CompatibleMaterial;
import net.pitan76.mcpitanlib.api.block.ExtendBlock;
import net.pitan76.mcpitanlib.api.event.block.AppendPropertiesArgs;
import net.pitan76.mcpitanlib.api.event.block.BlockBreakEvent;
import net.pitan76.mcpitanlib.api.event.block.OutlineShapeEvent;
import net.pitan76.mcpitanlib.api.event.block.result.BlockBreakResult;

public class Frame extends ExtendBlock {
    public static BooleanProperty CONNECT_NORTH = BooleanProperty.of("north");
    public static BooleanProperty CONNECT_SOUTH = BooleanProperty.of("south");
    public static BooleanProperty CONNECT_WEST = BooleanProperty.of("west");
    public static BooleanProperty CONNECT_EAST = BooleanProperty.of("east");
    public static BooleanProperty CONNECT_UP = BooleanProperty.of("up");
    public static BooleanProperty CONNECT_DOWN = BooleanProperty.of("down");

    public static VoxelShape CENTER = VoxelShapes.cuboid(0.25, 0.25, 0.25, 0.75, 0.75, 0.75);
    public static VoxelShape NORTH = VoxelShapes.cuboid(0.25, 0.25, 0, 0.75, 0.75, 0.25);
    public static VoxelShape SOUTH = VoxelShapes.cuboid(0.25, 0.25, .75, 0.75, 0.75, 1);
    public static VoxelShape WEST = VoxelShapes.cuboid(0, 0.25, 0.25, .25, 0.75, 0.75);
    public static VoxelShape EAST = VoxelShapes.cuboid(.75, 0.25, 0.25, 1, 0.75, 0.75);
    public static VoxelShape UP = VoxelShapes.cuboid(0.25, .75, 0.25, 0.75, 1, 0.75);
    public static VoxelShape DOWN = VoxelShapes.cuboid(0.25, 0, 0.25, 0.75, .25, 0.75);

    public Frame() {
        super(CompatibleBlockSettings.of(CompatibleMaterial.METAL).strength(1, 4).nonOpaque());
        setDefaultState(getDefaultState()
                .with(CONNECT_NORTH, false)
                .with(CONNECT_SOUTH, false)
                .with(CONNECT_WEST, false)
                .with(CONNECT_EAST, false)
                .with(CONNECT_UP, false)
                .with(CONNECT_DOWN, false)
        );
    }

    public static Block INSTANCE = new Frame();

    public static Block getInstance() {
        return INSTANCE;
    }

    public static Block getBlock() {
        return getInstance();
    }

    @Override
    public void appendProperties(AppendPropertiesArgs args) {
        args.addProperty(Properties.WATERLOGGED, CONNECT_UP, CONNECT_DOWN, CONNECT_NORTH, CONNECT_SOUTH, CONNECT_EAST, CONNECT_WEST);
        super.appendProperties(args);
    }

    @Override
    public VoxelShape getOutlineShape(OutlineShapeEvent event) {
        return CENTER;
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        return true;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        return getPlacementStateByTile(world, pos);
    }

    public static BlockState getPlacementStateByTile(World world, BlockPos pos) {
        return getPlacementStateDefine(world, pos);
    }

    public static BlockState getPlacementStateDefine(World world, BlockPos pos) {
        return getBlock().getDefaultState()
                .with(CONNECT_NORTH, canConnect(world, pos.north()))
                .with(CONNECT_SOUTH, canConnect(world, pos.south()))
                .with(CONNECT_EAST, canConnect(world, pos.east()))
                .with(CONNECT_WEST, canConnect(world, pos.west()))
                .with(CONNECT_UP, canConnect(world, pos.up()))
                .with(CONNECT_DOWN, canConnect(world, pos.down()));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (world.isClient()) return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
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
            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    public static boolean canConnect(BlockView world, BlockPos blockPos) {
        return world.getBlockState(blockPos).getBlock() == getBlock();
    }

    public static boolean canConnect(World world, BlockPos blockPos) {
        return world.getBlockState(blockPos).getBlock() == getBlock();
    }

    @Override
    public BlockBreakResult onBreak(BlockBreakEvent e) {
        BlockBreakResult result = super.onBreak(e);
        breakConnectFrames(e.world, e.pos);
        return result;
    }

    public static void breakConnectFrames(World world, BlockPos pos) {
        if (!world.isClient()) {
            if (!(world.getBlockState(pos).getBlock() instanceof Frame)) return;
            world.removeBlock(pos, false);
            if (world.getBlockState(pos.north()).getBlock() instanceof Frame) breakConnectFrames(world, pos.north());
            if (world.getBlockState(pos.south()).getBlock() instanceof Frame) breakConnectFrames(world, pos.south());
            if (world.getBlockState(pos.east()).getBlock() instanceof Frame) breakConnectFrames(world, pos.east());
            if (world.getBlockState(pos.west()).getBlock() instanceof Frame) breakConnectFrames(world, pos.west());
            if (world.getBlockState(pos.up()).getBlock() instanceof Frame) breakConnectFrames(world, pos.up());
            if (world.getBlockState(pos.down()).getBlock() instanceof Frame) breakConnectFrames(world, pos.down());
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
