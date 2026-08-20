package net.pitan76.enhancedquarries.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.item.ItemStack;

// StorageBox は Fabric 版しか無いため、Forge/NeoForge では no-op
public class StorageBoxHooks {

    @ExpectPlatform
    public static ItemStack getStackInStorageBox(ItemStack storageBox) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean hasStackInStorageBox(ItemStack storageBox) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void setStackInStorageBox(ItemStack storageBox, ItemStack stack) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getAmountInStorageBox(ItemStack storageBox) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void setAmountInStorageBox(ItemStack storageBox, int amount) {
        throw new AssertionError();
    }
}
