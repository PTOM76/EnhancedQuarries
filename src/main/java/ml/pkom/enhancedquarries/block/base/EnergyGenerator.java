package ml.pkom.enhancedquarries.block.base;

import ml.pkom.enhancedquarries.tile.base.EnergyGeneratorTile;
import ml.pkom.mcpitanlibarch.api.block.CompatibleBlockSettings;
import ml.pkom.mcpitanlibarch.api.block.CompatibleMaterial;
import ml.pkom.mcpitanlibarch.api.entity.Player;
import ml.pkom.mcpitanlibarch.api.event.block.BlockUseEvent;
import ml.pkom.mcpitanlibarch.api.event.block.StateReplacedEvent;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnergyGenerator extends BaseBlock {

    public static CompatibleBlockSettings defaultSettings = CompatibleBlockSettings
            .of(CompatibleMaterial.METAL)
            .requiresTool()
            .strength(2, 8);

    public EnergyGenerator(CompatibleBlockSettings settings) {
        super(settings);
    }

    public EnergyGenerator() {
        this(defaultSettings);
    }

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new EnergyGeneratorTile(event);
    }

    public static EnergyGenerator INSTANCE = new EnergyGenerator();

    public static EnergyGenerator getInstance() {
        return INSTANCE;
    }

    public static EnergyGenerator getEnergyGenerator() {
        return getInstance();
    }

    @Override
    public void onStateReplaced(StateReplacedEvent e) {
        if (e.state.getBlock() != e.newState.getBlock()) {
            BlockEntity blockEntity = e.world.getBlockEntity(e.pos);
            if (blockEntity instanceof EnergyGeneratorTile) {
                EnergyGeneratorTile tile = (EnergyGeneratorTile) blockEntity;
                ItemScatterer.spawn(e.world, e.pos, tile);
            }
            super.onStateReplaced(e);
        }
    }

    @Override
    public ActionResult onRightClick(BlockUseEvent event) {
        World world = event.getWorld();
        Player player = event.getPlayer();
        BlockPos pos = event.getPos();

        if (world.isClient()) return ActionResult.SUCCESS;

        if (world.getBlockEntity(pos) instanceof EnergyGeneratorTile)
            player.openGuiScreen((EnergyGeneratorTile) world.getBlockEntity(pos));

        return ActionResult.CONSUME;
    }
}
