package ml.pkom.enhancedquarries.block;

import ml.pkom.enhancedquarries.block.base.Filler;
import ml.pkom.enhancedquarries.tile.NormalFillerTile;
import ml.pkom.enhancedquarries.tile.base.FillerTile;
import ml.pkom.mcpitanlibarch.api.entity.Player;
import ml.pkom.mcpitanlibarch.api.event.block.BlockUseEvent;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NormalFiller extends Filler {

    public NormalFiller() {
        super();
    }

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new NormalFillerTile(event);
    }

    // instance
    public static Filler INSTANCE = new NormalFiller();

    public static Filler getInstance() {
        return INSTANCE;
    }

    public static Filler getFiller() {
        return getInstance();
    }
    // ----

    @Override
    public ActionResult onRightClick(BlockUseEvent event) {
        World world = event.getWorld();
        Player player = event.getPlayer();
        BlockPos pos = event.getPos();

        if (world.isClient()) return ActionResult.SUCCESS;

        if (world.getBlockEntity(pos) instanceof FillerTile)
            player.openGuiScreen((FillerTile) world.getBlockEntity(pos));

        return ActionResult.CONSUME;
    }
}
