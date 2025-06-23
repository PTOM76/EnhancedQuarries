package net.pitan76.enhancedquarries.tile;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.pitan76.enhancedquarries.Tiles;
import net.pitan76.enhancedquarries.block.Frame;
import net.pitan76.enhancedquarries.item.quarrymodule.ModuleItems;
import net.pitan76.enhancedquarries.util.UnbreakableBlocks;
import net.pitan76.mcpitanlib.api.block.CompatBlocks;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.event.nbt.ReadNbtArgs;
import net.pitan76.mcpitanlib.api.event.nbt.WriteNbtArgs;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.mcpitanlib.api.util.entity.ItemEntityUtil;
import net.pitan76.mcpitanlib.api.util.math.BoxUtil;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;
import net.pitan76.mcpitanlib.api.util.nbt.v2.NbtRWUtil;
import net.pitan76.mcpitanlib.midohra.util.math.BlockPos;
import net.pitan76.mcpitanlib.midohra.world.World;

import java.util.List;

public class OptimumQuarryTile extends NormalQuarryTile {

    public OptimumQuarryTile(BlockEntityType<?> type, TileCreateEvent event) {
        super(type, event);
    }

    public OptimumQuarryTile(TileCreateEvent event) {
        this(Tiles.OPTIMUM_QUARRY_TILE.getOrNull(), event);
    }

    boolean finishedQuarry = false;

    Integer procX = null;
    Integer procY = null;
    Integer procZ = null;

    private boolean continueQuarrying() {
        try {
            procZ++;
            return tryQuarrying();
        } catch (StackOverflowError e) {
            return false;
        }
    }

    @Override
    public boolean tryQuarrying() {
        if (finishedQuarry) return true;
        if (callGetWorld() == null || WorldUtil.isClient(callGetWorld())) return false;
        World world = World.of(callGetWorld());

        if (getMinPos() == null)
            setMinPos(getDefaultRangeMinPos());
        if (getMaxPos() == null)
            setMaxPos(getDefaultRangeMaxPos());

        BlockPos minPos = BlockPos.of(getMinPos());
        BlockPos maxPos = BlockPos.of(getMaxPos());
        if (procX == null || procY == null || procZ == null) {
            procX = minPos.getX();
            procY = maxPos.getY();
            procZ = minPos.getZ();
        }

        if (procY <= world.getBottomY()) {
            finishedQuarry = true;
            return false;
        }
        if (minPos.getY() - 1 >= procY) {
            if (procX < maxPos.getX() - 1) {
                if (procZ < maxPos.getZ() - 1) {
                    BlockPos procPos = PosUtil.flooredMidohraBlockPos(procX, procY, procZ);
                    if (world.getBlockState(procPos) == null) return false;

                    BlockEntity blockEntity = world.getBlockEntity(procPos).get();
                    if (blockEntity == this)
                        continueQuarrying();

                    Block procBlock = world.getBlockState(procPos).getBlock().get();
                    if (procBlock instanceof AirBlock || (UnbreakableBlocks.isUnbreakable(procBlock) && !hasModuleItem(ModuleItems.BEDROCK_BREAK_MODULE))) {
                        if (canReplaceFluid()) {
                            double time = tryFluidReplace(procPos);
                            if (time != 0) {
                                useEnergy((long) time * getReplaceFluidEnergyCost());
                            }
                        }
                        return continueQuarrying();
                    }
                    if (minPos.getY() >= procY) {
                        if (procBlock instanceof FluidBlock) {
                            if (canReplaceFluid()
                                    && FluidUtil.isStill(world.getRawFluidState(procPos))
                                    && getEnergy() > getReplaceFluidEnergyCost()) {
                                world.setBlockState(procPos, BlockStateUtil.getMidohraDefaultState(getReplaceFluidWithBlock()));
                                useEnergy(getReplaceFluidEnergyCost());
                            } else {
                                return continueQuarrying();
                            }
                        }
                        if (canReplaceFluid()) {
                            double time = tryFluidReplace(procPos);
                            if (time != 0) {
                                useEnergy((long) time * getReplaceFluidEnergyCost());
                            }
                        }
                        if (hasModuleItem(ModuleItems.MOB_DELETE_MODULE)) {
                            tryDeleteMob(procPos.toRaw());
                        }
                        if (hasModuleItem(ModuleItems.MOB_KILL_MODULE)) {
                            tryKillMob(procPos.toRaw());
                        }
                        if (hasModuleItem(ModuleItems.EXP_COLLECT_MODULE)) {
                            tryCollectExp(procPos.toRaw());
                        }

                        breakBlock(procPos.toRaw(), true);
                        List<ItemEntity> entities = ItemEntityUtil.getEntities(callGetWorld(), BoxUtil.createBox(PosUtil.flooredBlockPos(procX - 1, procY - 1, procZ - 1), PosUtil.flooredBlockPos(procX + 1, procY + 1, procZ + 1)));
                        if (entities.isEmpty()) return true;
                        for (ItemEntity itemEntity : entities) {
                            addStack(ItemEntityUtil.getStack(itemEntity));
                            EntityUtil.kill(itemEntity);
                        }
                        procZ++;
                        return true;
                    }
                } else {
                    procZ = minPos.getZ() + 1;
                    procZ--; // continue先でprocZ++されるためここで追加しておく。
                    procX++;
                    return continueQuarrying();
                }
            } else {
                procX = minPos.getX() + 1;
                procZ--;
                procY--;
                return continueQuarrying();
            }
            //procZ++;
        } else if (minPos.getY() <= procY && maxPos.getY() >= procY) {
            if (procX < maxPos.getX()) {
                if (procZ < maxPos.getZ()) {
                    // procX < pos2.getX()を=<するとposのずれ問題は修正可能だが、別の方法で対処しているので、時間があればこっちで修正したい。
                    BlockPos procPos = PosUtil.flooredMidohraBlockPos(procX, procY, procZ);
                    BlockState state = world.getBlockState(procPos).toMinecraft();
                    if (state == null)
                        return false;

                    Block procBlock = BlockStateUtil.getBlock(state);
                    if (procBlock instanceof AirBlock || (procBlock.equals(CompatBlocks.BEDROCK) && !hasModuleItem(ModuleItems.BEDROCK_BREAK_MODULE))) {
                        if (tryPlaceFrame(procPos)) {
                            useEnergy(getPlaceFrameEnergyCost());
                            return false;
                        }
                        if (canReplaceFluid()) {
                            double time = tryFluidReplace(procPos);
                            if (time != 0) {
                                useEnergy((long) time * getReplaceFluidEnergyCost());
                            }
                        }
                        return continueQuarrying();
                    }
                    if (canReplaceFluid()) {
                        double time = tryFluidReplace(procPos);
                        if (time != 0) {
                            useEnergy((long) time * getReplaceFluidEnergyCost());
                        }
                    }
                    if (hasModuleItem(ModuleItems.MOB_DELETE_MODULE))
                        tryDeleteMob(procPos.toRaw());

                    if (hasModuleItem(ModuleItems.MOB_KILL_MODULE))
                        tryKillMob(procPos.toRaw());

                    if (hasModuleItem(ModuleItems.EXP_COLLECT_MODULE))
                        tryCollectExp(procPos.toRaw());

                    if (procBlock instanceof FluidBlock && !FluidUtil.isStill(world.getRawFluidState(procPos)))
                        return continueQuarrying();

                    if (procBlock instanceof Frame) return continueQuarrying();

                    breakBlock(procPos.toRaw(), false);
                    return true;
                } else {
                    procZ = minPos.getZ();
                    procX++;
                    procZ--;
                    return continueQuarrying();
                }
            } else {
                procX = minPos.getX();
                procZ--;
                procY--;
                if (minPos.getY() - 1 >= procY) {
                    procX = minPos.getX() + 1;
                    procZ = minPos.getZ() + 1;
                }
                return continueQuarrying();
            }
        }
        return false;
    }

    @Override
    public void writeNbt(WriteNbtArgs args) {
        if (procX != null && procY != null && procZ != null)
            NbtRWUtil.putBlockPos(args, "procPos", PosUtil.flooredBlockPos(procX, procY, procZ));
        NbtRWUtil.putBoolean(args, "finished", finishedQuarry);
    }

    @Override
    public void readNbt(ReadNbtArgs args) {
        BlockPos procPos = NbtRWUtil.getBlockPos(args, "procPos");

        procX = procPos.getX();
        procY = procPos.getY();
        procZ = procPos.getZ();

        finishedQuarry = NbtRWUtil.getBoolean(args, "finished");
    }
}
