package net.pitan76.enhancedquarries.block.base;

import net.minecraft.block.entity.BlockEntity;
import net.pitan76.enhancedquarries.Items;
import net.pitan76.enhancedquarries.block.NormalMarker;
import net.pitan76.enhancedquarries.event.v2.BlockStatePos;
import net.pitan76.enhancedquarries.tile.base.FillerTile;
import net.pitan76.mcpitanlib.api.block.CompatibleMaterial;
import net.pitan76.mcpitanlib.api.block.v2.BlockSettingsBuilder;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.event.block.BlockBreakEvent;
import net.pitan76.mcpitanlib.api.event.block.BlockPlacedEvent;
import net.pitan76.mcpitanlib.api.event.block.ItemScattererUtil;
import net.pitan76.mcpitanlib.api.event.block.StateReplacedEvent;
import net.pitan76.mcpitanlib.api.event.block.result.BlockBreakResult;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.mcpitanlib.api.util.entity.ItemEntityUtil;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;
import net.pitan76.mcpitanlib.midohra.block.BlockState;
import net.pitan76.mcpitanlib.midohra.util.math.BlockPos;
import net.pitan76.mcpitanlib.midohra.util.math.Direction;
import net.pitan76.mcpitanlib.midohra.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Filler extends BaseBlock {

    public static BlockSettingsBuilder defaultSettings = new BlockSettingsBuilder()
            .material(CompatibleMaterial.METAL)
            .requiresTool()
            .strength(2, 8);

    public Filler(CompatibleBlockSettings settings) {
        super(settings);
    }

    public Filler(CompatIdentifier id) {
        this(defaultSettings.build(id));
    }

    @Override
    public void onStateReplaced(StateReplacedEvent e) {
        if (e.state == null || e.newState == null) return;

        if (e.isSameState()) {
            super.onStateReplaced(e);
            return;
        }

        BlockEntity blockEntity = e.getBlockEntity();
        if (blockEntity instanceof FillerTile) {
            FillerTile filler = (FillerTile) blockEntity;
            if (filler.keepNbtOnDrop) {
                super.onStateReplaced(e);
                return;
            }

            // モジュールの返却
            if (filler.canBedrockBreak())
                ItemEntityUtil.createWithSpawn(e.world, ItemStackUtil.create(Items.BEDROCK_BREAK_MODULE, 1), e.pos);

            filler.getCraftingInventory().set(9, ItemStackUtil.empty());
            ItemScattererUtil.spawn(e.world, e.pos, filler.getAllStacks());
            filler.getAllStacks().clear();
            //InventoryUtil.setStack(filler.getCraftingInventory(), 9, ItemStackUtil.empty());
            //ItemScattererUtil.spawn(e.world, e.pos, filler.getCraftingInventory());
        }

        super.onStateReplaced(e);
    }

    public BlockBreakResult onBreak(BlockBreakEvent e) {
        World world = e.getMidohraWorld();
        BlockPos pos = e.getMidohraPos();
        BlockState state = e.getMidohraState();

        if (state.isEmpty()) return super.onBreak(e);

        BlockEntity blockEntity = e.getBlockEntity();
        if (blockEntity instanceof FillerTile) {
            FillerTile filler = (FillerTile) blockEntity;
            if (filler.keepNbtOnDrop) {
                return super.onBreak(e);
            }

            // モジュールの返却
            if (filler.canBedrockBreak())
                ItemEntityUtil.createWithSpawn(e.world, ItemStackUtil.create(Items.BEDROCK_BREAK_MODULE, 1), e.pos);

            filler.getCraftingInventory().set(9, ItemStackUtil.empty());
            ItemScattererUtil.spawn(world, pos, filler.getAllStacks());
            filler.getAllStacks().clear();
        }

        return super.onBreak(e);
    }

    @Override
    public void onPlaced(BlockPlacedEvent e) {
        super.onPlaced(e);
        World world = e.getMidohraWorld();
        BlockPos pos = e.getMidohraPos();
        BlockState fstate = e.getMidohraState();

        BlockState state;
        state = (world.getBlockState(pos) == null) ? fstate : world.getBlockState(pos);
        if (e.isClient()) return;

        BlockEntity blockEntity = e.getBlockEntity();
        if (blockEntity instanceof FillerTile) {
            FillerTile fillerTile = (FillerTile) blockEntity;
            Objects.requireNonNull(fillerTile).init();
            if (fillerTile.canSetPosByMarker()) {
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

                    if (markerList.size() <= 2) return;

                    fillerTile.setPos1(PosUtil.flooredMidohraBlockPos(minPosX, minPosY, minPosZ));
                    fillerTile.setPos2(PosUtil.flooredMidohraBlockPos(maxPosX, maxPosY, maxPosZ));
                }
            }
        }
    }
}
