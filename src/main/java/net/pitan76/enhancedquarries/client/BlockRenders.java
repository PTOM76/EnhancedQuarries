package net.pitan76.enhancedquarries.client;

import net.pitan76.enhancedquarries.Blocks;
import net.pitan76.mcpitanlib.api.client.registry.CompatRegistryClient;

public class BlockRenders {
    public static void init() {
        CompatRegistryClient.registerCutoutBlock(Blocks.FRAME);
    }
}
