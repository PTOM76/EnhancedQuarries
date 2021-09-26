package ml.pkom.enhancedquarries.tile;

import ml.pkom.enhancedquarries.Tiles;
import ml.pkom.enhancedquarries.block.Frame;
import ml.pkom.enhancedquarries.event.TileCreateEvent;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.List;

public class OptimumQuarryTile extends NormalQuarryTile {

    public OptimumQuarryTile() {
        super(Tiles.OPTIMUM_QUARRY_TILE);
    }

    public OptimumQuarryTile(BlockEntityType<?> type) {
        super(type);
    }

    public OptimumQuarryTile(TileCreateEvent event) {
        this();
    }

    Integer procX = null;
    Integer procY = null;
    Integer procZ = null;

    private boolean continueQuarrying() {
        procZ--;
        return tryQuarrying();
    }

    @Override
    public boolean tryQuarrying() {
        if (getWorld() == null || getWorld().isClient()) return false;
        if (getPos1() == null)
            setPos1(getRangePos1());
        if (getPos2() == null)
            setPos2(getRangePos2());
        BlockPos pos1 = getPos1();
        BlockPos pos2 = getPos2();
        if (procX == null || procY == null || procZ == null) {
            procX = pos1.getX();
            procY = pos2.getY();
            procZ = pos1.getZ();
        }
        if (pos1.getY() - 1 >= procY) {
            if (procX < pos2.getX() - 1) {
                if (procZ > pos2.getZ() + 1) {
                    BlockPos procPos = new BlockPos(procX, procY, procZ);
                    if (getWorld().getBlockState(procPos) == null) return continueQuarrying();
                    Block procBlock = getWorld().getBlockState(procPos).getBlock();
                    if (procBlock instanceof AirBlock || (procBlock.equals(Blocks.BEDROCK) && !canBedrockBreak())) {
                        if (canReplaceFluid()) {
                            double time = tryFluidReplace(procPos);
                            if (time != 0) {
                                useEnergy(time * getReplaceFluidEnergyCost());
                            }
                        }
                        return continueQuarrying();
                    }
                    if (pos1.getY() >= procY) {
                        if (procBlock instanceof FluidBlock) {
                            if (canReplaceFluid()
                                    && getWorld().getFluidState(procPos).isStill()
                                    && getEnergy() > getEuPerTick(getReplaceFluidEnergyCost())) {
                                getWorld().setBlockState(procPos, getReplaceFluidWithBlock().getDefaultState());
                                useEnergy(getReplaceFluidEnergyCost());
                            } else {
                                return continueQuarrying();
                            }
                        }
                        if (canReplaceFluid()) {
                            double time = tryFluidReplace(procPos);
                            if (time != 0) {
                                useEnergy(time * getReplaceFluidEnergyCost());
                            }
                        }
                        if (isSetMobDelete()) {
                            tryDeleteMob(procPos);
                        }
                        if (isSetMobKill()) {
                            tryKillMob(procPos);
                        }
                        breakBlock(procPos, true);
                        List<ItemEntity> entities = getWorld().getEntitiesByType(EntityType.ITEM, new Box(new BlockPos(procX - 1, procY - 1, procZ - 1), new BlockPos(procX + 1, procY + 1, procZ + 1)), EntityPredicates.VALID_ENTITY);
                        if (entities.isEmpty()) return true;
                        for (ItemEntity itemEntity : entities) {
                            addStack(itemEntity.getStack());
                            itemEntity.kill();
                        }
                        procZ--;
                        return true;
                    }
                } else {
                    procZ = getPos1().getZ();
                    procX++;
                    return continueQuarrying();
                }
            } else {
                procX = getPos1().getX();
                procY--;
                return continueQuarrying();
            }
            //procZ--;
        } else if (pos1.getY() <= procY && pos2.getY() >= procY) {
            if (procX < pos2.getX()) {
                if (procZ > pos2.getZ()) {
                    // procX < pos2.getX()を=<するとposのずれ問題は修正可能だが、別の方法で対処しているので、時間があればこっちで修正したい。
                    BlockPos procPos = new BlockPos(procX, procY, procZ);
                    if (getWorld().getBlockState(procPos) == null) continueQuarrying();
                    Block procBlock = getWorld().getBlockState(procPos).getBlock();
                    if (procBlock instanceof AirBlock || (procBlock.is(Blocks.BEDROCK) && !canBedrockBreak())) {
                        if (tryPlaceFrame(procPos)) {
                            useEnergy(getPlaceFrameEnergyCost());
                            return false;
                        }
                        if (canReplaceFluid()) {
                            double time = tryFluidReplace(procPos);
                            if (time != 0) {
                                useEnergy(time * getReplaceFluidEnergyCost());
                            }
                        }
                        return continueQuarrying();
                    }
                    if (canReplaceFluid()) {
                        double time = tryFluidReplace(procPos);
                        if (time != 0) {
                            useEnergy(time * getReplaceFluidEnergyCost());
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
                    procZ--;
                    return true;
                } else {
                    procZ = getPos1().getZ() + 1;
                    procX++;
                    return continueQuarrying();
                }
            } else {
                procX = getPos1().getX() - 1;
                procY--;
                return continueQuarrying();
            }
        }
        return false;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        NbtCompound procPos = new NbtCompound();
        if (procX != null) procPos.putInt("x", procX);
        if (procY != null) procPos.putInt("y", procY);
        if (procZ != null) procPos.putInt("z", procZ);
        tag.put("procPos", procPos);
        return super.writeNbt(tag);
    }

    @Override
    public void fromTag(BlockState blockState, NbtCompound tag) {
        super.fromTag(blockState, tag);
        if (!tag.contains("procPos")) return;
        NbtCompound procPos = tag.getCompound("procPos");
        if (procPos.contains("x")) procX = procPos.getInt("x");
        if (procPos.contains("y")) procY = procPos.getInt("y");
        if (procPos.contains("z")) procZ = procPos.getInt("z");
    }
}
