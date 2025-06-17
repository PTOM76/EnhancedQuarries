package net.pitan76.enhancedquarries.tile;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.BlockPos;
import net.pitan76.enhancedquarries.Tiles;
import net.pitan76.enhancedquarries.block.Frame;
import net.pitan76.enhancedquarries.item.quarrymodule.ModuleItems;
import net.pitan76.enhancedquarries.tile.base.QuarryTile;
import net.pitan76.enhancedquarries.util.UnbreakableBlocks;
import net.pitan76.mcpitanlib.api.block.CompatBlocks;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.event.nbt.ReadNbtArgs;
import net.pitan76.mcpitanlib.api.event.nbt.WriteNbtArgs;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.mcpitanlib.api.util.math.BoxUtil;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;

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

        if (getMinPos() == null)
            setMinPos(getDefaultRangeMinPos());
        if (getMaxPos() == null)
            setMaxPos(getDefaultRangeMaxPos());

        BlockPos minPos = getMinPos();
        BlockPos maxPos = getMaxPos();
        if (procX == null || procY == null || procZ == null) {
            procX = PosUtil.x(minPos);
            procY = PosUtil.y(maxPos);
            procZ = PosUtil.z(minPos);
        }

        if (procY <= WorldUtil.getBottomY(callGetWorld())) {
            finishedQuarry = true;
            return false;
        }
        if (PosUtil.y(minPos) - 1 >= procY) {
            if (procX < PosUtil.x(maxPos) - 1) {
                if (procZ < PosUtil.z(maxPos) - 1) {
                    BlockPos procPos = PosUtil.flooredBlockPos(procX, procY, procZ);
                    if (WorldUtil.getBlockState(callGetWorld(), procPos) == null) return false;

                    BlockEntity blockEntity = WorldUtil.getBlockEntity(callGetWorld(), procPos);
                    if (blockEntity == this)
                        continueQuarrying();

                    Block procBlock = WorldUtil.getBlockState(callGetWorld(), procPos).getBlock();
                    if (procBlock instanceof AirBlock || (UnbreakableBlocks.isUnbreakable(procBlock) && !hasModuleItem(ModuleItems.BEDROCK_BREAK_MODULE))) {
                        if (canReplaceFluid()) {
                            double time = tryFluidReplace(procPos);
                            if (time != 0) {
                                useEnergy((long) time * getReplaceFluidEnergyCost());
                            }
                        }
                        return continueQuarrying();
                    }
                    if (PosUtil.y(minPos) >= procY) {
                        if (procBlock instanceof FluidBlock) {
                            if (canReplaceFluid()
                                    && FluidUtil.isStill(WorldUtil.getFluidState(callGetWorld(), procPos))
                                    && getEnergy() > getReplaceFluidEnergyCost()) {
                                WorldUtil.setBlockState(callGetWorld(), procPos, BlockStateUtil.getDefaultState(getReplaceFluidWithBlock()));
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
                            tryDeleteMob(procPos);
                        }
                        if (hasModuleItem(ModuleItems.MOB_KILL_MODULE)) {
                            tryKillMob(procPos);
                        }
                        if (hasModuleItem(ModuleItems.EXP_COLLECT_MODULE)) {
                            tryCollectExp(procPos);
                        }

                        breakBlock(procPos, true);
                        List<ItemEntity> entities = WorldUtil.getEntitiesByType(callGetWorld(), EntityType.ITEM, BoxUtil.createBox(PosUtil.flooredBlockPos(procX - 1, procY - 1, procZ - 1), PosUtil.flooredBlockPos(procX + 1, procY + 1, procZ + 1)), EntityPredicates.VALID_ENTITY);
                        if (entities.isEmpty()) return true;
                        for (ItemEntity itemEntity : entities) {
                            addStack(itemEntity.getStack());
                            EntityUtil.kill(itemEntity);
                        }
                        procZ++;
                        return true;
                    }
                } else {
                    procZ = getMinPos().getZ() + 1;
                    procZ--; // continue先でprocZ++されるためここで追加しておく。
                    procX++;
                    return continueQuarrying();
                }
            } else {
                procX = getMinPos().getX() + 1;
                procZ--;
                procY--;
                return continueQuarrying();
            }
            //procZ++;
        } else if (PosUtil.y(minPos) <= procY && PosUtil.y(maxPos) >= procY) {
            if (procX < PosUtil.x(maxPos)) {
                if (procZ < PosUtil.z(maxPos)) {
                    // procX < pos2.getX()を=<するとposのずれ問題は修正可能だが、別の方法で対処しているので、時間があればこっちで修正したい。
                    BlockPos procPos = PosUtil.flooredBlockPos(procX, procY, procZ);
                    BlockState state = WorldUtil.getBlockState(callGetWorld(), procPos);
                    if (state == null)
                        return false;

                    Block procBlock = state.getBlock();
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
                        tryDeleteMob(procPos);

                    if (hasModuleItem(ModuleItems.MOB_KILL_MODULE))
                        tryKillMob(procPos);

                    if (hasModuleItem(ModuleItems.EXP_COLLECT_MODULE))
                        tryCollectExp(procPos);

                    if (procBlock instanceof FluidBlock && !FluidUtil.isStill(WorldUtil.getFluidState(callGetWorld(), procPos)))
                        return continueQuarrying();

                    if (procBlock instanceof Frame) return continueQuarrying();

                    breakBlock(procPos, false);
                    return true;
                } else {
                    procZ = getMinPos().getZ();
                    procX++;
                    procZ--;
                    return continueQuarrying();
                }
            } else {
                procX = getMinPos().getX();
                procZ--;
                procY--;
                if (PosUtil.y(minPos) - 1 >= procY) {
                    procX = PosUtil.x(minPos) + 1;
                    procZ = PosUtil.z(minPos) + 1;
                }
                return continueQuarrying();
            }
        }
        return false;
    }

    @Override
    public void writeNbt(WriteNbtArgs args) {
        NbtCompound nbt = args.getNbt();

        if (procX != null && procY != null && procZ != null)
            NbtUtil.setBlockPos(nbt, "procPos", PosUtil.flooredBlockPos(procX, procY, procZ));
        NbtUtil.putBoolean(nbt, "finished", finishedQuarry);
    }

    @Override
    public void readNbt(ReadNbtArgs args) {
        NbtCompound nbt = args.getNbt();
        if (!NbtUtil.has(nbt, "procPos")) return;

        BlockPos procPos = NbtUtil.getBlockPos(nbt, "procPos");
        procX = PosUtil.x(procPos);
        procY = PosUtil.y(procPos);
        procZ = PosUtil.z(procPos);

        if (NbtUtil.has(nbt, "finished")) finishedQuarry = NbtUtil.getBoolean(nbt, "finished");
    }
}
