package net.pitan76.enhancedquarries.block;

import net.minecraft.block.entity.BlockEntity;
import net.pitan76.enhancedquarries.block.base.Filler;
import net.pitan76.enhancedquarries.item.WrenchItem;
import net.pitan76.enhancedquarries.tile.EnhancedFillerWithChestTile;
import net.pitan76.enhancedquarries.tile.base.FillerTile;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.block.BlockUseEvent;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.util.CompatActionResult;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;

public class EnhancedFillerWithChest extends Filler {

    public EnhancedFillerWithChest() {
        this(CompatIdentifier.of("enhanced_filler_with_chest"));
    }

    public EnhancedFillerWithChest(CompatIdentifier id) {
        super(id);
    }

    public EnhancedFillerWithChest(CompatibleBlockSettings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new EnhancedFillerWithChestTile(event);
    }

    // ----

    @Override
    public CompatActionResult onRightClick(BlockUseEvent e) {
        Player player = e.getPlayer();

        if (e.isClient()) return e.success();
        if (e.stack.getItem() instanceof WrenchItem) return e.pass();

        BlockEntity blockEntity = e.getBlockEntity();
        if (blockEntity instanceof FillerTile)
            player.openGuiScreen((FillerTile) blockEntity);

        return e.consume();
    }
}
