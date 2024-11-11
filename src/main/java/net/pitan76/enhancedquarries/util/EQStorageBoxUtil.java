package net.pitan76.enhancedquarries.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.pitan76.mcpitanlib.api.util.PlatformUtil;
import net.pitan76.mcpitanlib.api.util.item.ItemUtil;

public class EQStorageBoxUtil {
    public static boolean isStorageBox(ItemStack stack) {
        return isStorageBox(stack.getItem());
    }

    public static boolean isStorageBox(Item item) {
        if (PlatformUtil.isModLoaded("storagebox")) {
            return ItemUtil.toId(item).toString().equals("storagebox:storagebox");
        }

        return false;
    }
}
