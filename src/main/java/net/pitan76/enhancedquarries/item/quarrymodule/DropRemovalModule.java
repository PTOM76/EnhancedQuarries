package net.pitan76.enhancedquarries.item.quarrymodule;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.TypedActionResult;
import net.pitan76.enhancedquarries.item.base.MachineModule;
import net.pitan76.enhancedquarries.screen.DropRemovalModuleScreenHandler;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.item.ItemUseEvent;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DropRemovalModule extends MachineModule implements NamedScreenHandlerFactory {
    public DropRemovalModule(CompatibleItemSettings settings) {
        super(settings);
    }

    @Override
    public boolean allowMultiple() {
        return true;
    }

    @Override
    public TypedActionResult<ItemStack> onRightClick(ItemUseEvent e) {
        ItemStack stack = e.getStack();
        Player player = e.getUser();
        if (e.isClient()) return TypedActionResult.pass(stack);

        if (player.getEntity() == null) return TypedActionResult.pass(stack);

        if (player.isSneaking()) {
            player.openMenu(this);
        }
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
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        Optional<ItemStack> optional = new Player(player).getCurrentHandItem();
        return optional.map(stack -> new DropRemovalModuleScreenHandler(syncId, playerInventory, stack))
                .orElseGet(() -> new DropRemovalModuleScreenHandler(syncId, playerInventory));
    }

    @Override
    public Text getDisplayName() {
        return TextUtil.translatable("screen.enhanced_quarries.dropped_item_removal_module_edit.title");
    }
}
