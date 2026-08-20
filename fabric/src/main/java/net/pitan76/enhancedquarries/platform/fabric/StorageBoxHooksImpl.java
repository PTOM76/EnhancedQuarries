package net.pitan76.enhancedquarries.platform.fabric;

import net.minecraft.item.ItemStack;
import net.pitan76.storagebox.api.StorageBoxUtil;

public class StorageBoxHooksImpl {

    public static ItemStack getStackInStorageBox(ItemStack storageBox) {
        return StorageBoxUtil.getStackInStorageBox(storageBox);
    }

    public static boolean hasStackInStorageBox(ItemStack storageBox) {
        return StorageBoxUtil.hasStackInStorageBox(storageBox);
    }

    public static void setStackInStorageBox(ItemStack storageBox, ItemStack stack) {
        StorageBoxUtil.setStackInStorageBox(storageBox, stack);
    }

    public static int getAmountInStorageBox(ItemStack storageBox) {
        return StorageBoxUtil.getAmountInStorageBox(storageBox);
    }

    public static void setAmountInStorageBox(ItemStack storageBox, int amount) {
        StorageBoxUtil.setAmountInStorageBox(storageBox, amount);
    }
}
