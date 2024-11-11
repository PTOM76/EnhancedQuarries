package net.pitan76.enhancedquarries.block;

import net.pitan76.mcpitanlib.api.util.CompatActionResult;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.pitan76.enhancedquarries.block.base.Scanner;
import net.pitan76.enhancedquarries.item.WrenchItem;
import net.pitan76.enhancedquarries.tile.NormalScannerTile;
import net.pitan76.enhancedquarries.tile.base.ScannerTile;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.block.BlockUseEvent;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.util.WorldUtil;

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
    public CompatActionResult onRightClick(BlockUseEvent e) {
        World world = e.getWorld();
        Player player = e.getPlayer();
        BlockPos pos = e.getPos();

        if (WorldUtil.isClient(world)) return e.success();
        if (e.stack.getItem() instanceof WrenchItem) return e.pass();

        if (world.getBlockEntity(pos) instanceof ScannerTile)
            player.openGuiScreen((ScannerTile) world.getBlockEntity(pos));

        return e.consume();
    }
}
