package ml.pkom.enhancedquarries.block.base;

import ml.pkom.enhancedquarries.Items;
import ml.pkom.enhancedquarries.block.Frame;
import ml.pkom.enhancedquarries.block.NormalMarker;
import ml.pkom.enhancedquarries.event.BlockStatePos;
import ml.pkom.enhancedquarries.tile.base.QuarryTile;
import ml.pkom.mcpitanlibarch.api.block.CompatibleBlockSettings;
import ml.pkom.mcpitanlibarch.api.block.CompatibleMaterial;
import ml.pkom.mcpitanlibarch.api.event.block.BlockPlacedEvent;
import ml.pkom.mcpitanlibarch.api.event.block.StateReplacedEvent;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Quarry extends BaseBlock {

    public static CompatibleBlockSettings defaultSettings = CompatibleBlockSettings
            .of(CompatibleMaterial.METAL)
            .requiresTool()
            .strength(2, 8);

    public Quarry(CompatibleBlockSettings settings) {
        super(settings);
    }

    public Quarry() {
        this(defaultSettings);
    }

    @Override
    public void onStateReplaced(StateReplacedEvent e) {
        World world = e.world;
        BlockPos pos = e.pos;
        BlockState state = e.state;

        if (state.getBlock() != e.newState.getBlock() && state.getBlock() != null && e.newState.getBlock() != null) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof QuarryTile) {
                QuarryTile quarry = (QuarryTile)blockEntity;
                ItemScatterer.spawn(world, pos, (QuarryTile)blockEntity);

                // モジュールの返却
                if (quarry.canBedrockBreak()) {
                    world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.BEDROCK_BREAK_MODULE, 1)));
                }
                if (quarry.isSetLuck()) {
                    world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.LUCK_MODULE, 1)));
                }
                if (quarry.isSetSilkTouch()) {
                    world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.SILK_TOUCH_MODULE, 1)));
                }
                if (quarry.isSetMobDelete()) {
                    world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.MOB_DELETE_MODULE, 1)));
                }
                if (quarry.isSetMobKill()) {
                    world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.MOB_KILL_MODULE, 1)));
                }

                // フレーム破壊
                BlockPos framePos = null;
                if (getFacing(state).equals(Direction.NORTH))
                    framePos = pos.add(0, 0, 1);
                if (getFacing(state).equals(Direction.SOUTH))
                    framePos = pos.add(0, 0, -1);
                if (getFacing(state).equals(Direction.WEST))
                    framePos = pos.add(1, 0, 0);
                if (getFacing(state).equals(Direction.EAST))
                    framePos = pos.add(-1, 0, 0);
                if (framePos != null)
                    if (world.getBlockState(framePos).getBlock() instanceof Frame) {
                        Frame.breakConnectFrames(world, framePos);
                    }
            }
            super.onStateReplaced(e);
        }
    }

    @Override
    public void onPlaced(BlockPlacedEvent e) {
        super.onPlaced(e);
        World world = e.world;
        BlockPos pos = e.pos;
        BlockState fstate = e.state;

        BlockState state;
        state = (world.getBlockState(pos) == null) ? fstate : world.getBlockState(pos);
        if (world.isClient()) return;
        if (world.getBlockEntity(pos) instanceof QuarryTile) {
            QuarryTile quarryTile = (QuarryTile) world.getBlockEntity(pos);
            Objects.requireNonNull(quarryTile).init();
            if (quarryTile.canSetPosByMarker()) {
                BlockPos markerPos = null;
                if (getFacing(state).equals(Direction.NORTH))
                    markerPos = pos.add(0, 0, 1);
                if (getFacing(state).equals(Direction.SOUTH))
                    markerPos = pos.add(0, 0, -1);
                if (getFacing(state).equals(Direction.WEST))
                    markerPos = pos.add(1, 0, 0);
                if (getFacing(state).equals(Direction.EAST))
                    markerPos = pos.add(-1, 0, 0);
                if (markerPos == null) return;
                if (world.getBlockState(markerPos).getBlock() instanceof NormalMarker) {
                    BlockState markerState = world.getBlockState(markerPos);

                    List<BlockStatePos> markerList = new ArrayList<>();
                    markerList.add(new BlockStatePos(markerState, markerPos, world));
                    NormalMarker.searchMarker(world, markerPos, markerList);

                    Integer maxPosX = null, maxPosY = null, maxPosZ = null;
                    Integer minPosX = null, minPosY = null, minPosZ = null;

                    for (BlockStatePos markerSP : markerList) {
                        if (maxPosX == null || markerSP.getPosX() > maxPosX) maxPosX = markerSP.getPosX();
                        if (maxPosY == null || markerSP.getPosY() > maxPosY) maxPosY = markerSP.getPosY();
                        if (maxPosZ == null || markerSP.getPosZ() > maxPosZ) maxPosZ = markerSP.getPosZ();
                        if (minPosX == null || markerSP.getPosX() < minPosX) minPosX = markerSP.getPosX();
                        if (minPosY == null || markerSP.getPosY() < minPosY) minPosY = markerSP.getPosY();
                        if (minPosZ == null || markerSP.getPosZ() < minPosZ) minPosZ = markerSP.getPosZ();
                        world.breakBlock(markerSP.getBlockPos(), true);
                    }
                    if (markerList.size() <= 2 ) return;
                    if (maxPosY.equals(minPosY)) maxPosY += 4;

                    quarryTile.setPos1(new BlockPos(minPosX, minPosY, minPosZ));
                    quarryTile.setPos2(new BlockPos(maxPosX + 1, maxPosY, maxPosZ + 1));
                }
            }
        }
    }
}
