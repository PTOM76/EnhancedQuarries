package net.pitan76.enhancedquarries.tile.base;

import net.pitan76.mcpitanlib.midohra.util.math.BlockPos;

// 範囲を持ち、枠線を描画できるタイル
public interface RangeTile {

    BlockPos getPos1();

    BlockPos getPos2();
}
