package net.pitan76.enhancedquarries.block.base;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
import net.pitan76.mcpitanlib.api.util.CustomDataUtil;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import net.pitan76.mcpitanlib.midohra.util.math.Direction;

public class BaseBlock extends CompatBlock implements ExtendBlockEntityProvider {

    public static final DirectionProperty FACING = CompatProperties.HORIZONTAL_FACING;
    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");

    public BaseBlock(CompatibleBlockSettings settings) {
        super(settings);
        setDefaultState(getDefaultMidohraState().with(ACTIVE, false).with(FACING, Direction.NORTH));
    }

    public static void setFacing(Direction facing, World world, BlockPos pos) {
        net.pitan76.mcpitanlib.midohra.block.BlockState state = net.pitan76.mcpitanlib.midohra.block.BlockState.of(WorldUtil.getBlockState(world, pos));
        WorldUtil.setBlockState(world, pos, state.with(FACING, facing).toMinecraft());
    }

    public static Direction getFacing(BlockState state) {
        return FACING.getAsMidohra(net.pitan76.mcpitanlib.midohra.block.BlockState.of(state));
    }

    public static void setActive(Boolean active, World world, BlockPos pos) {
        net.pitan76.mcpitanlib.midohra.block.BlockState state = net.pitan76.mcpitanlib.midohra.block.BlockState.of(WorldUtil.getBlockState(world, pos));

        Direction facing = state.get(FACING);
        BlockState newState = state.with(ACTIVE, active).with(FACING, facing).toMinecraft();
        WorldUtil.setBlockState(world, pos, newState);
    }

    public static boolean isActive(BlockState state) {
        return net.pitan76.mcpitanlib.midohra.block.BlockState.of(state).get(ACTIVE);
    }

    @Override
    public void onPlaced(BlockPlacedEvent e) {
        super.onPlaced(e);
        if (e.placer != null)
            setFacing(Direction.of(e.placer.getHorizontalFacing().getOpposite()), e.world, e.pos);

        loadBlockEntityTag(e);
    }

    @Deprecated
    private static void loadBlockEntityTag(BlockPlacedEvent e) {
        if (!CustomDataUtil.hasNbt(e.stack)) return;
        if (!CustomDataUtil.getNbt(e.stack).contains("BlockEntityTag")) return;

        NbtCompound nbt = CustomDataUtil.get(e.stack, "BlockEntityTag");
        BlockEntity blockEntity = e.getBlockEntity();
        if (blockEntity instanceof BaseEnergyTile) {
            BaseEnergyTile energyTile = (BaseEnergyTile) blockEntity;
            energyTile.readNbt(new ReadNbtArgs(nbt));
        }
    }

    @Override
    public void appendProperties(AppendPropertiesArgs args) {
        super.appendProperties(args);
        args.addProperty(FACING, ACTIVE);
    }

    @Override
    public BlockState rotate(RotateArgs args) {
        net.pitan76.mcpitanlib.midohra.block.BlockState state = args.getBlockState();
        Direction facing = state.get(FACING);
        return state.with(FACING, args.rotate(facing)).toMinecraft();
    }

    @Override
    public boolean isTick() {
        return true;
    }
}
