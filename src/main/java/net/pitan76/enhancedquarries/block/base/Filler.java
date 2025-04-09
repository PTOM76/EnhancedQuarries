package net.pitan76.enhancedquarries.block.base;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.pitan76.enhancedquarries.Items;
import net.pitan76.enhancedquarries.block.NormalMarker;
import net.pitan76.enhancedquarries.event.v2.BlockStatePos;
import net.pitan76.enhancedquarries.tile.base.FillerTile;
import net.pitan76.mcpitanlib.api.block.CompatibleMaterial;
import net.pitan76.mcpitanlib.api.block.v2.BlockSettingsBuilder;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.event.block.BlockPlacedEvent;
import net.pitan76.mcpitanlib.api.event.block.ItemScattererUtil;
import net.pitan76.mcpitanlib.api.event.block.StateReplacedEvent;
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
        if (e.state.getBlock() != e.newState.getBlock()) {
            BlockEntity blockEntity = e.getBlockEntity();
            if (blockEntity instanceof FillerTile) {
                FillerTile filler = (FillerTile) blockEntity;
                if (filler.keepNbtOnDrop) {
                    super.onStateReplaced(e);
                    return;
                }

                ItemScattererUtil.spawn(e.world, e.pos, filler.inventory);
                filler.getCraftingInventory().setStack(9, ItemStackUtil.empty());
                ItemScattererUtil.spawn(e.world, e.pos, filler.getCraftingInventory());

                // モジュールの返却
                if (filler.canBedrockBreak()) {
                    ItemEntity itemEntity = ItemEntityUtil.create(e.world, PosUtil.x(e.pos), PosUtil.y(e.pos), PosUtil.z(e.pos), ItemStackUtil.create(Items.BEDROCK_BREAK_MODULE, 1));
                    WorldUtil.spawnEntity(e.world, itemEntity);
                }
            }
            super.onStateReplaced(e);
        }
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
                    fillerTile.setPos1(PosUtil.flooredBlockPos(minPosX, minPosY, minPosZ));
                    fillerTile.setPos2(PosUtil.flooredBlockPos(maxPosX, maxPosY, maxPosZ));
                }
            }
        }
    }
}
