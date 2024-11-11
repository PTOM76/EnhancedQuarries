package net.pitan76.enhancedquarries.item.quarrymodule;

import net.pitan76.enhancedquarries.item.base.MachineModule;
import net.pitan76.mcpitanlib.api.enchantment.CompatEnchantment;
import net.pitan76.mcpitanlib.api.item.v2.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.api.util.EnchantmentUtil;

import java.util.HashMap;
import java.util.Map;

public class LuckEnchantModule extends MachineModule {
    public LuckEnchantModule(CompatibleItemSettings settings) {
        super(settings);
    }

    @Override
    public boolean allowMultiple() {
        return false;
    }

    @Override
    public Map<CompatEnchantment, Integer> getEnchantments() {
        Map<CompatEnchantment, Integer> enchantments = new HashMap<>();
        enchantments.put(EnchantmentUtil.getEnchantment(CompatIdentifier.of("fortune")), 3);

        return enchantments;
    }
}
