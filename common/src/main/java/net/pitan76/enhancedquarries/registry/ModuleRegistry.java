package net.pitan76.enhancedquarries.registry;

import net.pitan76.enhancedquarries.item.base.FillerModule;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;

import java.util.ArrayList;
import java.util.List;

public class ModuleRegistry {
    private static final ModuleRegistry INSTANCE = new ModuleRegistry();
    private final List<RegistryData> modules = new ArrayList<>();

    public static ModuleRegistry getINSTANCE() {
        return INSTANCE;
    }

    public List<FillerModule> getModules() {
        List<FillerModule> list = new ArrayList<>();
        for (RegistryData data : modules) {
            list.add(data.module);
        }
        return list;
    }

    public List<RegistryData> getRegistryData() {
        return modules;
    }

    public static FillerModule register(CompatIdentifier identifier, FillerModule fillerModule) {
        getINSTANCE().modules.add(new RegistryData(fillerModule, identifier));
        return fillerModule;
    }

    public static boolean unregister(CompatIdentifier identifier) {
        for (RegistryData data : getINSTANCE().modules) {
            if (data.identifier.equals(identifier)) {
                return getINSTANCE().modules.remove(data);
            }
        }
        return false;
    }

    public static class RegistryData {
        public FillerModule module;
        public CompatIdentifier identifier;
        public RegistryData(FillerModule m, CompatIdentifier id) {
            module = m;
            identifier = id;
        }
    }
}
