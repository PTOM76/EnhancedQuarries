package net.pitan76.enhancedquarries.block;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
import net.pitan76.mcpitanlib.api.util.WorldUtil;

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
        World world = e.getWorld();
        Player player = e.getPlayer();
        BlockPos pos = e.getPos();

        if (WorldUtil.isClient(world)) return e.success();
        if (e.stack.getItem() instanceof WrenchItem) return e.pass();

        if (e.getBlockEntity() instanceof BuilderTile)
            player.openGuiScreen((BuilderTile) world.getBlockEntity(pos));

        return e.consume();
    }
}
