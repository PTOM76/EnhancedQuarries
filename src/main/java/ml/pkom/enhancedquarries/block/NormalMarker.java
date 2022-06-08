package ml.pkom.enhancedquarries.block;

import ml.pkom.enhancedquarries.Blocks;
import ml.pkom.enhancedquarries.event.BlockStatePos;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class NormalMarker extends Block {

    public static DirectionProperty FACING = Properties.FACING;
    public static BooleanProperty ACTIVE = BooleanProperty.of("active");

    public NormalMarker() {
        super(FabricBlockSettings.of(Material.METAL).strength(1, 4));
            this.setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(ACTIVE, false));
    }

    public static Block INSTANCE = new NormalMarker();

    public static Block getInstance() {
        return INSTANCE;
    }

    public static Block getBlock() {
        return getInstance();
    }

    protected static VoxelShape UP_SHAPE = Block.createCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 10.0D, 10.0D);
    protected static VoxelShape DOWN_SHAPE = Block.createCuboidShape(6.0D, 6.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    protected static VoxelShape NORTH_SHAPE = Block.createCuboidShape(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 16.0D);
    protected static VoxelShape SOUTH_SHAPE = Block.createCuboidShape(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 10.0D);
    protected static VoxelShape EAST_SHAPE = Block.createCuboidShape(0.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);
    protected static VoxelShape WEST_SHAPE = Block.createCuboidShape(6.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D);

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (getFacing(state).equals(Direction.UP)) return UP_SHAPE;
        if (getFacing(state).equals(Direction.DOWN)) return DOWN_SHAPE;
        if (getFacing(state).equals(Direction.NORTH)) return NORTH_SHAPE;
        if (getFacing(state).equals(Direction.SOUTH)) return SOUTH_SHAPE;
        if (getFacing(state).equals(Direction.EAST)) return EAST_SHAPE;
        if (getFacing(state).equals(Direction.WEST)) return WEST_SHAPE;
        return UP_SHAPE;
    }

    public static void searchMarker(World world, BlockPos pos, List<BlockStatePos> list) {
        int i;
        for (i = 0;i <= 64;i++) {
            BlockPos blockPosX1 = new BlockPos(pos.getX() + i, pos.getY(), pos.getZ());
            BlockPos blockPosX2 = new BlockPos(pos.getX() - i, pos.getY(), pos.getZ());
            BlockPos blockPosY1 = new BlockPos(pos.getX(), pos.getY() + i, pos.getZ());
            BlockPos blockPosY2 = new BlockPos(pos.getX(), pos.getY() - i, pos.getZ());
            BlockPos blockPosZ1 = new BlockPos(pos.getX(), pos.getY(), pos.getZ() + i);
            BlockPos blockPosZ2 = new BlockPos(pos.getX(), pos.getY(), pos.getZ() - i);

            BlockState blockState;
            List<BlockPos> blockPosList = new ArrayList<>();
            for (BlockStatePos blockStatePos : list) {
                blockPosList.add(blockStatePos.getBlockPos());
            }
            blockState = world.getBlockState(blockPosX1);
            if (blockState.getBlock().equals(Blocks.NORMAL_MARKER))
                if (!blockPosList.contains(blockPosX1)) {
                    list.add(new BlockStatePos(blockState, blockPosX1, world));
                    searchMarker(world, blockPosX1, list);
                }
            blockState = world.getBlockState(blockPosX2);
            if (blockState.getBlock().equals(Blocks.NORMAL_MARKER))
                if (!blockPosList.contains(blockPosX2)) {
                    list.add(new BlockStatePos(blockState, blockPosX2, world));
                    searchMarker(world, blockPosX2, list);
                }
            blockState = world.getBlockState(blockPosY1);
            if (blockState.getBlock().equals(Blocks.NORMAL_MARKER))
                if (!blockPosList.contains(blockPosY1)) {
                    list.add(new BlockStatePos(blockState, blockPosY1, world));
                    searchMarker(world, blockPosY1, list);
                }
            blockState = world.getBlockState(blockPosY2);
            if (blockState.getBlock().equals(Blocks.NORMAL_MARKER))
                if (!blockPosList.contains(blockPosY2)) {
                    list.add(new BlockStatePos(blockState, blockPosY2, world));
                    searchMarker(world, blockPosY2, list);
                }
            blockState = world.getBlockState(blockPosZ1);
            if (blockState.getBlock().equals(Blocks.NORMAL_MARKER))
                if (!blockPosList.contains(blockPosZ1)) {
                    list.add(new BlockStatePos(blockState, blockPosZ1, world));
                    searchMarker(world, blockPosZ1, list);
                }
            blockState = world.getBlockState(blockPosZ2);
            if (blockState.getBlock().equals(Blocks.NORMAL_MARKER))
                if (!blockPosList.contains(blockPosZ2)) {
                    list.add(new BlockStatePos(blockState, blockPosZ2, world));
                    searchMarker(world, blockPosZ2, list);
                }
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient()) return ActionResult.SUCCESS;
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
            markerList.forEach((markerSP) -> {
                setActive(false, markerSP.getWorld(), markerSP.getBlockPos());
            });
        } else {
            markerList.forEach((markerSP) -> {
                setActive(true, markerSP.getWorld(), markerSP.getBlockPos());
            });
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        if (world.isClient()) return;
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
    }

    public static void setFacing(Direction facing, World world, BlockPos pos) {
        world.setBlockState(pos, world.getBlockState(pos).with(FACING, facing));
    }

    public static void setActive(boolean active, World world, BlockPos pos) {
        world.setBlockState(pos, world.getBlockState(pos).with(ACTIVE, active));
    }

    public static Direction getFacing(BlockState state) {
        return state.get(FACING);
    }

    public static boolean getActive(BlockState state) {
        return state.get(ACTIVE);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = this.getDefaultState();
        Direction direction = ctx.getSide();
        return blockState.with(FACING, direction);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, ACTIVE);
    }
}
