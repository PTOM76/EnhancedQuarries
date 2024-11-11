package net.pitan76.enhancedquarries.item.quarrymodule;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.pitan76.enhancedquarries.item.base.MachineModule;
import net.pitan76.enhancedquarries.screen.DropRemovalModuleScreenHandler;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.container.factory.DisplayNameArgs;
import net.pitan76.mcpitanlib.api.event.item.ItemUseEvent;
import net.pitan76.mcpitanlib.api.gui.args.CreateMenuEvent;
import net.pitan76.mcpitanlib.api.gui.v2.SimpleScreenHandlerFactory;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.mcpitanlib.api.util.item.ItemUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DropRemovalModule extends MachineModule implements SimpleScreenHandlerFactory {
    public DropRemovalModule(CompatibleItemSettings settings) {
        super(settings);
    }

    @Override
    public boolean allowMultiple() {
        return true;
    }

    @Override
    public StackActionResult onRightClick(ItemUseEvent e) {
        Player player = e.getUser();
        if (e.isClient() || player.getEntity() == null) return e.pass();

        if (e.isSneaking())
            player.openMenu(this);

        return super.onRightClick(e);
    }

    public static List<Item> getRemovalItems(ItemStack stack) {
        List<Item> items = new ArrayList<>();

        NbtCompound nbt = CustomDataUtil.getNbt(stack);
        NbtList list = NbtUtil.getList(nbt, "Items");

        for (int i = 0; i < list.size(); i++) {
            String itemId = list.getString(i);
            if (itemId == null || itemId.isEmpty()) continue;

            items.add(ItemUtil.fromId(CompatIdentifier.of(itemId)));
        }

        return items;
    }

    public static NbtCompound getNbtFromCurrentStack(Player player) {
        Optional<ItemStack> optional = player.getCurrentHandItem();
        if (optional.isPresent()) {
            ItemStack stack = optional.get();
            return getNbt(stack);
        }
        return NbtUtil.create();
    }

    public static NbtCompound getNbt(ItemStack stack) {
        if (CustomDataUtil.hasNbt(stack)) {
            return CustomDataUtil.getNbt(stack);
        }
        return NbtUtil.create();
    }

    @Override
    public ScreenHandler createMenu(CreateMenuEvent e) {
        Optional<ItemStack> optional = e.getPlayer().getCurrentHandItem();
        return optional.map(stack -> new DropRemovalModuleScreenHandler(e.syncId, e.playerInventory, stack))
                .orElseGet(() -> new DropRemovalModuleScreenHandler(e.syncId, e.playerInventory));
    }

    @Override
    public Text getDisplayName(DisplayNameArgs args) {
        return TextUtil.translatable("screen.enhanced_quarries.dropped_item_removal_module_edit.title");
    }
}
