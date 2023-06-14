package ml.pkom.enhancedquarries.block.base;

import ml.pkom.enhancedquarries.block.NormalMarker;
import ml.pkom.enhancedquarries.event.BlockStatePos;
import ml.pkom.mcpitanlibarch.api.block.CompatibleBlockSettings;
import ml.pkom.mcpitanlibarch.api.block.CompatibleMaterial;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import ml.pkom.enhancedquarries.tile.base.ScannerTile;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import reborncore.api.blockentity.IMachineGuiHandler;
import reborncore.common.blocks.BlockMachineBase;

import java.util.ArrayList;
import java.util.List;

public abstract class Scanner extends BlockMachineBase implements BlockEntityProvider {

    public static CompatibleBlockSettings defaultSettings = CompatibleBlockSettings
            .of(CompatibleMaterial.METAL)
            .requiresTool()
            //.breakByTool(FabricToolTags.PICKAXES, 0)
            .strength(2, 8);

    public Scanner() {
        super(defaultSettings.build());
    }

    // 1.17.1へのポート用
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return createBlockEntity(new TileCreateEvent(pos, state));
    }

    public BlockEntity createBlockEntity(BlockView world) {
        return createBlockEntity(new TileCreateEvent(world));
    }

    public abstract BlockEntity createBlockEntity(TileCreateEvent event);

    // TechReborn
    public IMachineGuiHandler getGui() {
        return null;
    }

    public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockHitResult hitResult) {
        // ここでGUIを開けないように無効化しておく
        return ActionResult.PASS;
    }

    // もし、TRを使ったGUIをつくる機会のために関数をつくっておく
    public ActionResult onUseTR(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockHitResult hitResult) {
        return super.onUse(state, worldIn, pos, playerIn, hand, hitResult);
    }
    // ----

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ScannerTile) {
                ScannerTile scanner = (ScannerTile)blockEntity;
                ItemScatterer.spawn(world, pos, scanner.getInventory());
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public void onPlaced(World worldIn, BlockPos pos, BlockState fstate, LivingEntity placer, ItemStack stack) {
        super.onPlaced(worldIn, pos, fstate, placer, stack);
        BlockState state;
        state = (worldIn.getBlockState(pos) == null) ? fstate : worldIn.getBlockState(pos);
        if (worldIn == null || worldIn.isClient()) return;
        if (worldIn.getBlockEntity(pos) instanceof ScannerTile) {
            ScannerTile scannerTile = (ScannerTile) worldIn.getBlockEntity(pos);
            scannerTile.init();
            if (scannerTile.canSetPosByMarker()) {
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
                    if ((maxPosX == null || maxPosY == null || maxPosZ == null || minPosX == null || minPosY == null || minPosZ == null) || markerList.size() <= 2 ) return;
                    scannerTile.setPos1(new BlockPos(minPosX, minPosY, minPosZ));
                    scannerTile.setPos2(new BlockPos(maxPosX, maxPosY, maxPosZ));
                }
            }
        }
    }
}
