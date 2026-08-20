package net.pitan76.enhancedquarries.platform.neoforge;

import net.minecraft.item.ItemStack;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;

/**
 * StorageBox は Fabric 版しか存在しないため、常に空扱いにする。
 */
public class StorageBoxHooksImpl {

    public static ItemStack getStackInStorageBox(ItemStack storageBox) {
        return ItemStackUtil.empty();
    }

    public static boolean hasStackInStorageBox(ItemStack storageBox) {
        return false;
    }

    public static void setStackInStorageBox(ItemStack storageBox, ItemStack stack) {
    }

    public static int getAmountInStorageBox(ItemStack storageBox) {
        return 0;
    }

    public static void setAmountInStorageBox(ItemStack storageBox, int amount) {
    }
}
