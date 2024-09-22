package net.pitan76.enhancedquarries.item.base;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import net.pitan76.enhancedquarries.tile.base.FillerTile;
import net.pitan76.enhancedquarries.tile.base.QuarryTile;
import net.pitan76.mcpitanlib.api.enchantment.CompatEnchantment;
import net.pitan76.mcpitanlib.api.event.item.ItemUseOnBlockEvent;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.ExtendItem;
import net.pitan76.mcpitanlib.api.util.WorldUtil;

import java.util.Map;

public abstract class MachineModule extends ExtendItem {

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
    public ActionResult onRightClickOnBlock(ItemUseOnBlockEvent e) {
        World world = e.getWorld();
        if (WorldUtil.isClient(world)) return super.onRightClickOnBlock(e);

        BlockEntity blockEntity = e.getBlockEntity();

        if (blockEntity instanceof QuarryTile) {
            QuarryTile quarry = (QuarryTile) blockEntity;
            ActionResult result = onRightClickOnQuarry(e, quarry);
            if (result == null)
                result = super.onRightClickOnBlock(e);

            return result;
        }

        if (blockEntity instanceof FillerTile) {
            FillerTile filler = (FillerTile) blockEntity;
            ActionResult result = onRightClickOnFiller(e, filler);
            if (result == null)
                result = super.onRightClickOnBlock(e);

            return result;
        }

        return e.success();
    }

    public ActionResult onRightClickOnQuarry(ItemUseOnBlockEvent e, QuarryTile quarry) {
        if (!allowMultiple() && quarry.hasModuleItem(this)) {
            e.getPlayer().sendMessage(getAlreadyAppliedMessage());
            return e.pass();
        }

        for (ItemStack stack : quarry.getModuleStacks()) {
            if (!(stack.getItem() instanceof MachineModule)) continue;
            if (checkConflict(e, (MachineModule) stack.getItem())) {
                return e.pass();
            }
        }

        quarry.insertModuleStack(e.stack);
        return null;
    }

    public ActionResult onRightClickOnFiller(ItemUseOnBlockEvent e, FillerTile filler) {
        return null;
    }

    public Text getAlreadyAppliedMessage() {
        return Text.translatable("message.enhanced_quarries.already_applied", getName());
    }

    public Map<CompatEnchantment, Integer> getEnchantments() {
        return null;
    }
}
