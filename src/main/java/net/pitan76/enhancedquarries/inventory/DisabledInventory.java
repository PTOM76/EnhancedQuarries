package net.pitan76.enhancedquarries.inventory;

import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.util.inventory.CompatInventory;
import net.pitan76.mcpitanlib.api.util.inventory.args.CanInsertArgs;

public class DisabledInventory extends CompatInventory {

    public DisabledInventory(int size) {
        super(size);
    }

    @Override
    public boolean canInsert(CanInsertArgs args) {
        return false;
    }

    @Override
    public boolean canPlayerUse(Player player) {
        return false;
    }
}
