package net.pitan76.enhancedquarries.block.base;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.pitan76.enhancedquarries.block.NormalMarker;
import net.pitan76.enhancedquarries.event.BlockStatePos;
import net.pitan76.enhancedquarries.tile.base.ScannerTile;
import net.pitan76.mcpitanlib.api.block.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.block.CompatibleMaterial;
import net.pitan76.mcpitanlib.api.event.block.BlockPlacedEvent;
import net.pitan76.mcpitanlib.api.event.block.ItemScattererUtil;
import net.pitan76.mcpitanlib.api.event.block.StateReplacedEvent;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Scanner extends BaseBlock {

    public static CompatibleBlockSettings defaultSettings = CompatibleBlockSettings
            .of(CompatibleMaterial.METAL)
            .requiresTool()
            .strength(2, 8);

    public Scanner() {
        super(defaultSettings);
    }

    @Override
    public void onStateReplaced(StateReplacedEvent e) {
        if (e.state.getBlock() != e.newState.getBlock()) {
            BlockEntity blockEntity = e.world.getBlockEntity(e.pos);
            if (blockEntity instanceof ScannerTile) {
                ScannerTile scanner = (ScannerTile)blockEntity;
                if (scanner.keepNbtOnDrop) {
                    super.onStateReplaced(e);
                    return;
                }

                ItemScattererUtil.spawn(e.world, e.pos, (BlockEntity) scanner);
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

        BlockState state = (WorldUtil.getBlockState(world, pos) == null) ? fstate : WorldUtil.getBlockState(world, pos);

        if (WorldUtil.isClient(world)) return;
        if (!(e.getBlockEntity() instanceof ScannerTile)) return;

        ScannerTile scannerTile = (ScannerTile) e.getBlockEntity();
        Objects.requireNonNull(scannerTile).init();
        if (!scannerTile.canSetPosByMarker()) return;

        BlockPos markerPos = null;

        switch (getFacing(state)) {
            case NORTH:
                markerPos = pos.add(0, 0, 1);
                break;
            case SOUTH:
                markerPos = pos.add(0, 0, -1);
                break;
            case WEST:
                markerPos = pos.add(1, 0, 0);
                break;
            case EAST:
                markerPos = pos.add(-1, 0, 0);
                break;
        }

        if (markerPos == null) return;
        if (WorldUtil.getBlockState(world, markerPos).getBlock() instanceof NormalMarker) {
            BlockState markerState = WorldUtil.getBlockState(world, markerPos);

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
                WorldUtil.breakBlock(world, markerSP.getBlockPos(), true);
            }
            if (markerList.size() <= 2) return;
            scannerTile.setPos1(PosUtil.flooredBlockPos(minPosX, minPosY, minPosZ));
            scannerTile.setPos2(PosUtil.flooredBlockPos(maxPosX, maxPosY, maxPosZ));
        }
    }
}
