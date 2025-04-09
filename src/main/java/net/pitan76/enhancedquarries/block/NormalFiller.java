package net.pitan76.enhancedquarries.block;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.pitan76.enhancedquarries.EnhancedQuarries;
import net.pitan76.enhancedquarries.block.base.Filler;
import net.pitan76.enhancedquarries.item.WrenchItem;
import net.pitan76.enhancedquarries.tile.NormalFillerTile;
import net.pitan76.enhancedquarries.tile.base.FillerTile;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.block.BlockUseEvent;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.util.CompatActionResult;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.api.util.WorldUtil;

public class NormalFiller extends Filler {

    public NormalFiller(CompatibleBlockSettings settings) {
        super(settings);
    }

    public NormalFiller(CompatIdentifier id) {
        super(id);
    }

    public NormalFiller() {
        this(EnhancedQuarries._id("normal_filler"));
    }

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent e) {
        return new NormalFillerTile(e);
    }

    // ----

    @Override
    public CompatActionResult onRightClick(BlockUseEvent e) {
        if (e.isClient()) return e.success();
        if (e.getStack().getItem() instanceof WrenchItem) return e.pass();

        BlockEntity blockEntity = e.getBlockEntity();
        if (blockEntity instanceof FillerTile)
            e.getPlayer().openGuiScreen((FillerTile) blockEntity);

        return e.consume();
    }
}
