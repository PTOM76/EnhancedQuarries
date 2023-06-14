package ml.pkom.enhancedquarries.block.base;

import ml.pkom.mcpitanlibarch.api.block.CompatibleBlockSettings;
import ml.pkom.mcpitanlibarch.api.block.CompatibleMaterial;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import ml.pkom.enhancedquarries.tile.base.BuilderTile;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import reborncore.api.blockentity.IMachineGuiHandler;
import reborncore.common.blocks.BlockMachineBase;

public abstract class Builder extends BlockMachineBase implements BlockEntityProvider {

    public static CompatibleBlockSettings defaultSettings = CompatibleBlockSettings
            .of(CompatibleMaterial.METAL)
            .requiresTool()
            //.breakByTool(FabricToolTags.PICKAXES, 0)
            .strength(2, 8);

    public Builder() {
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
            if (blockEntity instanceof BuilderTile) {
                BuilderTile builder = (BuilderTile)blockEntity;
                ItemScatterer.spawn(world, pos, builder.getInventory());
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
}
