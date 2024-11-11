package net.pitan76.enhancedquarries.block.base;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;
import net.pitan76.enhancedquarries.EnhancedQuarries;
import net.pitan76.enhancedquarries.item.WrenchItem;
import net.pitan76.enhancedquarries.tile.base.EnergyGeneratorTile;
import net.pitan76.mcpitanlib.api.block.CompatibleMaterial;
import net.pitan76.mcpitanlib.api.block.v2.BlockSettingsBuilder;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.block.BlockUseEvent;
import net.pitan76.mcpitanlib.api.event.block.ItemScattererUtil;
import net.pitan76.mcpitanlib.api.event.block.StateReplacedEvent;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.util.CompatActionResult;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.api.util.WorldUtil;

public class EnergyGenerator extends BaseBlock {

    public static BlockSettingsBuilder defaultSettings = new BlockSettingsBuilder()
        .material(CompatibleMaterial.METAL)
        .requiresTool()
        .strength(2, 8);

    public EnergyGenerator(CompatibleBlockSettings settings) {
        super(settings);
    }

    public EnergyGenerator(CompatIdentifier id) {
        this(defaultSettings.build(id));
    }

    public EnergyGenerator() {
        this(EnhancedQuarries._id("energy_generator"));
    }

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new EnergyGeneratorTile(event);
    }

    @Override
    public void onStateReplaced(StateReplacedEvent e) {
        if (e.state.getBlock() != e.newState.getBlock()) {
            BlockEntity blockEntity = e.getBlockEntity();
            if (blockEntity instanceof EnergyGeneratorTile) {
                EnergyGeneratorTile tile = (EnergyGeneratorTile) blockEntity;
                if (tile.keepNbtOnDrop) {
                    super.onStateReplaced(e);
                    return;
                }

                ItemScattererUtil.spawn(e.world, e.pos, (BlockEntity) tile);
            }
            super.onStateReplaced(e);
        }
    }

    @Override
    public CompatActionResult onRightClick(BlockUseEvent e) {
        World world = e.getWorld();
        Player player = e.getPlayer();

        if (WorldUtil.isClient(world)) return e.success();
        if (e.stack.getItem() instanceof WrenchItem) return e.pass();

        if (e.getBlockEntity() instanceof EnergyGeneratorTile)
            player.openExtendedMenu((EnergyGeneratorTile) e.getBlockEntity());

        return e.consume();
    }
}
