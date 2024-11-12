package net.pitan76.enhancedquarries.block;

import net.minecraft.block.entity.BlockEntity;
import net.pitan76.enhancedquarries.block.base.Scanner;
import net.pitan76.enhancedquarries.item.WrenchItem;
import net.pitan76.enhancedquarries.tile.NormalScannerTile;
import net.pitan76.enhancedquarries.tile.base.ScannerTile;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.block.BlockUseEvent;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.util.CompatActionResult;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;

public class NormalScanner extends Scanner {

    public NormalScanner() {
        super();
    }

    public NormalScanner(CompatIdentifier id) {
        super(id);
    }

    public NormalScanner(CompatibleBlockSettings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent e) {
        return new NormalScannerTile(e);
    }

    @Override
    public CompatActionResult onRightClick(BlockUseEvent e) {
        Player player = e.getPlayer();

        if (e.isClient()) return e.success();
        if (e.stack.getItem() instanceof WrenchItem) return e.pass();

        BlockEntity blockEntity = e.getBlockEntity();
        if (blockEntity instanceof ScannerTile)
            player.openGuiScreen((ScannerTile) blockEntity);

        return e.consume();
    }
}
