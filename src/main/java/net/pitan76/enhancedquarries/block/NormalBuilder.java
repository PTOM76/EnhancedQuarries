package net.pitan76.enhancedquarries.block;

import net.minecraft.block.entity.BlockEntity;
import net.pitan76.enhancedquarries.EnhancedQuarries;
import net.pitan76.enhancedquarries.block.base.Builder;
import net.pitan76.enhancedquarries.item.WrenchItem;
import net.pitan76.enhancedquarries.tile.NormalBuilderTile;
import net.pitan76.enhancedquarries.tile.base.BuilderTile;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.block.BlockUseEvent;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.util.CompatActionResult;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;

public class NormalBuilder extends Builder {

    public NormalBuilder() {
        this(EnhancedQuarries._id("normal_builder"));
    }

    public NormalBuilder(CompatIdentifier id) {
        super(id);
    }

    public NormalBuilder(CompatibleBlockSettings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new NormalBuilderTile(event);
    }

    @Override
    public CompatActionResult onRightClick(BlockUseEvent e) {
        Player player = e.getPlayer();

        if (e.isClient()) return e.success();
        if (e.stack.getItem() instanceof WrenchItem) return e.pass();

        BlockEntity blockEntity = e.getBlockEntity();
        if (blockEntity instanceof BuilderTile)
            player.openGuiScreen((BuilderTile) blockEntity);

        return e.consume();
    }
}
