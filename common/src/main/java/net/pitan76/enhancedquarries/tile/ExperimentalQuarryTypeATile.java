package net.pitan76.enhancedquarries.tile;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.pitan76.enhancedquarries.Config;
import net.pitan76.enhancedquarries.Tiles;
import net.pitan76.enhancedquarries.block.Frame;
import net.pitan76.enhancedquarries.item.quarrymodule.ModuleItems;
import net.pitan76.enhancedquarries.util.UnbreakableBlocks;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.event.nbt.ReadNbtArgs;
import net.pitan76.mcpitanlib.api.event.nbt.WriteNbtArgs;
import net.pitan76.mcpitanlib.api.transfer.item.v1.ItemTransferUtil;
import net.pitan76.mcpitanlib.api.util.FluidUtil;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.entity.ItemEntityUtil;
import net.pitan76.mcpitanlib.api.util.nbt.v2.NbtRWUtil;
import net.pitan76.mcpitanlib.midohra.block.BlockState;
import net.pitan76.mcpitanlib.midohra.block.entity.BlockEntityWrapper;
import net.pitan76.mcpitanlib.midohra.entity.ItemEntityWrapper;
import net.pitan76.mcpitanlib.midohra.util.math.BlockPos;
import net.pitan76.mcpitanlib.midohra.util.math.Box;
import net.pitan76.mcpitanlib.midohra.util.math.Direction;
import net.pitan76.mcpitanlib.midohra.world.World;

import java.util.List;

/**
 * 試験的なクォーリー (Type-A)
 * <p>
 * FluidQuarryTileと能力は同じで、走査ロジックだけを差し替えたもの。
 * どの最適化を有効にするかは{@link Optimization}ごとに個別に切り替えられる。
 * (デフォルト値はconfig、そこからブロックごとにShift+右クリックで変更できる)
 * <p>
 * 従来の{@link net.pitan76.enhancedquarries.tile.base.QuarryTile#tryQuarrying()}は、
 * 1ブロック処理するたびに最上部から範囲全体を走査し直していたため、
 * 掘り進むほど1回あたりのコストが増え続けていた。
 * こちらは走査位置をカーソルとして保持し、前回の続きから再開する。
 * <p>
 * カーソルが最下部まで到達して何も見つからなかった場合は先頭から走査し直し、
 * それでも何も無ければ「掘り終わった」とみなして一定時間走査を休む。
 * (休んだあとに再度全体を走査するので、外部でブロックが設置されても最終的に拾われる)
 *
 * TODO: これはAIによる実装なのでQuarryTileにこのロジックを組み込むかは要チェックってことで
 */
public class ExperimentalQuarryTypeATile extends FluidQuarryTile {

    /**
     * 個別に切り替えられる最適化。
     * 並び順がShift+右クリックでの巡回順になるので、途中に挿入しないこと。
     */
    public enum Optimization {
        /**
         * 走査位置をカーソルとして保持し、前回の続きから再開する。
         * 無効の場合は毎回範囲の先頭から走査し直す (従来と同じ挙動)。
         */
        SCAN_CURSOR("scan_cursor"),

        /**
         * 範囲全体を走査しても何も無ければ、一定時間走査を休む。
         * 掘り終わったクォーリーが走査し続けるのを防ぐ。
         */
        IDLE_SUSPEND("idle_suspend"),

        /**
         * 自分自身かどうかの判定を座標の比較で行う。
         * 無効の場合は座標ごとにBlockEntityを取得して比較する (従来と同じ挙動)。
         */
        FAST_SELF_CHECK("fast_self_check"),

        /**
         * 1座標につきBlockStateの取得を1回にまとめる。
         */
        CACHE_BLOCK_STATE("cache_block_state"),

        /**
         * モジュールの有無を走査開始時にまとめて確定させる。
         */
        CACHE_MODULE_FLAGS("cache_module_flags"),

        /**
         * チェストへの搬出で、空スロットの判定を方向のループの外に出す。
         */
        OPTIMIZED_INSERT_CHEST("optimized_insert_chest");

        public static final Optimization[] VALUES = values();

        private final String id;

        Optimization(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public String getTranslationKey() {
            return "info.enhanced_quarries.optimization." + id;
        }

        public int getFlag() {
            return 1 << ordinal();
        }

        public boolean getDefaultValue() {
            return Config.getExperimentalQuarryOptimization(id);
        }
    }

    // 走査結果
    protected static final int SCAN_NOTHING = 0;    // 範囲を走査し終えたが何も無かった
    protected static final int SCAN_MINED = 1;      // 採掘した (呼び出し元でエネルギーを消費する)
    protected static final int SCAN_FRAME = 2;      // フレームを設置した (エネルギー消費済み)
    protected static final int SCAN_SUSPENDED = 3;  // 走査量の上限に達したので次回に持ち越す

    // 有効になっている最適化 (Optimization#getFlagのビットマスク)
    protected int enabledOptimizations = defaultOptimizations();

    // Shift+右クリックで選択中の最適化
    protected int selectedOptimization = 0;

    // 走査位置のカーソル
    protected int scanX = Integer.MIN_VALUE;
    protected int scanY = Integer.MIN_VALUE;
    protected int scanZ = Integer.MIN_VALUE;

    // 現在の走査が範囲の先頭から始まったものかどうか
    protected boolean scanFromTop = true;

    // この時刻まで走査を休む (0なら休んでいない)
    protected long suspendUntil = 0L;

    // 今回のtryQuarryingで走査した座標数
    protected int scannedCount = 0;

    // モジュールの有無 (1回の走査中は変わらないのでキャッシュする)
    protected boolean hasBedrockBreakModule = false;
    protected boolean hasMobDeleteModule = false;
    protected boolean hasMobKillModule = false;
    protected boolean hasExpCollectModule = false;

    // 継承のため
    public ExperimentalQuarryTypeATile(BlockEntityType<?> type, TileCreateEvent e) {
        super(type, e);
    }

    public ExperimentalQuarryTypeATile(TileCreateEvent e) {
        this(Tiles.EXPERIMENTAL_QUARRY_TYPE_A_TILE.getOrNull(), e);
    }

    // ---- 最適化の切り替え ----

    public static int defaultOptimizations() {
        int flags = 0;
        for (Optimization optimization : Optimization.VALUES) {
            if (optimization.getDefaultValue())
                flags |= optimization.getFlag();
        }
        return flags;
    }

    public boolean isEnabled(Optimization optimization) {
        return (enabledOptimizations & optimization.getFlag()) != 0;
    }

    public void setEnabled(Optimization optimization, boolean enabled) {
        if (enabled)
            enabledOptimizations |= optimization.getFlag();
        else
            enabledOptimizations &= ~optimization.getFlag();

        // 挙動が変わるので走査をやり直す
        resetScanCursor();
        suspendUntil = 0L;
        callMarkDirty();
    }

    public Optimization getSelectedOptimization() {
        return Optimization.VALUES[Math.floorMod(selectedOptimization, Optimization.VALUES.length)];
    }

    public void selectNextOptimization() {
        selectedOptimization = Math.floorMod(selectedOptimization + 1, Optimization.VALUES.length);
        callMarkDirty();
    }

    public boolean toggleSelectedOptimization() {
        Optimization optimization = getSelectedOptimization();
        boolean enabled = !isEnabled(optimization);
        setEnabled(optimization, enabled);

        return enabled;
    }

    // ---- NBT ----

    @Override
    public void writeNbt(WriteNbtArgs args) {
        super.writeNbt(args);

        NbtRWUtil.putInt(args, "enabledOptimizations", enabledOptimizations);
        NbtRWUtil.putInt(args, "selectedOptimization", selectedOptimization);
    }

    @Override
    public void readNbt(ReadNbtArgs args) {
        super.readNbt(args);

        enabledOptimizations = NbtRWUtil.getIntOrDefault(args, "enabledOptimizations", defaultOptimizations());
        selectedOptimization = NbtRWUtil.getIntOrDefault(args, "selectedOptimization", 0);
    }

    // ---- 走査 ----

    // 範囲が変わったらカーソルを作り直す
    @Override
    public void setMinPos(BlockPos pos) {
        super.setMinPos(pos);
        resetScanCursor();
        suspendUntil = 0L;
    }

    @Override
    public void setMaxPos(BlockPos pos) {
        super.setMaxPos(pos);
        resetScanCursor();
        suspendUntil = 0L;
    }

    public void resetScanCursor() {
        scanY = getMaxPos() == null ? Integer.MIN_VALUE : getMaxPos().getY();
        scanX = Integer.MIN_VALUE;
        scanZ = Integer.MIN_VALUE;
        scanFromTop = true;
    }

    @Override
    public boolean tryQuarrying() {
        World world = getMidohraWorld();
        if (world.toMinecraft() == null || world.isClient()) return false;

        if (getMinPos() == null)
            setMinPos(getDefaultRangeMinPos());
        if (getMaxPos() == null)
            setMaxPos(getDefaultRangeMaxPos());

        // 掘り終わっている間は走査を休む
        if (suspendUntil != 0L) {
            if (world.getTime() < suspendUntil) return false;

            suspendUntil = 0L;
            resetScanCursor();
        }

        // カーソルを使わない場合は毎回先頭から走査する
        if (!isEnabled(Optimization.SCAN_CURSOR))
            resetScanCursor();

        if (isEnabled(Optimization.CACHE_MODULE_FLAGS)) {
            hasBedrockBreakModule = hasModuleItem(ModuleItems.BEDROCK_BREAK_MODULE);
            hasMobDeleteModule = hasModuleItem(ModuleItems.MOB_DELETE_MODULE);
            hasMobKillModule = hasModuleItem(ModuleItems.MOB_KILL_MODULE);
            hasExpCollectModule = hasModuleItem(ModuleItems.EXP_COLLECT_MODULE);
        }

        scannedCount = 0;
        int result = scanRange(world);

        if (result == SCAN_NOTHING) {
            if (scanFromTop && isEnabled(Optimization.IDLE_SUSPEND)) {
                // 先頭から走査しても何も無かったので、しばらく休む
                // 複数台が同じtickに一斉に走査しないよう、待ち時間をずらす
                long interval = getRescanInterval();
                long stagger = Math.floorMod(getMidohraPos().hashCode(), interval);
                suspendUntil = world.getTime() + interval + stagger;
            } else {
                // カーソルの途中から走査していたので、次回は先頭から走査し直す
                resetScanCursor();
            }
        }

        return result == SCAN_MINED;
    }

    /**
     * カーソル位置から範囲を走査し、最初に処理できたブロックで打ち切る。
     */
    protected int scanRange(World world) {
        BlockPos minPos = getMinPos();
        BlockPos maxPos = getMaxPos();

        // カーソルを使わない場合は途中で打ち切ると掘り残すので、上限を設けない
        int budget = isEnabled(Optimization.SCAN_CURSOR) ? getScanBudget() : Integer.MAX_VALUE;

        long bottomY = world.getBottomY();
        if (scanY == Integer.MIN_VALUE || scanY > maxPos.getY())
            scanY = maxPos.getY();

        for (; scanY > bottomY; scanY--) {
            boolean inner;
            if (minPos.getY() - 1 >= scanY)
                inner = true;
            else if (minPos.getY() <= scanY && maxPos.getY() >= scanY)
                inner = false;
            else
                continue;

            // procX < maxPos.getX()を=<するとposのずれ問題は修正可能だが、別の方法で対処しているので、時間があればこっちで修正したい。
            int startX = inner ? minPos.getX() + 1 : minPos.getX();
            int endX = inner ? maxPos.getX() - 1 : maxPos.getX();
            int startZ = inner ? minPos.getZ() + 1 : minPos.getZ();
            int endZ = inner ? maxPos.getZ() - 1 : maxPos.getZ();

            // 別のレイヤーに移った場合などはカーソルを先頭に戻す
            if (scanX < startX || scanX >= endX) {
                scanX = startX;
                scanZ = startZ;
            }
            if (scanZ < startZ || scanZ >= endZ)
                scanZ = startZ;

            for (; scanX < endX; scanX++, scanZ = startZ) {
                for (; scanZ < endZ; scanZ++) {
                    if (scannedCount >= budget) return SCAN_SUSPENDED;
                    scannedCount++;

                    BlockPos procPos = BlockPos.of(scanX, scanY, scanZ);
                    int result = inner ? processInnerPos(world, procPos) : processFramePos(world, procPos);

                    if (result != SCAN_NOTHING) return result;
                }
            }

            // このレイヤーは処理し終えたので、次のレイヤーでは先頭に戻す
            scanX = Integer.MIN_VALUE;
        }

        return SCAN_NOTHING;
    }

    /**
     * 枠より下のレイヤー (掘削のみ)
     */
    protected int processInnerPos(World world, BlockPos procPos) {
        if (isSelf(world, procPos)) return SCAN_NOTHING;

        BlockState state = world.getBlockState(procPos);
        if (state == null || state.toMinecraft() == null) return SCAN_NOTHING;

        Block procBlock = getProcBlock(world, procPos, state);
        if (isSkippableBlock(procBlock)) {
            tryFluidReplaceWithEnergy(procPos);
            return SCAN_NOTHING;
        }

        if (procBlock instanceof FluidBlock) {
            if (canReplaceFluid()
                    && world.getFluidState(procPos.toMinecraft()).isStill()
                    && getEnergy() > getReplaceFluidEnergyCost()) {
                world.setBlockState(procPos, getReplaceFluidWithBlockWrapper().getDefaultState());
                useEnergy(getReplaceFluidEnergyCost());
            } else {
                return SCAN_NOTHING;
            }
        }

        tryFluidReplaceWithEnergy(procPos);
        tryModuleEffects(procPos);

        breakBlock(procPos, true);
        collectDroppedItems(world, procPos);

        return SCAN_MINED;
    }

    /**
     * 枠のレイヤー (フレーム設置あり)
     */
    protected int processFramePos(World world, BlockPos procPos) {
        if (isSelf(world, procPos)) return SCAN_NOTHING;

        BlockState state = world.getBlockState(procPos);
        if (state == null || state.toMinecraft() == null) return SCAN_NOTHING;

        Block procBlock = getProcBlock(world, procPos, state);
        if (isSkippableBlock(procBlock)) {
            if (tryPlaceFrame(procPos)) {
                useEnergy(getPlaceFrameEnergyCost());
                return SCAN_FRAME;
            }
            tryFluidReplaceWithEnergy(procPos);
            return SCAN_NOTHING;
        }

        if (procBlock instanceof Frame) return SCAN_NOTHING;

        tryFluidReplaceWithEnergy(procPos);
        tryModuleEffects(procPos);

        if (procBlock instanceof FluidBlock
                && !FluidUtil.isStill(world.getFluidState(procPos.toMinecraft())))
            return SCAN_NOTHING;

        breakBlock(procPos, false);

        return SCAN_MINED;
    }

    protected boolean isSelf(World world, BlockPos procPos) {
        if (isEnabled(Optimization.FAST_SELF_CHECK))
            return procPos.equals(getMidohraPos());

        BlockEntityWrapper blockEntity = world.getBlockEntity(procPos);
        return blockEntity != null && blockEntity.get() == this;
    }

    protected Block getProcBlock(World world, BlockPos procPos, BlockState state) {
        if (isEnabled(Optimization.CACHE_BLOCK_STATE))
            return state.getBlock().get();

        return world.getBlockState(procPos).getBlock().get();
    }

    protected boolean isSkippableBlock(Block block) {
        if (block instanceof AirBlock) return true;

        return !hasModule(ModuleItems.BEDROCK_BREAK_MODULE, hasBedrockBreakModule)
                && UnbreakableBlocks.isUnbreakable(block);
    }

    protected boolean hasModule(Item item, boolean cached) {
        if (isEnabled(Optimization.CACHE_MODULE_FLAGS)) return cached;

        return hasModuleItem(item);
    }

    protected void tryFluidReplaceWithEnergy(BlockPos procPos) {
        if (!canReplaceFluid()) return;

        double time = tryFluidReplace(procPos);
        if (time != 0)
            useEnergy((long) time * getReplaceFluidEnergyCost());
    }

    protected void tryModuleEffects(BlockPos procPos) {
        if (hasModule(ModuleItems.MOB_DELETE_MODULE, hasMobDeleteModule)) tryDeleteMob(procPos);
        if (hasModule(ModuleItems.MOB_KILL_MODULE, hasMobKillModule)) tryKillMob(procPos);
        if (hasModule(ModuleItems.EXP_COLLECT_MODULE, hasExpCollectModule)) tryCollectExp(procPos);
    }

    protected void collectDroppedItems(World world, BlockPos procPos) {
        List<ItemEntityWrapper> entities = ItemEntityUtil.getEntityWrappers(world,
                new Box(procPos.add(-1, -1, -1), procPos.add(1, 1, 1)));

        for (ItemEntityWrapper itemEntity : entities) {
            if (!itemEntity.isAlive()) continue;

            addStack(itemEntity.getStackRaw());
            itemEntity.discard();
        }
    }

    // ---- チェストへの搬出 ----

    @Override
    public void insertChest() {
        if (!isEnabled(Optimization.OPTIMIZED_INSERT_CHEST)) {
            super.insertChest();
            return;
        }

        if (getItems().isEmpty()) return;

        List<Direction> dirs = getDirsOfAnyContainerBlock();
        if (dirs.isEmpty()) return;

        int time = 0;
        int size = getItems().size();

        for (int i = 0; i < size; i++) {
            if (time > limit) break;

            // 空スロットの判定を方向のループの外に出す
            ItemStack stack = getItems().get(i);
            if (stack.isEmpty()) continue;

            for (Direction dir : dirs) {
                int moved = ItemTransferUtil.moveToNeighbor(callGetWorld(), callGetPos(), dir.toMinecraft(), ItemStackUtil.copy(stack));
                if (moved <= 0) continue;

                ItemStackUtil.decrementCount(stack, moved);
                callMarkDirty();

                ++time;
                break;
            }
        }
    }

    // ---- 設定値 ----

    public int getScanBudget() {
        return Math.max(1, Config.experimental_quarry_scan_budget);
    }

    public long getRescanInterval() {
        return Math.max(1L, Config.experimental_quarry_rescan_interval);
    }
}
