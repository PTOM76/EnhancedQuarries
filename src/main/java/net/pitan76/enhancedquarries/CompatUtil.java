package net.pitan76.enhancedquarries;

import net.pitan76.mcpitanlib.api.util.MCVersionUtil;
import net.pitan76.mcpitanlib.api.util.PlatformUtil;

public class CompatUtil {
    public static boolean isUsableRebornCore() {
        return PlatformUtil.isModLoaded("reborncore") &&
                MCVersionUtil.getProtocolVersion() < 769;
    }
}
