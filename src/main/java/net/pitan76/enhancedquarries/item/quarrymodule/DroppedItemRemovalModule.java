package net.pitan76.enhancedquarries.item.quarrymodule;

import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;
import net.pitan76.enhancedquarries.item.base.MachineModule;
import net.pitan76.enhancedquarries.screen.DroppedItemRemovalModuleEditScreenHandler;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.item.ItemUseEvent;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;

public class DroppedItemRemovalModule extends MachineModule {
    public DroppedItemRemovalModule(CompatibleItemSettings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> onRightClick(ItemUseEvent e) {
        ItemStack stack = e.getStack();
        Player player = e.getUser();
        if (e.isClient()) return TypedActionResult.pass(stack);
        if (player.isSneaking()) {
            player.openGuiScreen(DroppedItemRemovalModuleEditScreenHandler.factory);
        }
        return super.onRightClick(e);
    }
}
