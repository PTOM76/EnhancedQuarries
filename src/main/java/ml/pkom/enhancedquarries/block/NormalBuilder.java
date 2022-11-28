package ml.pkom.enhancedquarries.block;

import ml.pkom.enhancedquarries.block.base.Builder;
import ml.pkom.enhancedquarries.event.TileCreateEvent;
import ml.pkom.enhancedquarries.screen.BuilderScreenHandler;
import ml.pkom.enhancedquarries.tile.NormalBuilderTile;
import ml.pkom.enhancedquarries.tile.base.BuilderTile;
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

public class NormalBuilder extends Builder {

    public NormalBuilder() {
        super();
    }

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new NormalBuilderTile(event);
    }

    // instance
    public static Builder INSTANCE = new NormalBuilder();

    public static Builder getInstance() {
        return INSTANCE;
    }

    public static Builder getBuilder() {
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
        BuilderTile builderTile = (BuilderTile) world.getBlockEntity(pos);
        return new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> new BuilderScreenHandler(i, playerInventory, builderTile.getInventory(), builderTile.getCraftingInventory()), TextUtil.literal(""));
    }
}
