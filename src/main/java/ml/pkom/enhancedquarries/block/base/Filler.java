package ml.pkom.enhancedquarries.block.base;

import ml.pkom.enhancedquarries.Items;
import ml.pkom.enhancedquarries.block.NormalMarker;
import ml.pkom.enhancedquarries.event.BlockStatePos;
import ml.pkom.enhancedquarries.tile.base.FillerTile;
import ml.pkom.mcpitanlibarch.api.block.CompatibleBlockSettings;
import ml.pkom.mcpitanlibarch.api.block.CompatibleMaterial;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Filler extends BaseBlock {

    public static CompatibleBlockSettings defaultSettings = CompatibleBlockSettings
            .of(CompatibleMaterial.METAL)
            .requiresTool()
            .strength(2, 8);

    public Filler(CompatibleBlockSettings settings) {
        super(settings);
    }

    public Filler() {
        this(defaultSettings);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof FillerTile) {
                FillerTile filler = (FillerTile)blockEntity;
                ItemScatterer.spawn(world, pos, filler.inventory);
                filler.getCraftingInventory().setStack(9, ItemStack.EMPTY);
                ItemScatterer.spawn(world, pos, filler.getCraftingInventory());

                // モジュールの返却
                if (filler.canBedrockBreak()) {
                    world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.BEDROCK_BREAK_MODULE, 1)));
                }
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public void onPlaced(World worldIn, BlockPos pos, BlockState fstate, LivingEntity placer, ItemStack stack) {
        super.onPlaced(worldIn, pos, fstate, placer, stack);
        BlockState state;
        state = (worldIn.getBlockState(pos) == null) ? fstate : worldIn.getBlockState(pos);
        if (worldIn.isClient()) return;
        if (worldIn.getBlockEntity(pos) instanceof FillerTile) {
            FillerTile fillerTile = (FillerTile) worldIn.getBlockEntity(pos);
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
                if (worldIn.getBlockState(markerPos).getBlock() instanceof NormalMarker) {
                    BlockState markerState = worldIn.getBlockState(markerPos);

                    List<BlockStatePos> markerList = new ArrayList<>();
                    markerList.add(new BlockStatePos(markerState, markerPos, worldIn));
                    NormalMarker.searchMarker(worldIn, markerPos, markerList);

                    Integer maxPosX = null, maxPosY = null, maxPosZ = null;
                    Integer minPosX = null, minPosY = null, minPosZ = null;

                    for (BlockStatePos markerSP : markerList) {
                        if (maxPosX == null || markerSP.getPosX() > maxPosX) maxPosX = markerSP.getPosX();
                        if (maxPosY == null || markerSP.getPosY() > maxPosY) maxPosY = markerSP.getPosY();
                        if (maxPosZ == null || markerSP.getPosZ() > maxPosZ) maxPosZ = markerSP.getPosZ();
                        if (minPosX == null || markerSP.getPosX() < minPosX) minPosX = markerSP.getPosX();
                        if (minPosY == null || markerSP.getPosY() < minPosY) minPosY = markerSP.getPosY();
                        if (minPosZ == null || markerSP.getPosZ() < minPosZ) minPosZ = markerSP.getPosZ();
                        worldIn.breakBlock(markerSP.getBlockPos(), true);
                    }
                    if (markerList.size() <= 2) return;
                    fillerTile.setPos1(new BlockPos(minPosX, minPosY, minPosZ));
                    fillerTile.setPos2(new BlockPos(maxPosX, maxPosY, maxPosZ));
                }
            }
        }
    }
}
