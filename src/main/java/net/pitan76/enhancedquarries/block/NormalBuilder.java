package net.pitan76.enhancedquarries.block;

import net.pitan76.mcpitanlib.api.util.CompatActionResult;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.pitan76.enhancedquarries.block.base.Builder;
import net.pitan76.enhancedquarries.item.WrenchItem;
import net.pitan76.enhancedquarries.tile.NormalBuilderTile;
import net.pitan76.enhancedquarries.tile.base.BuilderTile;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.block.BlockUseEvent;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.util.WorldUtil;

public class NormalBuilder extends Builder {

    public NormalBuilder() {
        super();
    }

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new NormalBuilderTile(event);
    }

    // instance
    public static Builder INSTANCE = new NormalBuilder();

    public static Builder getInstance() {
        return INSTANCE;
    }

    public static Builder getBuilder() {
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

        if (e.getBlockEntity() instanceof BuilderTile)
            player.openGuiScreen((BuilderTile) world.getBlockEntity(pos));

        return e.consume();
    }
}
