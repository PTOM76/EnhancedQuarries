package net.pitan76.enhancedquarries.block.base;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pitan76.enhancedquarries.tile.base.BaseEnergyTile;
import net.pitan76.mcpitanlib.api.block.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.block.ExtendBlock;
import net.pitan76.mcpitanlib.api.block.ExtendBlockEntityProvider;
import net.pitan76.mcpitanlib.api.event.block.AppendPropertiesArgs;
import net.pitan76.mcpitanlib.api.event.block.BlockPlacedEvent;
import net.pitan76.mcpitanlib.api.event.nbt.ReadNbtArgs;
import net.pitan76.mcpitanlib.api.util.CustomDataUtil;
import org.jetbrains.annotations.Nullable;

public class BaseBlock extends ExtendBlock implements ExtendBlockEntityProvider {

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");

    public BaseBlock(CompatibleBlockSettings settings) {
        super(settings);
        setNewDefaultState(getNewDefaultState().with(ACTIVE, false).with(FACING, Direction.NORTH));
    }

    public static void setFacing(Direction facing, World world, BlockPos pos) {
        world.setBlockState(pos, world.getBlockState(pos).with(FACING, facing));
    }

    public static Direction getFacing(BlockState state) {
        return state.get(FACING);
    }

    public static void setActive(Boolean active, World world, BlockPos pos) {
        Direction facing = world.getBlockState(pos).get(FACING);
        BlockState state = world.getBlockState(pos).with(ACTIVE, active).with(FACING, facing);
        world.setBlockState(pos, state, 3);
    }

    public static boolean isActive(BlockState state) {
        return state.get(ACTIVE);
    }

    @Override
    public void onPlaced(BlockPlacedEvent e) {
        super.onPlaced(e);
        if (e.placer != null) {
            setFacing(e.placer.getHorizontalFacing().getOpposite(), e.world, e.pos);
        }
        if (!CustomDataUtil.hasNbt(e.stack)) return;
        if (!CustomDataUtil.getNbt(e.stack).contains("BlockEntityTag")) return;

        NbtCompound nbt = CustomDataUtil.get(e.stack, "BlockEntityTag");
        BlockEntity blockEntity = e.world.getBlockEntity(e.pos);
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
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return ((world1, pos, state1, blockEntity) -> {
            if (blockEntity instanceof BaseEnergyTile) {
                BaseEnergyTile baseEnergyTile = (BaseEnergyTile) blockEntity;
                baseEnergyTile.tick(world1, pos, state1, baseEnergyTile);
            }
        });
    }
}
