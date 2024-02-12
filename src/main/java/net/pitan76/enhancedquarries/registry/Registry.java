package net.pitan76.enhancedquarries.registry;

import net.pitan76.enhancedquarries.item.base.FillerModule;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class Registry {
    private static final Registry INSTANCE = new Registry();
    private final List<RegistryData> modules = new ArrayList<>();

    public static Registry getINSTANCE() {
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

    public static FillerModule register(Identifier identifier, FillerModule fillerModule) {
        getINSTANCE().modules.add(new RegistryData(fillerModule, identifier));
        return fillerModule;
    }

    public static boolean unregister(Identifier identifier) {
        for (RegistryData data : getINSTANCE().modules) {
            if (data.identifier.equals(identifier)) {
                return getINSTANCE().modules.remove(data);
            }
        }
        return false;
    }

    public static class RegistryData {
        public FillerModule module;
        public Identifier identifier;
        public RegistryData(FillerModule m, Identifier id) {
            module = m;
            identifier = id;
        }
    }
}
