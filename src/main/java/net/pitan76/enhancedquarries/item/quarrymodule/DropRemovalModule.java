package net.pitan76.enhancedquarries.item.quarrymodule;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.TypedActionResult;
import net.pitan76.enhancedquarries.item.base.MachineModule;
import net.pitan76.enhancedquarries.screen.DropRemovalModuleScreenHandler;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.container.factory.DisplayNameArgs;
import net.pitan76.mcpitanlib.api.event.container.factory.ExtraDataArgs;
import net.pitan76.mcpitanlib.api.event.item.ItemUseEvent;
import net.pitan76.mcpitanlib.api.gui.ExtendedScreenHandlerFactory;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DropRemovalModule extends MachineModule implements ExtendedScreenHandlerFactory {
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
        if (player.isSneaking()) {
            player.openExtendedMenu(this);
        }
        return super.onRightClick(e);
    }

    public static List<Item> getRemovalItems(ItemStack stack) {
        List<Item> items = new ArrayList<>();

        NbtCompound nbt = CustomDataUtil.getNbt(stack);
        for (Object o : NbtUtil.getList(nbt, "Items")) {
            if (!(o instanceof String)) continue;
            items.add(ItemUtil.fromId(CompatIdentifier.of((String) o)));
        }

        return items;
    }

    public static NbtCompound getNbtFromCurrentStack(Player player) {
        Optional<ItemStack> optional = player.getCurrentHandItem();
        if (optional.isPresent()) {
            ItemStack stack = optional.get();
            if (CustomDataUtil.hasNbt(stack)) {
                return CustomDataUtil.getNbt(stack);
            }
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
    public Text getDisplayName(DisplayNameArgs args) {
        return TextUtil.translatable("screen.enhancedquarries.dropped_item_removal_module_edit.title");
    }

    @Override
    public void writeExtraData(ExtraDataArgs args) {
        Player player = new Player(args.getPlayer());
        NbtCompound nbt = getNbtFromCurrentStack(player);

        args.writeVar(nbt);
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        Optional<ItemStack> optional = new Player(player).getCurrentHandItem();
        return optional.map(stack -> new DropRemovalModuleScreenHandler(syncId, playerInventory, stack))
                .orElseGet(() -> new DropRemovalModuleScreenHandler(syncId, playerInventory));
    }
}
