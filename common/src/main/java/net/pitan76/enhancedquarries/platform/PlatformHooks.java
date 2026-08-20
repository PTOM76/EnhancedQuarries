package net.pitan76.enhancedquarries.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.world.World;
import net.pitan76.mcpitanlib.midohra.util.math.BlockPos;
import net.pitan76.mcpitanlib.midohra.util.math.Direction;

/**
 * ローダー間で実装が分かれる処理をまとめた入口。
 * 実装は各プラットフォームの {@code platform.<loader>.PlatformHooksImpl} にある。
 */
public class PlatformHooks {

    /**
     * エネルギーの受け渡しをローダーのエネルギーAPIに登録する。
     * Fabric: Team Reborn Energy / Forge・NeoForge: Forge Energy (FE) capability
     */
    @ExpectPlatform
    public static void registerEnergyStorage() {
        throw new AssertionError();
    }

    /**
     * バケツ1杯分の流体量。Fabricはdroplet(81000)、Forge系はmB(1000)。
     */
    @ExpectPlatform
    public static long bucketAmount() {
        throw new AssertionError();
    }

    /**
     * 自インベントリの1スロット分を、隣接ブロックのインベントリへ搬出する。
     *
     * @return 実際に移動した個数
     */
    @ExpectPlatform
    public static long moveToNeighbor(World world, BlockPos pos, Inventory inventory, int slot, Direction dir) {
        throw new AssertionError();
    }

    /**
     * 他MODのエネルギー機械へエネルギーを直接足す (レッドストーンハンマー用)。
     * 対応する機械でなければ false。
     *
     * @return エネルギーを足せたら true
     */
    @ExpectPlatform
    public static boolean addEnergyToForeignTile(BlockEntity blockEntity, long amount) {
        throw new AssertionError();
    }
}
