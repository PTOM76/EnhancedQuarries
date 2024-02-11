package ml.pkom.enhancedquarries.screen;

import ml.pkom.enhancedquarries.ScreenHandlers;
import ml.pkom.enhancedquarries.inventory.FillerCraftingInventory;
import ml.pkom.enhancedquarries.inventory.FillerInventory;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;

public class FillerWithChestScreenHandler extends FillerScreenHandler {
    public FillerWithChestScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new FillerInventory(54), new FillerCraftingInventory());
    }

    public FillerWithChestScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, Inventory craftingInventory) {
        super(ScreenHandlers.FILLER_WITH_CHEST_SCREEN_HANDLER_TYPE, syncId, playerInventory, inventory, craftingInventory);
        int m, l;
        for (m = 0; m < 9; ++m) {
            for (l = 0; l < 3; ++l) {
                callAddSlot(new Slot(inventory, l + m * 3 + 27, 180 + l * 18, 13 + m * 18));
            }
        }
    }
}
