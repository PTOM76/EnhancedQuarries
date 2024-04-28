package net.pitan76.enhancedquarries.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.pitan76.mcpitanlib.api.util.WorldUtil;

public class FillerModuleRange {
    public final int start;
    public final int end;

    private FillerModuleRange(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public static FillerModuleRange between(int startY, World world) {
        return new FillerModuleRange(startY, WorldUtil.getTopY(world));
    }
    public static FillerModuleRange between(BlockPos start, World world) {
        return between(start.getY(), world);
    }

    public static FillerModuleRange between(World world, int endY) {
        return new FillerModuleRange(WorldUtil.getBottomY(world), endY);
    }
    public static FillerModuleRange between(World world, BlockPos end) {
        return between(world, end.getY());
    }

    public static FillerModuleRange between(int startY, int endY) {
        return new FillerModuleRange(startY, endY);
    }
    public static FillerModuleRange between(BlockPos start, BlockPos end) {
        return between(start.getY(), end.getY());
    }

    public static FillerModuleRange singleLayer(BlockPos pos) {
        return new FillerModuleRange(pos.getY(), pos.getY());
    }
    public static FillerModuleRange singleLayer(int posY) {
        return new FillerModuleRange(posY, posY);
    }
}