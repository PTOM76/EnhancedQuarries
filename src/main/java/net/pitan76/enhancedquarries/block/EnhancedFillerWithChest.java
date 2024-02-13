package net.pitan76.enhancedquarries.block;

import ml.pkom.mcpitanlibarch.api.entity.Player;
import ml.pkom.mcpitanlibarch.api.event.block.BlockUseEvent;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.pitan76.enhancedquarries.block.base.Filler;
import net.pitan76.enhancedquarries.item.WrenchItem;
import net.pitan76.enhancedquarries.tile.EnhancedFillerWithChestTile;
import net.pitan76.enhancedquarries.tile.base.FillerTile;

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
        World world = e.getWorld();
        Player player = e.getPlayer();
        BlockPos pos = e.getPos();

        if (world.isClient()) return e.success();
        if (e.stack.getItem() instanceof WrenchItem) return e.pass();

        if (world.getBlockEntity(pos) instanceof FillerTile)
            player.openGuiScreen((FillerTile) world.getBlockEntity(pos));

        return e.consume();
    }
}
