package net.pitan76.enhancedquarries.util;

import net.minecraft.block.Block;
import net.pitan76.mcpitanlib.api.util.block.BlockUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// TODO: とりあえず、破壊不可ブロックを定義しているが、破壊できないブロックを動的に取得する調査する必要がある or もしくはクァーリーのブロック破壊処理を変更する
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

    // クォーリーは走査する座標ごとに呼ぶので、毎回ID文字列に変換すると重い。ブロックごとに結果をキャッシュする
    private static final Map<Block, Boolean> CACHE = new ConcurrentHashMap<>();

    public static boolean isUnbreakable(Block block) {
        return CACHE.computeIfAbsent(block, b -> isUnbreakable(BlockUtil.toIdAsString(b)));
    }
}
