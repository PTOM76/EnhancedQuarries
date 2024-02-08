package ml.pkom.enhancedquarries.registry;

import ml.pkom.enhancedquarries.item.base.FillerModuleItem;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class Registry {
    private static final Registry INSTANCE = new Registry();
    private final List<RegistryData> modules = new ArrayList<>();

    public static Registry getINSTANCE() {
        return INSTANCE;
    }

    public List<FillerModuleItem> getModules() {
        List<FillerModuleItem> list = new ArrayList<>();
        for (RegistryData data : modules) {
            list.add(data.module);
        }
        return list;
    }

    public List<RegistryData> getRegistryData() {
        return modules;
    }

    public static FillerModuleItem register(Identifier identifier, FillerModuleItem fillerModule) {
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
        public FillerModuleItem module;
        public Identifier identifier;
        public RegistryData(FillerModuleItem m, Identifier id) {
            module = m;
            identifier = id;
        }
    }
}
