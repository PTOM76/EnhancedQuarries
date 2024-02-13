package net.pitan76.enhancedquarries.block;

import net.pitan76.enhancedquarries.block.base.Scanner;
import net.pitan76.enhancedquarries.tile.NormalScannerTile;
import net.pitan76.enhancedquarries.tile.base.ScannerTile;
import ml.pkom.mcpitanlibarch.api.entity.Player;
import ml.pkom.mcpitanlibarch.api.event.block.BlockUseEvent;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
    public ActionResult onRightClick(BlockUseEvent e) {
        World world = e.getWorld();
        Player player = e.getPlayer();
        BlockPos pos = e.getPos();

        if (world.isClient()) return e.success();

        if (world.getBlockEntity(pos) instanceof ScannerTile)
            player.openGuiScreen((ScannerTile) world.getBlockEntity(pos));

        return e.consume();
    }
}
