package ml.pkom.enhancedquarries.block.base;

import ml.pkom.enhancedquarries.tile.base.BaseEnergyTile;
import ml.pkom.mcpitanlibarch.api.block.CompatibleBlockSettings;
import ml.pkom.mcpitanlibarch.api.block.ExtendBlock;
import ml.pkom.mcpitanlibarch.api.block.ExtendBlockEntityProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BaseBlock extends ExtendBlock implements ExtendBlockEntityProvider {

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");

    public BaseBlock(CompatibleBlockSettings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(ACTIVE, false).with(FACING, Direction.NORTH));
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
    public void onPlaced(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onPlaced(worldIn, pos, state, placer, stack);
        if(placer != null) {
            setFacing(placer.getHorizontalFacing().getOpposite(), worldIn, pos);
        }
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, ACTIVE);
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
