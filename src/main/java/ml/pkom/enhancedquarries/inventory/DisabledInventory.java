package ml.pkom.enhancedquarries.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;

public class DisabledInventory extends SimpleInventory {

    public DisabledInventory(int size) {
        super(size);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return false;
    }
}
