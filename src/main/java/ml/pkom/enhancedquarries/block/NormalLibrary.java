package ml.pkom.enhancedquarries.block;

import ml.pkom.enhancedquarries.block.base.Library;
import ml.pkom.enhancedquarries.event.TileCreateEvent;
import ml.pkom.enhancedquarries.screen.LibraryScreenHandler;
import ml.pkom.enhancedquarries.tile.NormalLibraryTile;
import ml.pkom.enhancedquarries.tile.base.LibraryTile;
import ml.pkom.mcpitanlib.api.text.TextUtil;
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

public class NormalLibrary extends Library {

    public NormalLibrary() {
        super();
    }

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new NormalLibraryTile(event);
    }

    // instance
    public static Library INSTANCE = new NormalLibrary();

    public static Library getInstance() {
        return INSTANCE;
    }

    public static Library getLibrary() {
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
        LibraryTile libraryTile = (LibraryTile) world.getBlockEntity(pos);
        return new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> new LibraryScreenHandler(i, playerInventory, libraryTile.getInventory(), libraryTile.getCraftingInventory()), TextUtil.literal(""));
    }
}
