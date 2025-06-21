package net.pitan76.enhancedquarries.item.base;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.pitan76.enhancedquarries.tile.base.FillerTile;
import net.pitan76.enhancedquarries.tile.base.QuarryTile;
import net.pitan76.mcpitanlib.api.enchantment.CompatEnchantment;
import net.pitan76.mcpitanlib.api.event.item.ItemUseOnBlockEvent;
import net.pitan76.mcpitanlib.api.item.v2.CompatItem;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.text.TextComponent;
import net.pitan76.mcpitanlib.api.util.CompatActionResult;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.TextUtil;
import net.pitan76.mcpitanlib.api.util.item.ItemUtil;

import java.util.Map;

public abstract class MachineModule extends CompatItem {

    public MachineModule(CompatibleItemSettings settings) {
        super(settings);
    }

    /**
     * If conflict modules, return true.
     * @param e ItemUseOnBlockEvent
     * @param module Checking module
     * @return isConflict?
     */
    public boolean checkConflict(ItemUseOnBlockEvent e, MachineModule module) {
        return false;
    }

    public abstract boolean allowMultiple();

    @Override
    public CompatActionResult onRightClickOnBlock(ItemUseOnBlockEvent e) {
        if (e.isClient()) return super.onRightClickOnBlock(e);

        BlockEntity blockEntity = e.getBlockEntity();
        if (blockEntity instanceof QuarryTile) {
            QuarryTile quarry = (QuarryTile) blockEntity;
            CompatActionResult result = onRightClickOnQuarry(e, quarry);
            if (result == null)
                result = super.onRightClickOnBlock(e);

            return result;
        }

        if (blockEntity instanceof FillerTile) {
            FillerTile filler = (FillerTile) blockEntity;
            CompatActionResult result = onRightClickOnFiller(e, filler);
            if (result == null)
                result = super.onRightClickOnBlock(e);

            return result;
        }

        return e.success();
    }

    public CompatActionResult onRightClickOnQuarry(ItemUseOnBlockEvent e, QuarryTile quarry) {
        if (!allowMultiple() && quarry.hasModuleItem(this)) {
            e.getPlayer().sendMessage(getAlreadyAppliedMessage().getText());
            return e.pass();
        }

        for (ItemStack stack : quarry.getModuleStacks()) {
            Item item = ItemStackUtil.getItem(stack);
            if (!(item instanceof MachineModule)) continue;
            if (checkConflict(e, (MachineModule) item)) {
                return e.pass();
            }
        }

        e.player.sendMessage(TextUtil.translatable("message.enhanced_quarries.appended_module", ItemUtil.getNameAsString(this)));
        quarry.insertModuleStack(e.stack);
        return null;
    }

    public CompatActionResult onRightClickOnFiller(ItemUseOnBlockEvent e, FillerTile filler) {
        return null;
    }

    public TextComponent getAlreadyAppliedMessage() {
        return TextComponent.translatable("message.enhanced_quarries.already_applied", ItemUtil.getNameAsString(this));
    }

    public Map<CompatEnchantment, Integer> getEnchantments() {
        return null;
    }
}
