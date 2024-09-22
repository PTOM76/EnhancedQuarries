package net.pitan76.enhancedquarries.tile;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
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
        if (getWorld() == null || getWorld().isClient()) return false;

        if (getMinPos() == null)
            setMinPos(getDefaultRangeMinPos());
        if (getMaxPos() == null)
            setMaxPos(getDefaultRangeMaxPos());

        BlockPos minPos = getMinPos();
        BlockPos maxPos = getMaxPos();
        if (procX == null || procY == null || procZ == null) {
            procX = minPos.getX();
            procY = maxPos.getY();
            procZ = minPos.getZ();
        }

        if (procY <= WorldUtil.getBottomY(getWorld())) {
            finishedQuarry = true;
            return false;
        }
        if (minPos.getY() - 1 >= procY) {
            if (procX < maxPos.getX() - 1) {
                if (procZ < maxPos.getZ() - 1) {
                    BlockPos procPos = PosUtil.flooredBlockPos(procX, procY, procZ);
                    if (getWorld().getBlockState(procPos) == null) return false;
                    if (getWorld().getBlockEntity(procPos) instanceof QuarryTile && getWorld().getBlockEntity(procPos) == this) continueQuarrying();
                    Block procBlock = getWorld().getBlockState(procPos).getBlock();
                    if (procBlock instanceof AirBlock || (procBlock.equals(Blocks.BEDROCK) && !hasModuleItem(ModuleItems.BEDROCK_BREAK_MODULE))) {
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
                                    && FluidUtil.isStill(WorldUtil.getFluidState(getWorld(), procPos))
                                    && getEnergy() > getReplaceFluidEnergyCost()) {
                                getWorld().setBlockState(procPos, BlockStateUtil.getDefaultState(getReplaceFluidWithBlock()));
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
                        List<ItemEntity> entities = getWorld().getEntitiesByType(EntityType.ITEM, BoxUtil.createBox(PosUtil.flooredBlockPos(procX - 1, procY - 1, procZ - 1), PosUtil.flooredBlockPos(procX + 1, procY + 1, procZ + 1)), EntityPredicates.VALID_ENTITY);
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
        } else if (minPos.getY() <= procY && maxPos.getY() >= procY) {
            if (procX < maxPos.getX()) {
                if (procZ < maxPos.getZ()) {
                    // procX < pos2.getX()を=<するとposのずれ問題は修正可能だが、別の方法で対処しているので、時間があればこっちで修正したい。
                    BlockPos procPos = PosUtil.flooredBlockPos(procX, procY, procZ);
                    if (getWorld().getBlockState(procPos) == null) return false;
                    Block procBlock = getWorld().getBlockState(procPos).getBlock();
                    if (procBlock instanceof AirBlock || (procBlock.equals(Blocks.BEDROCK) && !hasModuleItem(ModuleItems.BEDROCK_BREAK_MODULE))) {
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

                    if (procBlock instanceof FluidBlock && !FluidUtil.isStill(WorldUtil.getFluidState(getWorld(), procPos)))
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
        procX = procPos.getX();
        procY = procPos.getY();
        procZ = procPos.getZ();

        if (NbtUtil.has(nbt, "finished")) finishedQuarry = NbtUtil.getBoolean(nbt, "finished");
    }
}
