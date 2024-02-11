package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.Tiles;
import ml.pkom.enhancedquarries.block.Frame;
import ml.pkom.enhancedquarries.tile.base.QuarryTile;
import ml.pkom.mcpitanlibarch.api.event.block.TileCreateEvent;
import ml.pkom.mcpitanlibarch.api.util.math.BoxUtil;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.BlockPos;

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
            procZ--;
            return tryQuarrying();
        } catch (StackOverflowError e) {
            return false;
        }
    }

    @Override
    public boolean tryQuarrying() {
        if (finishedQuarry) return true;
        if (getWorld() == null || getWorld().isClient()) return false;
        if (getPos1() == null)
            setPos1(getDefaultRangePos1());
        if (getPos2() == null)
            setPos2(getDefaultRangePos2());
        BlockPos pos1 = getPos1();
        BlockPos pos2 = getPos2();
        if (procX == null || procY == null || procZ == null) {
            procX = pos1.getX();
            procY = pos2.getY();
            procZ = pos1.getZ();
        }
        if (procY <= getWorld().getBottomY()) {
            finishedQuarry = true;
            return false;
        }
        if (pos1.getY() - 1 >= procY) {
            if (procX < pos2.getX() - 1) {
                if (procZ > pos2.getZ() + 1) {
                    BlockPos procPos = new BlockPos(procX, procY, procZ);
                    if (getWorld().getBlockState(procPos) == null) return false;
                    if (getWorld().getBlockEntity(procPos) instanceof QuarryTile && getWorld().getBlockEntity(procPos) == this) continueQuarrying();
                    Block procBlock = getWorld().getBlockState(procPos).getBlock();
                    if (procBlock instanceof AirBlock || (procBlock.equals(Blocks.BEDROCK) && !canBedrockBreak())) {
                        if (canReplaceFluid()) {
                            double time = tryFluidReplace(procPos);
                            if (time != 0) {
                                useEnergy((long) time * getReplaceFluidEnergyCost());
                            }
                        }
                        return continueQuarrying();
                    }
                    if (pos1.getY() >= procY) {
                        if (procBlock instanceof FluidBlock) {
                            if (canReplaceFluid()
                                    && getWorld().getFluidState(procPos).isStill()
                                    && getEnergy() > getReplaceFluidEnergyCost()) {
                                getWorld().setBlockState(procPos, getReplaceFluidWithBlock().getDefaultState());
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
                        if (isSetMobDelete()) {
                            tryDeleteMob(procPos);
                        }
                        if (isSetMobKill()) {
                            tryKillMob(procPos);
                        }
                        breakBlock(procPos, true);
                        List<ItemEntity> entities = getWorld().getEntitiesByType(EntityType.ITEM, BoxUtil.createBox(new BlockPos(procX - 1, procY - 1, procZ - 1), new BlockPos(procX + 1, procY + 1, procZ + 1)), EntityPredicates.VALID_ENTITY);
                        if (entities.isEmpty()) return true;
                        for (ItemEntity itemEntity : entities) {
                            addStack(itemEntity.getStack());
                            itemEntity.kill();
                        }
                        procZ--;
                        return true;
                    }
                } else {
                    procZ = getPos1().getZ() - 1;
                    procZ++; // continue先でprocZ--されるためここで追加しておく。
                    procX++;
                    return continueQuarrying();
                }
            } else {
                procX = getPos1().getX() + 1;
                procZ++;
                procY--;
                return continueQuarrying();
            }
            //procZ--;
        } else if (pos1.getY() <= procY && pos2.getY() >= procY) {
            if (procX < pos2.getX()) {
                if (procZ > pos2.getZ()) {
                    // procX < pos2.getX()を=<するとposのずれ問題は修正可能だが、別の方法で対処しているので、時間があればこっちで修正したい。
                    BlockPos procPos = new BlockPos(procX, procY, procZ);
                    if (getWorld().getBlockState(procPos) == null) return false;
                    Block procBlock = getWorld().getBlockState(procPos).getBlock();
                    if (procBlock instanceof AirBlock || (procBlock.equals(Blocks.BEDROCK) && !canBedrockBreak())) {
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
                    if (isSetMobDelete()) {
                        tryDeleteMob(procPos);
                    }
                    if (isSetMobKill()) {
                        tryKillMob(procPos);
                    }
                    if (procBlock instanceof FluidBlock && !getWorld().getFluidState(procPos).isStill()) {
                        return continueQuarrying();
                    }
                    if (procBlock instanceof Frame) return continueQuarrying();
                    breakBlock(procPos, false);
                    return true;
                } else {
                    procZ = getPos1().getZ();
                    procX++;
                    procZ++;
                    return continueQuarrying();
                }
            } else {
                procX = getPos1().getX();
                procZ++;
                procY--;
                if (pos1.getY() - 1 >= procY) {
                    procX = pos1.getX() + 1;
                    procZ = pos1.getZ() - 1;
                }
                return continueQuarrying();
            }
        }
        return false;
    }

    @Override
    public void writeNbtOverride(NbtCompound tag) {
        NbtCompound procPos = new NbtCompound();
        if (procX != null) procPos.putInt("x", procX);
        if (procY != null) procPos.putInt("y", procY);
        if (procZ != null) procPos.putInt("z", procZ);
        tag.put("procPos", procPos);
        tag.putBoolean("finished", finishedQuarry);
        super.writeNbtOverride(tag);
    }

    @Override
    public void readNbtOverride(NbtCompound tag) {
        super.readNbtOverride(tag);
        if (!tag.contains("procPos")) return;
        NbtCompound procPos = tag.getCompound("procPos");
        if (procPos.contains("x")) procX = procPos.getInt("x");
        if (procPos.contains("y")) procY = procPos.getInt("y");
        if (procPos.contains("z")) procZ = procPos.getInt("z");
        if (tag.contains("finished")) finishedQuarry = tag.getBoolean("finished");
    }
}
