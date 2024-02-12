package net.pitan76.enhancedquarries.block;

import net.pitan76.enhancedquarries.block.base.Filler;
import net.pitan76.enhancedquarries.screen.FillerWithChestScreenHandler;
import net.pitan76.enhancedquarries.tile.EnhancedFillerWithChestTile;
import net.pitan76.enhancedquarries.tile.base.FillerTile;
import ml.pkom.mcpitanlibarch.api.event.block.BlockUseEvent;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import ml.pkom.mcpitanlibarch.api.util.TextUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class EnhancedFillerWithChest extends Filler {

    public EnhancedFillerWithChest() {
        super();
    }

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new EnhancedFillerWithChestTile(event);
    }

    // instance
    public static Filler INSTANCE = new EnhancedFillerWithChest();

    public static Filler getInstance() {
        return INSTANCE;
    }

    public static Filler getFiller() {
        return getInstance();
    }
    // ----


    @Override
    public ActionResult onRightClick(BlockUseEvent e) {
        if (e.isClient()) return ActionResult.SUCCESS;
        NamedScreenHandlerFactory namedScreenHandlerFactory = createScreenHandlerFactory(e.state, e.world, e.pos);
        if (namedScreenHandlerFactory != null)
            e.player.openGuiScreen(namedScreenHandlerFactory);

        return ActionResult.CONSUME;
    }

    @Nullable
    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        FillerTile fillerTile = (FillerTile) world.getBlockEntity(pos);
        return new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> new FillerWithChestScreenHandler(i, playerInventory, fillerTile.inventory, fillerTile.getCraftingInventory()), TextUtil.literal(""));
    }
}
