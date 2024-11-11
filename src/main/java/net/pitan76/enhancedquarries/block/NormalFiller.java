package net.pitan76.enhancedquarries.block;

import net.pitan76.mcpitanlib.api.util.CompatActionResult;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.pitan76.enhancedquarries.block.base.Filler;
import net.pitan76.enhancedquarries.item.WrenchItem;
import net.pitan76.enhancedquarries.tile.NormalFillerTile;
import net.pitan76.enhancedquarries.tile.base.FillerTile;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.block.BlockUseEvent;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.api.util.WorldUtil;

public class NormalFiller extends Filler {

    public NormalFiller(CompatIdentifier id) {
        super(id);
    }

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new NormalFillerTile(event);
    }

    // ----

    @Override
    public CompatActionResult onRightClick(BlockUseEvent e) {
        World world = e.getWorld();
        Player player = e.getPlayer();
        BlockPos pos = e.getPos();

        if (WorldUtil.isClient(world)) return e.success();
        if (e.stack.getItem() instanceof WrenchItem) return e.pass();

        if (e.getBlockEntity() instanceof FillerTile)
            player.openGuiScreen((FillerTile) world.getBlockEntity(pos));

        return e.consume();
    }
}
