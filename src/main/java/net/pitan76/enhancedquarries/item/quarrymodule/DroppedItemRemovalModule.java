package net.pitan76.enhancedquarries.item.quarrymodule;

import ml.pkom.mcpitanlibarch.api.entity.Player;
import ml.pkom.mcpitanlibarch.api.event.item.ItemUseEvent;
import ml.pkom.mcpitanlibarch.api.item.CompatibleItemSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;
import net.pitan76.enhancedquarries.item.base.MachineModule;
import net.pitan76.enhancedquarries.screen.DroppedItemRemovalModuleEditScreenHandler;

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
