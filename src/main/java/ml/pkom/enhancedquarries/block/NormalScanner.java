package ml.pkom.enhancedquarries.block;

import ml.pkom.enhancedquarries.block.base.Scanner;
import ml.pkom.enhancedquarries.event.TileCreateEvent;
import ml.pkom.enhancedquarries.screen.ScannerScreenHandler;
import ml.pkom.enhancedquarries.tile.NormalScannerTile;
import ml.pkom.enhancedquarries.tile.base.ScannerTile;
import ml.pkom.mcpitanlibarch.api.util.TextUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class NormalScanner extends Scanner {

    public NormalScanner() {
        super();
    }

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new NormalScannerTile(event);
    }

    // instance
    public static Scanner INSTANCE = new NormalScanner();

    public static Scanner getInstance() {
        return INSTANCE;
    }

    public static Scanner getScanner() {
        return getInstance();
    }
    // ----

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        if (world.isClient())
            return ActionResult.SUCCESS;
        NamedScreenHandlerFactory namedScreenHandlerFactory = createScreenHandlerFactory(state, world, pos);
        if (namedScreenHandlerFactory != null) {
            player.openHandledScreen(namedScreenHandlerFactory);
        }

        return ActionResult.CONSUME;
    }

    @Nullable
    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        ScannerTile scannerTile = (ScannerTile) world.getBlockEntity(pos);
        return new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> new ScannerScreenHandler(i, playerInventory, scannerTile.getInventory(), scannerTile.getCraftingInventory()), TextUtil.literal(""));
    }
}
