package net.pitan76.enhancedquarries.block;

import net.minecraft.block.Block;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.pitan76.enhancedquarries.Blocks;
import net.pitan76.enhancedquarries.EnhancedQuarries;
import net.pitan76.enhancedquarries.event.v2.BlockStatePos;
import net.pitan76.mcpitanlib.api.block.CompatibleMaterial;
import net.pitan76.mcpitanlib.api.block.args.v2.CollisionShapeEvent;
import net.pitan76.mcpitanlib.api.block.v2.BlockSettingsBuilder;
import net.pitan76.mcpitanlib.api.block.v2.CompatBlock;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.event.block.*;
import net.pitan76.mcpitanlib.api.block.args.v2.OutlineShapeEvent;
import net.pitan76.mcpitanlib.api.event.block.result.BlockBreakResult;
import net.pitan76.mcpitanlib.api.state.property.BooleanProperty;
import net.pitan76.mcpitanlib.api.state.property.CompatProperties;
import net.pitan76.mcpitanlib.api.state.property.DirectionProperty;
import net.pitan76.mcpitanlib.api.util.CompatActionResult;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.midohra.block.BlockState;
import net.pitan76.mcpitanlib.midohra.util.math.BlockPos;
import net.pitan76.mcpitanlib.midohra.util.math.Direction;
import net.pitan76.mcpitanlib.midohra.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NormalMarker extends CompatBlock { //BlockWithEntity {

    public static DirectionProperty FACING = CompatProperties.FACING;
    public static BooleanProperty ACTIVE = BooleanProperty.of("active");

    public static BlockSettingsBuilder defaultSettings = new BlockSettingsBuilder()
            .material(CompatibleMaterial.METAL)
            .strength(1, 4);

    public NormalMarker() {
        this(EnhancedQuarries._id("normal_marker"));
    }

    public NormalMarker(CompatIdentifier id) {
        this(defaultSettings.build(id));
    }

    public NormalMarker(CompatibleBlockSettings settings) {
        super(settings);
        setDefaultState(this.getDefaultMidohraState().with(FACING, Direction.NORTH).with(ACTIVE, false));
    }

    protected static VoxelShape UP_SHAPE = Block.createCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 10.0D, 10.0D);
    protected static VoxelShape DOWN_SHAPE = Block.createCuboidShape(6.0D, 6.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    protected static VoxelShape NORTH_SHAPE = Block.createCuboidShape(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 16.0D);
    protected static VoxelShape SOUTH_SHAPE = Block.createCuboidShape(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 10.0D);
    protected static VoxelShape EAST_SHAPE = Block.createCuboidShape(0.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);
    protected static VoxelShape WEST_SHAPE = Block.createCuboidShape(6.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D);

    @Override
    public VoxelShape getOutlineShape(OutlineShapeEvent e) {
        BlockState state = e.state;

        if (getFacing(state).equals(Direction.UP)) return UP_SHAPE;
        if (getFacing(state).equals(Direction.DOWN)) return DOWN_SHAPE;
        if (getFacing(state).equals(Direction.NORTH)) return NORTH_SHAPE;
        if (getFacing(state).equals(Direction.SOUTH)) return SOUTH_SHAPE;
        if (getFacing(state).equals(Direction.EAST)) return EAST_SHAPE;
        if (getFacing(state).equals(Direction.WEST)) return WEST_SHAPE;
        return UP_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(CollisionShapeEvent event) {
        return VoxelShapes.empty();
    }

    public static void searchMarker(net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos, List<net.pitan76.enhancedquarries.event.BlockStatePos> list) {
        List<BlockStatePos> list2 = new ArrayList<>();
        for (net.pitan76.enhancedquarries.event.BlockStatePos blockStatePos : list) {
            list2.add(new BlockStatePos(BlockState.of(blockStatePos.getBlockState()), BlockPos.of(blockStatePos.getBlockPos()), World.of(blockStatePos.getWorld())));
        }

        searchMarker(World.of(world), BlockPos.of(pos), list2);
    }

    public static void searchMarker(World world, BlockPos pos, List<BlockStatePos> list) {
        int i;
        for (i = 0; i <= 64; i++) {
            BlockPos blockPosX1 = BlockPos.of(pos.getX() + i, pos.getY(), pos.getZ());
            BlockPos blockPosX2 = BlockPos.of(pos.getX() - i, pos.getY(), pos.getZ());
            BlockPos blockPosY1 = BlockPos.of(pos.getX(), pos.getY() + i, pos.getZ());
            BlockPos blockPosY2 = BlockPos.of(pos.getX(), pos.getY() - i, pos.getZ());
            BlockPos blockPosZ1 = BlockPos.of(pos.getX(), pos.getY(), pos.getZ() + i);
            BlockPos blockPosZ2 = BlockPos.of(pos.getX(), pos.getY(), pos.getZ() - i);

            BlockState blockState;

            // Loop through the blockStatePos list to get the already visited positions
            List<BlockPos> blockPosList = new ArrayList<>();
            for (BlockStatePos blockStatePos : list) {
                blockPosList.add(blockStatePos.getBlockPos());
            }

            blockState = world.getBlockState(blockPosX1);
            if (blockState.getBlock().get().equals(Blocks.NORMAL_MARKER)) {
                if (!_has(blockPosList, blockPosX1)) {
                    list.add(new BlockStatePos(blockState, blockPosX1, world));
                    searchMarker(world, blockPosX1, list);
                }
            }

            blockState = world.getBlockState(blockPosX2);
            if (blockState.getBlock().get().equals(Blocks.NORMAL_MARKER)) {
                if (!_has(blockPosList, blockPosX2)) {
                    list.add(new BlockStatePos(blockState, blockPosX2, world));
                    searchMarker(world, blockPosX2, list);
                }
            }

            blockState = world.getBlockState(blockPosY1);
            if (blockState.getBlock().get().equals(Blocks.NORMAL_MARKER)) {
                if (!_has(blockPosList, blockPosY1)) {
                    list.add(new BlockStatePos(blockState, blockPosY1, world));
                    searchMarker(world, blockPosY1, list);
                }
            }

            blockState = world.getBlockState(blockPosY2);
            if (blockState.getBlock().get().equals(Blocks.NORMAL_MARKER)) {
                if (!_has(blockPosList, blockPosY2)) {
                    list.add(new BlockStatePos(blockState, blockPosY2, world));
                    searchMarker(world, blockPosY2, list);
                }
            }

            blockState = world.getBlockState(blockPosZ1);
            if (blockState.getBlock().get().equals(Blocks.NORMAL_MARKER)) {
                if (!_has(blockPosList, blockPosZ1)) {
                    list.add(new BlockStatePos(blockState, blockPosZ1, world));
                    searchMarker(world, blockPosZ1, list);
                }
            }

            blockState = world.getBlockState(blockPosZ2);
            if (blockState.getBlock().get().equals(Blocks.NORMAL_MARKER)) {
                if (!_has(blockPosList, blockPosZ2)) {
                    list.add(new BlockStatePos(blockState, blockPosZ2, world));
                    searchMarker(world, blockPosZ2, list);
                }
            }
        }
    }

    // Helper method to check if the position already exists in the list
    private static boolean _has(List<BlockPos> list, BlockPos pos) {
        for (BlockPos p : list) {
            if (p.getX() == pos.getX() && p.getY() == pos.getY() && p.getZ() == pos.getZ()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public CompatActionResult onRightClick(BlockUseEvent e) {
        World world = e.getMidohraWorld();
        BlockPos pos = e.getMidohraPos();
        BlockState state = e.getMidohraState();

        if (e.isClient()) return e.success();
        List<BlockStatePos> markerList = new ArrayList<>();
        markerList.add(new BlockStatePos(state, pos, world));
        searchMarker(world, pos, markerList);
        //for (BlockStatePos blockStatePos : markerList) {
        //    FillerPlus.log(Level.INFO, blockStatePos.toString());
        //}
        Integer maxPosX = null, maxPosY = null, maxPosZ = null;
        Integer minPosX = null, minPosY = null, minPosZ = null;
        for (BlockStatePos markerSP : markerList) {
            if (maxPosX == null || markerSP.getPosX() > maxPosX) maxPosX = markerSP.getPosX();
            if (maxPosY == null || markerSP.getPosY() > maxPosY) maxPosY = markerSP.getPosY();
            if (maxPosZ == null || markerSP.getPosZ() > maxPosZ) maxPosZ = markerSP.getPosZ();
            if (minPosX == null || markerSP.getPosX() < minPosX) minPosX = markerSP.getPosX();
            if (minPosY == null || markerSP.getPosY() < minPosY) minPosY = markerSP.getPosY();
            if (minPosZ == null || markerSP.getPosZ() < minPosZ) minPosZ = markerSP.getPosZ();
        }
        if ((maxPosX == null || maxPosY == null || maxPosZ == null || minPosX == null || minPosY == null || minPosZ == null) || markerList.size() <= 2) {
            markerList.forEach((markerSP) -> setActive(false, markerSP.getWorld(), markerSP.getBlockPos()));
        } else {
            markerList.forEach((markerSP) -> setActive(true, markerSP.getWorld(), markerSP.getBlockPos()));
        }
        return e.success();
    }

    @Override
    public BlockBreakResult onBreak(BlockBreakEvent e) {
        BlockBreakResult result = super.onBreak(e);
        World world = World.of(e.getWorld());
        BlockPos pos = BlockPos.of(e.getPos());
        BlockState state = BlockState.of(e.getState());

        if (e.isClient()) return result;
        if (getActive(state)) {
            List<BlockStatePos> markerList = new ArrayList<>();
            markerList.add(new BlockStatePos(state, pos, world));
            searchMarker(world, pos, markerList);
            //for (BlockStatePos blockStatePos : markerList) {
            //    FillerPlus.log(Level.INFO, blockStatePos.toString());
            //}
            Integer maxPosX = null, maxPosY = null, maxPosZ = null;
            Integer minPosX = null, minPosY = null, minPosZ = null;
            for (BlockStatePos markerSP : markerList) {
                if (maxPosX == null || markerSP.getPosX() > maxPosX) maxPosX = markerSP.getPosX();
                if (maxPosY == null || markerSP.getPosY() > maxPosY) maxPosY = markerSP.getPosY();
                if (maxPosZ == null || markerSP.getPosZ() > maxPosZ) maxPosZ = markerSP.getPosZ();
                if (minPosX == null || markerSP.getPosX() < minPosX) minPosX = markerSP.getPosX();
                if (minPosY == null || markerSP.getPosY() < minPosY) minPosY = markerSP.getPosY();
                if (minPosZ == null || markerSP.getPosZ() < minPosZ) minPosZ = markerSP.getPosZ();
            }
            setActive(!((maxPosX == null || maxPosY == null || maxPosZ == null || minPosX == null || minPosY == null || minPosZ == null ) || markerList.size() <= 2) , world, pos);
        }
        return result;
    }

    public static void setFacing(net.minecraft.util.math.Direction facing, net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos) {
        setFacing(Direction.of(facing), World.of(world), BlockPos.of(pos));
    }

    public static void setFacing(Direction facing, World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        world.setBlockState(pos, state.with(FACING, facing));
    }

    public static void setActive(boolean active, net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos) {
        setActive(active, World.of(world), BlockPos.of(pos));
    }

    public static void setActive(boolean active, World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        world.setBlockState(pos, state.with(ACTIVE, active));
    }

    public static net.minecraft.util.math.Direction getFacing(net.minecraft.block.BlockState state) {
        return getFacing(BlockState.of(state)).toMinecraft();
    }

    public static Direction getFacing(BlockState state) {
        return state.get(FACING);
    }

    public static boolean getActive(net.minecraft.block.BlockState state) {
        return getActive(BlockState.of(state));
    }

    public static boolean getActive(BlockState state) {
        return state.get(ACTIVE);
    }

    @Override
    public @Nullable BlockState getPlacementState(net.pitan76.mcpitanlib.api.block.args.v2.PlacementStateArgs args) {
        BlockState blockState = this.getDefaultMidohraState();
        return blockState.with(FACING, args.getSide());
    }

    @Override
    public void appendProperties(AppendPropertiesArgs args) {
        args.addProperty(FACING, ACTIVE);
        super.appendProperties(args);
    }

    /*
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MarkerTile(new TileCreateEvent(pos, state));
    }
     */
}
