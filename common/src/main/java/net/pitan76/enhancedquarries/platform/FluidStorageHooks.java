package net.pitan76.enhancedquarries.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.entity.BlockEntityType;

/**
 * TODO: ライブラリ側にFLUID LOOKUPがいる。
 */
public class FluidStorageHooks {
    @ExpectPlatform
    public static void registerPumpFluidStorage(BlockEntityType<?> type) {
        throw new AssertionError();
    }
}
