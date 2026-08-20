package net.pitan76.enhancedquarries.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.world.World;
import net.pitan76.mcpitanlib.midohra.util.math.BlockPos;
import net.pitan76.mcpitanlib.midohra.util.math.Direction;

public class PlatformHooks {

    @ExpectPlatform
    public static void registerEnergyStorage() {
        throw new AssertionError();
    }

    // Fabric: droplet(81000) / Forge・NeoForge: mB(1000)
    @ExpectPlatform
    public static long bucketAmount() {
        throw new AssertionError();
    }

    // 隣接インベントリへ1スロット分搬出し、移動した個数を返す
    @ExpectPlatform
    public static long moveToNeighbor(World world, BlockPos pos, Inventory inventory, int slot, Direction dir) {
        throw new AssertionError();
    }

    // 他MODのエネルギー機械へ直接加算する (レッドストーンハンマー用)
    @ExpectPlatform
    public static boolean addEnergyToForeignTile(BlockEntity blockEntity, long amount) {
        throw new AssertionError();
    }
}
