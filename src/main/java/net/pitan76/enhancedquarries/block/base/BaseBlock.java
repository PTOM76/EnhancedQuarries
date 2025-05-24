package net.pitan76.enhancedquarries.block.base;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.pitan76.enhancedquarries.tile.base.BaseEnergyTile;
import net.pitan76.mcpitanlib.api.block.ExtendBlockEntityProvider;
import net.pitan76.mcpitanlib.api.block.args.RotateArgs;
import net.pitan76.mcpitanlib.api.block.v2.CompatBlock;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.event.block.AppendPropertiesArgs;
import net.pitan76.mcpitanlib.api.event.block.BlockPlacedEvent;
import net.pitan76.mcpitanlib.api.event.nbt.ReadNbtArgs;
import net.pitan76.mcpitanlib.api.state.property.BooleanProperty;
import net.pitan76.mcpitanlib.api.state.property.CompatProperties;
import net.pitan76.mcpitanlib.api.state.property.DirectionProperty;
import net.pitan76.mcpitanlib.api.util.BlockEntityDataUtil;
import net.pitan76.mcpitanlib.api.util.CustomDataUtil;
import net.pitan76.mcpitanlib.midohra.block.BlockState;
import net.pitan76.mcpitanlib.midohra.util.math.BlockPos;
import net.pitan76.mcpitanlib.midohra.util.math.Direction;
import net.pitan76.mcpitanlib.midohra.world.World;

public class BaseBlock extends CompatBlock implements ExtendBlockEntityProvider {

    public static final DirectionProperty FACING = CompatProperties.HORIZONTAL_FACING;
    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");

    public BaseBlock(CompatibleBlockSettings settings) {
        super(settings);
        setDefaultState(getDefaultMidohraState().with(ACTIVE, false).with(FACING, Direction.NORTH));
    }

    public static void setFacing(net.minecraft.util.math.Direction facing, net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos) {
        setFacing(Direction.of(facing), World.of(world), BlockPos.of(pos));
    }

    public static void setFacing(Direction facing, World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);

        if (!state.contains(FACING)) return;
        world.setBlockState(pos, state.with(FACING, facing));
    }

    public static net.minecraft.util.math.Direction getFacing(net.minecraft.block.BlockState state) {
        return getFacing(BlockState.of(state)).toMinecraft();
    }

    public static Direction getFacing(BlockState state) {
        if (!state.contains(FACING)) return Direction.NORTH;
        return state.get(FACING);
    }

    public static void setActive(Boolean active, net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos) {
        setActive(active, World.of(world), BlockPos.of(pos));
    }

    public static void setActive(Boolean active, World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (!state.contains(FACING) || !state.contains(ACTIVE)) return;

        Direction facing = state.get(FACING);
        BlockState newState = state.with(ACTIVE, active).with(FACING, facing);
        world.setBlockState(pos, newState);
    }

    public static boolean isActive(net.minecraft.block.BlockState state) {
        return isActive(BlockState.of(state));
    }

    public static boolean isActive(BlockState state) {
        return state.contains(ACTIVE) && state.get(ACTIVE);
    }

    @Override
    public void onPlaced(BlockPlacedEvent e) {
        super.onPlaced(e);
        if (e.placer != null)
            setFacing(e.placer.getHorizontalFacing().getOpposite(), e.getWorld(), e.getPos());

        BlockEntity blockEntity = e.getBlockEntity();
        if (BlockEntityDataUtil.hasBlockEntityNbt(e.stack) && blockEntity instanceof BaseEnergyTile)
            BlockEntityDataUtil.readCompatBlockEntityNbtFromStack(e.stack, (BaseEnergyTile) blockEntity);

    }

    @Override
    public void appendProperties(AppendPropertiesArgs args) {
        super.appendProperties(args);
        args.addProperty(FACING, ACTIVE);
    }

    @Override
    public BlockState rotate(RotateArgs args) {
        BlockState state = args.getBlockState();
        Direction facing = state.get(FACING);
        return state.with(FACING, args.rotate(facing));
    }

    @Override
    public boolean isTick() {
        return true;
    }
}
