package ml.pkom.enhancedquarries.block;

import ml.pkom.enhancedquarries.block.base.Filler;
import ml.pkom.enhancedquarries.event.TileCreateEvent;
import ml.pkom.enhancedquarries.screen.FillerScreenHandler;
import ml.pkom.enhancedquarries.tile.NormalFillerTile;
import ml.pkom.enhancedquarries.tile.base.FillerTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class NormalFiller extends Filler {

    public NormalFiller() {
        super();
    }

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new NormalFillerTile(event);
    }

    // instance
    public static Filler INSTANCE = new NormalFiller();

    public static Filler getInstance() {
        return INSTANCE;
    }

    public static Filler getFiller() {
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
        FillerTile fillerTile = (FillerTile) world.getBlockEntity(pos);
        return new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> new FillerScreenHandler(i, playerInventory, fillerTile.getInventory(), fillerTile.getCraftingInventory()), new LiteralText(""));
    }
}
