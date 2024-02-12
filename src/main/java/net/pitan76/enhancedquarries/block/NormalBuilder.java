package net.pitan76.enhancedquarries.block;

import net.pitan76.enhancedquarries.block.base.Builder;
import net.pitan76.enhancedquarries.tile.NormalBuilderTile;
import net.pitan76.enhancedquarries.tile.base.BuilderTile;
import ml.pkom.mcpitanlibarch.api.entity.Player;
import ml.pkom.mcpitanlibarch.api.event.block.BlockUseEvent;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
    public ActionResult onRightClick(BlockUseEvent event) {
        World world = event.getWorld();
        Player player = event.getPlayer();
        BlockPos pos = event.getPos();

        if (world.isClient()) return ActionResult.SUCCESS;

        if (world.getBlockEntity(pos) instanceof BuilderTile)
            player.openGuiScreen((BuilderTile) world.getBlockEntity(pos));

        return ActionResult.CONSUME;
    }
}
