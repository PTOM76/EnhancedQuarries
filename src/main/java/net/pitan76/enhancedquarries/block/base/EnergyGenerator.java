package net.pitan76.enhancedquarries.block.base;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.pitan76.enhancedquarries.item.WrenchItem;
import net.pitan76.enhancedquarries.tile.base.EnergyGeneratorTile;
import net.pitan76.mcpitanlib.api.block.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.block.CompatibleMaterial;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.block.BlockUseEvent;
import net.pitan76.mcpitanlib.api.event.block.StateReplacedEvent;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;

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
                if (tile.keepNbtOnDrop) {
                    super.onStateReplaced(e);
                    return;
                }

                ItemScatterer.spawn(e.world, e.pos, tile);
            }
            super.onStateReplaced(e);
        }
    }

    @Override
    public ActionResult onRightClick(BlockUseEvent e) {
        World world = e.getWorld();
        Player player = e.getPlayer();
        BlockPos pos = e.getPos();

        if (WorldUtil.isClient(world)) return e.success();
        if (e.stack.getItem() instanceof WrenchItem) return e.pass();

        if (world.getBlockEntity(pos) instanceof EnergyGeneratorTile)
            player.openExtendedMenu((EnergyGeneratorTile) world.getBlockEntity(pos));

        return e.consume();
    }
}
