package net.pitan76.enhancedquarries.block.base;

import ml.pkom.mcpitanlibarch.api.block.CompatibleBlockSettings;
import ml.pkom.mcpitanlibarch.api.block.ExtendBlock;
import ml.pkom.mcpitanlibarch.api.block.ExtendBlockEntityProvider;
import ml.pkom.mcpitanlibarch.api.event.block.BlockBreakEvent;
import ml.pkom.mcpitanlibarch.api.event.block.BlockPlacedEvent;
import ml.pkom.mcpitanlibarch.api.event.block.PickStackEvent;
import ml.pkom.mcpitanlibarch.api.event.block.result.BlockBreakResult;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pitan76.enhancedquarries.tile.base.BaseEnergyTile;
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
    public void onPlaced(BlockPlacedEvent e) {
        super.onPlaced(e);
        if (e.placer != null) {
            setFacing(e.placer.getHorizontalFacing().getOpposite(), e.world, e.pos);
        }
        if (!e.stack.hasNbt()) return;
        if (!e.stack.getNbt().contains("BlockEntityTag")) return;

        NbtCompound nbt = e.stack.getSubNbt("BlockEntityTag");
        BlockEntity blockEntity = e.world.getBlockEntity(e.pos);
        if (blockEntity instanceof BaseEnergyTile) {
            BaseEnergyTile energyTile = (BaseEnergyTile) blockEntity;
            energyTile.readNbtOverride(nbt);
        }
    }

    /*
    @Override
    public ItemStack getPickStack(PickStackEvent e) {
        ItemStack stack = super.getPickStack(e);
        BlockEntity blockEntity = null;
        if (e.getBlockView() != null) {
            blockEntity = e.getBlockView().getBlockEntity(e.getPos());
        } else if (e.getBlockView() != null) {
            blockEntity = e.getBlockView().getBlockEntity(e.getPos());
        }

        if (! (blockEntity instanceof BaseEnergyTile)) return stack;

        BaseEnergyTile energyTile = (BaseEnergyTile) blockEntity;
        if (! energyTile.keepNbtOnDrop) return stack;
        NbtCompound nbt = new NbtCompound();
        energyTile.writeNbtOverride(nbt);
        stack.setSubNbt("BlockEntityTag", nbt);
        return stack;
    }
     */

    @Override
    public BlockBreakResult onBreak(BlockBreakEvent e) {
        BlockEntity blockEntity = e.getWorld().getBlockEntity(e.getPos());
        if (! (blockEntity instanceof BaseEnergyTile)) return super.onBreak(e);

        BaseEnergyTile energyTile = (BaseEnergyTile) blockEntity;
        if (! energyTile.keepNbtOnDrop) return super.onBreak(e);

        ItemStack stack = new ItemStack(this);
        NbtCompound nbt = new NbtCompound();
        energyTile.writeNbtOverride(nbt);
        stack.setSubNbt("BlockEntityTag", nbt);

        ItemEntity itemEntity = new ItemEntity(e.world, e.pos.getX() + 0.5D, e.pos.getY() + 0.5D, e.pos.getZ() + 0.5D, stack);
        itemEntity.setToDefaultPickupDelay();
        e.world.spawnEntity(itemEntity);

        return super.onBreak(e);
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
