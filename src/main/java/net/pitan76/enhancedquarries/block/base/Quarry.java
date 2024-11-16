package net.pitan76.enhancedquarries.block.base;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.pitan76.enhancedquarries.EnhancedQuarries;
import net.pitan76.enhancedquarries.block.Frame;
import net.pitan76.enhancedquarries.block.NormalMarker;
import net.pitan76.enhancedquarries.event.v2.BlockStatePos;
import net.pitan76.enhancedquarries.tile.base.QuarryTile;
import net.pitan76.mcpitanlib.api.block.CompatibleMaterial;
import net.pitan76.mcpitanlib.api.block.v2.BlockSettingsBuilder;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.event.block.BlockPlacedEvent;
import net.pitan76.mcpitanlib.api.event.block.BlockUseEvent;
import net.pitan76.mcpitanlib.api.event.block.ItemScattererUtil;
import net.pitan76.mcpitanlib.api.event.block.StateReplacedEvent;
import net.pitan76.mcpitanlib.api.util.CompatActionResult;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import net.pitan76.mcpitanlib.api.util.entity.ItemEntityUtil;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;
import net.pitan76.mcpitanlib.midohra.block.BlockState;
import net.pitan76.mcpitanlib.midohra.util.math.BlockPos;
import net.pitan76.mcpitanlib.midohra.util.math.Direction;
import net.pitan76.mcpitanlib.midohra.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Quarry extends BaseBlock {

    public static BlockSettingsBuilder defaultSettings = new BlockSettingsBuilder()
            .material(CompatibleMaterial.METAL)
            .requiresTool()
            .strength(2, 8);

    public Quarry(CompatibleBlockSettings settings) {
        super(settings);
    }

    public Quarry(CompatIdentifier id) {
        super(defaultSettings.build(id));
    }

    public Quarry() {
        this(EnhancedQuarries._id("normal_quarry"));
    }

    @Override
    public CompatActionResult onRightClick(BlockUseEvent e) {
        if (e.isSneaking())
            return e.pass();

        ItemStack stack = e.player.getMainHandStack();
        if (stack != null && stack.getItem() == net.minecraft.item.Items.GLASS_BOTTLE) {
            if (e.isClient()) return e.success();
            if (e.getBlockEntity() instanceof QuarryTile) {
                QuarryTile quarry = (QuarryTile) e.getBlockEntity();
                if (quarry.getStoredExp() >= 4) {
                    e.player.giveStack(ItemStackUtil.create(net.minecraft.item.Items.EXPERIENCE_BOTTLE, 1));
                    ItemStackUtil.decrementCount(stack, 1);
                    quarry.removeStoredExp(4);
                    return e.success();
                }
                return e.pass();
            }
        }

        return e.pass();
    }

    @Override
    public void onStateReplaced(StateReplacedEvent e) {
        World world = e.getMidohraWorld();
        BlockPos pos = e.getMidohraPos();
        BlockState state = e.getMidohraState();

        if (state == null) return;
        if (state.getBlock().get() == e.newState.getBlock()) {
            super.onStateReplaced(e);
            return;
        }

        BlockEntity blockEntity = e.getBlockEntity();
        if (blockEntity instanceof QuarryTile) {
            QuarryTile quarry = (QuarryTile) blockEntity;
            if (quarry.keepNbtOnDrop) {
                super.onStateReplaced(e);
                return;
            }

            ItemScattererUtil.spawn(world.getRaw(), pos.toMinecraft(), blockEntity);

            // モジュールの返却
            if (!quarry.isEmptyInModules()) {
                for (ItemStack module : quarry.getModuleStacks()) {
                    ItemEntity itemEntity = ItemEntityUtil.create(world.getRaw(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, module);
                    ItemEntityUtil.setToDefaultPickupDelay(itemEntity);
                    WorldUtil.spawnEntity(world.getRaw(), itemEntity);
                }
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
                if (world.getBlockState(framePos).getBlock().get() instanceof Frame) {
                    Frame.breakConnectFrames(world, framePos);
                }
        }
        super.onStateReplaced(e);
    }

    @Override
    public void onPlaced(BlockPlacedEvent e) {
        super.onPlaced(e);
        World world = World.of(e.world);
        BlockPos pos = BlockPos.of(e.pos);
        BlockState fstate = BlockState.of(e.state);

        BlockState state;
        state = (world.getBlockState(pos) == null) ? fstate : world.getBlockState(pos);
        if (e.isClient()) return;
        BlockEntity blockEntity = e.getBlockEntity();
        if (blockEntity instanceof QuarryTile) {
            QuarryTile quarryTile = (QuarryTile) blockEntity;
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
                if (world.getBlockState(markerPos).getBlock().get() instanceof NormalMarker) {
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

                    quarryTile.setMinPos(PosUtil.flooredBlockPos(minPosX, minPosY, minPosZ));
                    quarryTile.setMaxPos(PosUtil.flooredBlockPos(maxPosX + 1, maxPosY, maxPosZ + 1));
                }
            }
        }
    }
}
