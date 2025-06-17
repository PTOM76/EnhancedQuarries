package net.pitan76.enhancedquarries.util;

import net.minecraft.block.Block;
import net.pitan76.mcpitanlib.api.util.block.BlockUtil;

public class UnbreakableBlocks {
    public static final String[] UNBREAKABLE_BLOCKS = {
        "minecraft:bedrock",
        "minecraft:end_gateway",
        "minecraft:end_portal",
        "minecraft:end_portal_frame",
        "minecraft:command_block",
        "minecraft:chain_command_block",
        "minecraft:repeating_command_block",
        "minecraft:structure_block",
        "minecraft:barrier",
        "minecraft:structure_void",
        "minecraft:light_block"
    };

    public static boolean isUnbreakable(String blockId) {
        for (String unbreakableBlock : UNBREAKABLE_BLOCKS) {
            if (unbreakableBlock.equals(blockId)) return true;
        }
        return false;
    }

    public static boolean isUnbreakable(Block block) {
        return isUnbreakable(BlockUtil.toIdAsString(block));
    }
}
