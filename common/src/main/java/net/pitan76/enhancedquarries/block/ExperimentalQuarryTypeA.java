package net.pitan76.enhancedquarries.block;

import net.minecraft.block.entity.BlockEntity;
import net.pitan76.enhancedquarries.block.base.Quarry;
import net.pitan76.enhancedquarries.tile.ExperimentalQuarryTypeATile;
import net.pitan76.enhancedquarries.tile.ExperimentalQuarryTypeATile.Optimization;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.block.BlockUseEvent;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.util.CompatActionResult;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.api.util.TextUtil;
import net.pitan76.mcpitanlib.midohra.block.entity.BlockEntityWrapper;
import net.pitan76.mcpitanlib.midohra.util.hit.BlockHitResult;
import net.pitan76.mcpitanlib.midohra.util.math.Direction;

public class ExperimentalQuarryTypeA extends Quarry {

    public ExperimentalQuarryTypeA(CompatIdentifier id) {
        super(id);
    }

    // ----

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent event) {
        return new ExperimentalQuarryTypeATile(event);
    }

    /**
     * Shift+右クリックで最適化を切り替える。
     * <p>
     * スニーク中はアイテムを持っているとブロック側の処理が呼ばれないため、素手で操作する必要がある。
     * 入力が1種類しか無いので、クリックした面で操作を分けている。
     * <ul>
     *     <li>上面 … 次の項目を選択</li>
     *     <li>下面 … 現在の設定を一覧表示</li>
     *     <li>側面 … 選択中の項目をON/OFF</li>
     * </ul>
     */
    @Override
    public CompatActionResult onRightClick(BlockUseEvent e) {
        if (!e.isSneaking()) return super.onRightClick(e);

        BlockEntityWrapper blockEntity = e.getBlockEntityWrapper();
        if (!blockEntity.instanceOf(ExperimentalQuarryTypeATile.class)) return super.onRightClick(e);

        if (e.isClient()) return e.success();

        ExperimentalQuarryTypeATile tile = blockEntity.getCompatBlockEntity(ExperimentalQuarryTypeATile.class);
        Player player = e.getPlayer();
        Direction side = BlockHitResult.of(e.getHit()).getSideM();

        if (side.equals(Direction.DOWN)) {
            sendOptimizationList(player, tile);
            return e.success();
        }

        if (side.equals(Direction.UP)) {
            tile.selectNextOptimization();
            sendSelectedOptimization(player, tile);
            return e.success();
        }

        tile.toggleSelectedOptimization();
        sendSelectedOptimization(player, tile);

        return e.success();
    }

    public static void sendSelectedOptimization(Player player, ExperimentalQuarryTypeATile tile) {
        Optimization optimization = tile.getSelectedOptimization();

        player.sendActionBar(TextUtil.translatable("info.enhanced_quarries.optimization.selected",
                TextUtil.translatable(optimization.getTranslationKey()),
                TextUtil.translatable(getStateKey(tile.isEnabled(optimization)))));
    }

    public static void sendOptimizationList(Player player, ExperimentalQuarryTypeATile tile) {
        player.sendMessage(TextUtil.translatable("info.enhanced_quarries.optimization.list"));

        for (Optimization optimization : Optimization.VALUES) {
            String key = optimization.equals(tile.getSelectedOptimization())
                    ? "info.enhanced_quarries.optimization.entry.selected"
                    : "info.enhanced_quarries.optimization.entry";

            player.sendMessage(TextUtil.translatable(key,
                    TextUtil.translatable(optimization.getTranslationKey()),
                    TextUtil.translatable(getStateKey(tile.isEnabled(optimization)))));
        }

        player.sendMessage(TextUtil.translatable("info.enhanced_quarries.optimization.usage"));
    }

    public static String getStateKey(boolean enabled) {
        return enabled ? "info.enhanced_quarries.optimization.on" : "info.enhanced_quarries.optimization.off";
    }
}
