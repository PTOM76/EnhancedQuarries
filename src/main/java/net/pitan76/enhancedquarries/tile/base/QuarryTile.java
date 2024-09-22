package net.pitan76.enhancedquarries.tile.base;

import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pitan76.enhancedquarries.block.Frame;
import net.pitan76.enhancedquarries.block.base.Quarry;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.event.nbt.ReadNbtArgs;
import net.pitan76.mcpitanlib.api.event.nbt.WriteNbtArgs;
import net.pitan76.mcpitanlib.api.event.tile.TileTickEvent;
import net.pitan76.mcpitanlib.api.gui.inventory.IInventory;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.mcpitanlib.api.util.math.BoxUtil;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

//@SuppressWarnings("UnstableApiUsage")
public class QuarryTile extends BaseEnergyTile implements IInventory, SidedInventory {
    // Container
    public DefaultedList<ItemStack> invItems = DefaultedList.ofSize(27, ItemStackUtil.empty());

    public IInventory inventory = this;

    // Fluid
    /*
    SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return getMaxStoredExp();
        }

        @Override
        protected boolean canInsert(FluidVariant variant) {
            return false;
        }

        @Override
        protected void onFinalCommit() {
            super.onFinalCommit();
            markDirty();
        }
    };

    public SingleVariantStorage<FluidVariant> getFluidStorage() {
        return fluidStorage;
    }

     */

    // 経験値の量
    private int storedExp = 0;

    // 経験値の最大量
    public int getMaxStoredExp() {
        return 10_000;
    }

    public int getStoredExp() {
        return storedExp;
    }

    public void setStoredExp(int storedExp) {
        this.storedExp = storedExp;
    }

    public void addStoredExp(int storedExp) {
        this.storedExp += storedExp;
    }

    public void removeStoredExp(int storedExp) {
        this.storedExp -= storedExp;
    }

    // モジュール

    // - 岩盤破壊モジュール
    private boolean canBedrockBreak = false;

    public boolean canBedrockBreak() {
        return canBedrockBreak;
    }

    public void setBedrockBreak(boolean canBedrockBreak) {
        this.canBedrockBreak = canBedrockBreak;
    }

    // - シルクタッチモジュール
    private boolean isSetSilkTouch = false;

    public boolean isSetSilkTouch() {
        return isSetSilkTouch;
    }

    public void setSilkTouchModule(boolean isSetSilkTouch) {
        this.isSetSilkTouch = isSetSilkTouch;
    }

    // - 経験値採取モジュール
    private boolean isSetExpCollect = false;

    public boolean isSetExpCollect() {
        return isSetExpCollect;
    }

    public void setExpCollectModule(boolean isSetExpCollect) {
        this.isSetExpCollect = isSetExpCollect;
    }

    // - 幸運モジュール
    private boolean isSetLuck = false;

    public boolean isSetLuck() {
        return isSetLuck;
    }

    public void setLuckModule(boolean isSetLuck) {
        this.isSetLuck = isSetLuck;
    }

    // - モブキルモジュール
    private boolean isSetMobKill = false;

    public boolean isSetMobKill() {
        return isSetMobKill;
    }

    public void setMobKillModule(boolean isSetMobKill) {
        this.isSetMobKill = isSetMobKill;
    }

    // - モブ消去モジュール
    private boolean isSetMobDelete = false;

    public boolean isSetMobDelete() {
        return isSetMobDelete;
    }

    public void setMobDeleteModule(boolean isSetMobDelete) {
        this.isSetMobDelete = isSetMobDelete;
    }

    // ----

    // 液体を置き換えるか？
    public boolean canReplaceFluid() {
        return false;
    }

    // 液体を何に置き換えるか？
    public Block getReplaceFluidWithBlock() {
        return Blocks.GLASS;
    }

    // ブロック1回破壊分に対するエネルギーのコスト
    public long getEnergyCost() {
        return 30;
    }

    // フレーム設置に必要なエネルギーのコスト
    public long getPlaceFrameEnergyCost() {
        return 40;
    }

    // 液体をガラスに置き換えるエネルギーのコスト
    public long getReplaceFluidEnergyCost() {
        return 120;
    }

    // エネルギーの容量
    public long getMaxEnergy() {
        return 5000;
    }

    // エネルギーの最大出力
    public long getMaxOutput() {
        return 0;
    }

    // エネルギーの最大入力
    public long getMaxInput() {
        return 500;
    }
    // ----

    // NBT

    public void writeNbt(WriteNbtArgs args) {
        NbtCompound nbt = args.getNbt();
        InventoryUtil.writeNbt(args, getItems());

        NbtUtil.putDouble(nbt, "coolTime", coolTime);
        if (canBedrockBreak)
            NbtUtil.putBoolean(nbt, "module_bedrock_break", true);
        if (isSetMobKill)
            NbtUtil.putBoolean(nbt, "module_mob_kill", true);
        if (isSetLuck)
            NbtUtil.putBoolean(nbt, "module_luck", true);
        if (isSetSilkTouch)
            NbtUtil.putBoolean(nbt, "module_silk_touch", true);
        if (isSetMobDelete)
            NbtUtil.putBoolean(nbt, "module_mob_delete", true);
        if (isSetExpCollect)
            NbtUtil.putBoolean(nbt, "module_exp_collect", true);

        if (minPos != null) {
            NbtUtil.putInt(nbt, "rangePos1X", getMinPos().getX());
            NbtUtil.putInt(nbt, "rangePos1Y", getMinPos().getY());
            NbtUtil.putInt(nbt, "rangePos1Z", getMinPos().getZ());
        }
        if (maxPos != null) {
            NbtUtil.putInt(nbt, "rangePos2X", getMaxPos().getX());
            NbtUtil.putInt(nbt, "rangePos2Y", getMaxPos().getY());
            NbtUtil.putInt(nbt, "rangePos2Z", getMaxPos().getZ());
        }

        NbtUtil.putInt(nbt, "storedExp", getStoredExp());

        /*
        NbtUtil.put(nbt, "variant", fluidStorage.variant.toNbt());
        tag.putLong("amount", fluidStorage.amount);
         */
    }

    public void readNbt(ReadNbtArgs args) {
        NbtCompound nbt = args.getNbt();
        if (NbtUtil.has(nbt, "Items")) {
            InventoryUtil.readNbt(args, getItems());
        }

        if (NbtUtil.has(nbt, "coolTime")) coolTime = NbtUtil.getDouble(nbt, "coolTime");
        if (NbtUtil.has(nbt, "module_bedrock_break")) canBedrockBreak = NbtUtil.getBoolean(nbt, "module_bedrock_break");
        if (NbtUtil.has(nbt, "module_mob_delete")) isSetMobDelete = NbtUtil.getBoolean(nbt, "module_mob_delete");
        if (NbtUtil.has(nbt, "module_mob_kill")) isSetMobKill = NbtUtil.getBoolean(nbt, "module_mob_kill");
        if (NbtUtil.has(nbt, "module_luck")) isSetLuck = NbtUtil.getBoolean(nbt, "module_luck");
        if (NbtUtil.has(nbt, "module_silk_touch")) isSetSilkTouch = NbtUtil.getBoolean(nbt, "module_silk_touch");
        if (NbtUtil.has(nbt, "module_exp_collect")) isSetExpCollect = NbtUtil.getBoolean(nbt, "module_exp_collect");
        if (NbtUtil.has(nbt, "rangePos1X")
                && NbtUtil.has(nbt, "rangePos1Y")
                && NbtUtil.has(nbt, "rangePos1Z")
                && NbtUtil.has(nbt, "rangePos2X")
                && NbtUtil.has(nbt, "rangePos2Y")
                && NbtUtil.has(nbt, "rangePos2Z")) {
            setMinPos(PosUtil.flooredBlockPos(NbtUtil.getInt(nbt, "rangePos1X"), NbtUtil.getInt(nbt, "rangePos1Y"), NbtUtil.getInt(nbt, "rangePos1Z")));
            setMaxPos(PosUtil.flooredBlockPos(NbtUtil.getInt(nbt, "rangePos2X"), NbtUtil.getInt(nbt, "rangePos2Y"), NbtUtil.getInt(nbt, "rangePos2Z")));
        }

        if (NbtUtil.has(nbt, "storedExp")) setStoredExp(NbtUtil.getInt(nbt, "storedExp"));

        /*
        if (NbtUtil.has(nbt, "variant"))
            fluidStorage.variant = FluidVariant.fromNbt(NbtUtil.get(nbt, "variant"));
        if (NbtUtil.has(nbt, "amount"))
            fluidStorage.amount = tag.getLong("amount");

         */
    }

    // ----

    // 基準の速度
    public double getBasicSpeed() {
        return 5;
    }

    // クールダウンの基準
    public double getSettingCoolTime() {
        return 300;
    }

    public double coolTime = getSettingCoolTime();

    public void tick(TileTickEvent<BaseEnergyTile> e) {
        super.tick(e);
        World world = e.world;
        BlockPos pos = e.pos;
        if (world == null || WorldUtil.isClient(world)) return;

        // レッドストーン受信で無効
        if (WorldUtil.isReceivingRedstonePower(world, pos)) {
            if (isActive())
                Quarry.setActive(false, world, pos);

            return;
        }

        if (getEnergy() > getEnergyCost()) {
            // ここに処理を記入
            if (coolTime <= 0) {
                coolTime = getSettingCoolTime();
                if (tryQuarrying()) {
                    useEnergy(getEnergyCost());
                }
            }
            // エネルギーが多いほどクールダウンが早くなる
            coolTimeBonus();
            coolTime = coolTime - getBasicSpeed();
            if (!isActive()) Quarry.setActive(true, world, pos);

            if (WorldUtil.getTime(world) % 2L == 0L)
                insertChest();
        } else if (isActive()) {
            Quarry.setActive(false, world, pos);
        }
    }

    public int limit = 5;

    public void insertChest() {
        // チェスト自動挿入
        if (getItems().isEmpty()) return;
        List<Direction> dirs = getDirsOfAnyContainerBlock();
        if (dirs.isEmpty()) return;

        int time = 0;
        for (int i = 0; i < getItems().size(); i++) {
            if (time > limit) break;
            for (Direction dir : dirs) {
                ItemStack stack = getItems().get(i);
                if (stack.isEmpty()) continue;

                long amount = StorageUtil.move(InventoryStorage.of(this, null).getSlot(i), ItemStorage.SIDED.find(getWorld(), getPos().offset(dir), dir.getOpposite()), (iv) -> true, Long.MAX_VALUE, null);
                if (amount < stack.getCount()) continue;

                ++time;
                break;
            }
        }
    }

    public List<Direction> getDirsOfAnyContainerBlock() {
        if (getWorld() == null) return new ArrayList<>();

        Direction[] dirs = new Direction[]{Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
        List<Direction> usableDirs = new ArrayList<>();

        for (Direction dir : dirs) {
            BlockPos pos = getPos().offset(dir);
            if (WorldUtil.getBlockEntity(getWorld(), pos) == null) continue;
            usableDirs.add(dir);
        }
        return usableDirs;
    }

    // クールダウンのエネルギー量による追加ボーナス
    public void coolTimeBonus() {
        if (getMaxEnergy() / 1.125 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed() * 5;
        }
        if (getMaxEnergy() / 1.25 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed() * 3;
        }
        if (getMaxEnergy() / 3 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed() * 2;
        }
        if (getMaxEnergy() / 5 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getMaxEnergy() / 7 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getMaxEnergy() / 10 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getMaxEnergy() / 12 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getMaxEnergy() / 15 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getMaxEnergy() / 16 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getMaxEnergy() / 20 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getMaxEnergy() / 30 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getMaxEnergy() / 40 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
    }

    public BlockPos getDefaultRangeMinPos() {
        // default
        switch (getFacing()) {
            case NORTH:
                return PosUtil.flooredBlockPos(getPos().getX() - 5, getPos().getY(), getPos().getZ() + 1);
            case SOUTH:
                return PosUtil.flooredBlockPos(getPos().getX() - 5, getPos().getY(), getPos().getZ() - 11);
            case WEST:
                return PosUtil.flooredBlockPos(getPos().getX() + 1, getPos().getY(), getPos().getZ() - 5);
            case EAST:
                return PosUtil.flooredBlockPos(getPos().getX() - 11, getPos().getY(), getPos().getZ() - 5);
            default:
                return null;
        }
    }

    public BlockPos getDefaultRangeMaxPos() {
        // default
        switch (getFacing()) {
            case NORTH:
                return PosUtil.flooredBlockPos(getPos().getX() + 6, getPos().getY() + 4, getPos().getZ() + 12);
            case SOUTH:
                return PosUtil.flooredBlockPos(getPos().getX() + 6, getPos().getY() + 4, getPos().getZ());

            case WEST:
                return PosUtil.flooredBlockPos(getPos().getX() + 12, getPos().getY() + 4, getPos().getZ() + 6);
            case EAST:
                return PosUtil.flooredBlockPos(getPos().getX(), getPos().getY() + 4, getPos().getZ() + 6);
            default:
                return null;
        }
    }

    // マーカーによる範囲指定を許可するか？
    public boolean canSetPosByMarker() {
        return true;
    }

    private BlockPos minPos = null;
    private BlockPos maxPos = null;

    public BlockPos getMinPos() {
        return minPos;
    }

    public BlockPos getMaxPos() {
        return maxPos;
    }

    public void setMinPos(BlockPos pos1) {
        this.minPos = pos1;
    }

    public void setMaxPos(BlockPos pos2) {
        this.maxPos = pos2;
    }

    public void breakBlock(BlockPos pos, boolean drop) {
        if (WorldUtil.isClient(getWorld())) return;

        if (isSetSilkTouch || isSetLuck) {
            if (drop) {
                List<ItemStack> drops = Block.getDroppedStacks(WorldUtil.getBlockState(getWorld(), pos), (ServerWorld) getWorld(), pos, getWorld().getBlockEntity(pos), null, getQuarryStack());
                drops.forEach(this::addStack);
            }
            WorldUtil.breakBlock(getWorld(), pos, false);
        } else {
            WorldUtil.breakBlock(getWorld(), pos, drop);
        }
    }

    private ItemStack getQuarryStack() {
        ItemStack stack = ItemStackUtil.create(Items.DIAMOND_PICKAXE);
        if (isSetSilkTouch)
            stack.addEnchantment(Enchantments.SILK_TOUCH, 3);
        if (isSetLuck)
            stack.addEnchantment(Enchantments.FORTUNE, 3);
        return stack;
    }

    public void tryDeleteMob(BlockPos blockPos) {
        if (getWorld() == null || getWorld().isClient()) return;
        List<MobEntity> mobs = getWorld().getEntitiesByClass(MobEntity.class, BoxUtil.createBox(blockPos.add(-2, -2, -2), blockPos.add(2, 2, 2)), EntityPredicates.VALID_ENTITY);
        mobs.forEach(Entity::discard);
    }

    public void tryKillMob(BlockPos blockPos) {
        if (getWorld() == null || getWorld().isClient()) return;
        List<MobEntity> mobs = getWorld().getEntitiesByClass(MobEntity.class, BoxUtil.createBox(blockPos.add(-2, -2, -2), blockPos.add(2, 2, 2)), EntityPredicates.VALID_ENTITY);
        mobs.forEach(LivingEntity::kill);
    }

    public void tryCollectExp(BlockPos blockPos) {
        if (getWorld() == null || getWorld().isClient()) return;
        List<ExperienceOrbEntity> entities = getWorld().getEntitiesByClass(ExperienceOrbEntity.class, BoxUtil.createBox(blockPos.add(-2, -2, -2), blockPos.add(2, 2, 2)), EntityPredicates.VALID_ENTITY);
        entities.forEach((entity) -> {
            if (getStoredExp() + entity.getExperienceAmount() > getMaxStoredExp()) return;
            addStoredExp(entity.getExperienceAmount());
            entity.discard();
        });
    }

    public double tryFluidReplace(BlockPos blockPos) {
        double time = 0;
        if(getEnergy() < getReplaceFluidEnergyCost()) {
            return 0;
        }

        for (Direction value : Direction.values()) {
            BlockPos offsetBlockPos = blockPos.offset(value);

            if (WorldUtil.getBlockState(getWorld(), offsetBlockPos).getBlock() instanceof FluidBlock) {
                // replace fluid block
                WorldUtil.setBlockState(getWorld(), offsetBlockPos, getReplaceFluidWithBlock().getDefaultState());
                time += 0.1;
            }

            if(!WorldUtil.getFluidState(getWorld(), offsetBlockPos).isEmpty() || getWorld().getFluidState(offsetBlockPos).isStill()) {
                breakBlock(offsetBlockPos, true);

                List<ItemEntity> entities = getWorld().getEntitiesByType(EntityType.ITEM, BoxUtil.createBox(blockPos.add(-1, -1, -1), blockPos.add( 1,  1, 1)), EntityPredicates.VALID_ENTITY);
                for(ItemEntity itemEntity : entities) {
                    addStack(itemEntity.getStack());
                    EntityUtil.kill(itemEntity);
                }

                WorldUtil.setBlockState(getWorld(), offsetBlockPos, getReplaceFluidWithBlock().getDefaultState());
                time += 0.1;
            }
        }

        return time;
    }

    public boolean tryPlaceFrame(BlockPos blockPos) {
        if (getEnergy() > getPlaceFrameEnergyCost()) {
            Block block = WorldUtil.getBlockState(getWorld(), blockPos).getBlock();
            if (block instanceof Frame) return false;
            if (
                    ((blockPos.getX() == minPos.getX()
                    || blockPos.getZ() == minPos.getZ()
                    || blockPos.getX() + 1 == maxPos.getX()
                    || blockPos.getZ() + 1 == maxPos.getZ())
                            && (blockPos.getY() == minPos.getY()
                            || blockPos.getY() == maxPos.getY())
                    )
                    ||
                    (
                            (blockPos.getX() == minPos.getX() && blockPos.getZ() == minPos.getZ())
                                    || (blockPos.getX() + 1 == maxPos.getX() && blockPos.getZ() + 1 == maxPos.getZ())
                                    || (blockPos.getX() == minPos.getX() && blockPos.getZ() + 1 == maxPos.getZ())
                                    || (blockPos.getX() + 1 == maxPos.getX() && blockPos.getZ() == minPos.getZ())
                    )
            ) {
                WorldUtil.setBlockState(getWorld(), blockPos, Frame.getPlacementStateByTile(getWorld(), blockPos));
                return true;
            }

        }
        return false;
    }

    public boolean tryQuarrying() {
        if (getWorld() == null || WorldUtil.isClient(getWorld())) return false;
        if (minPos == null)
            minPos = getDefaultRangeMinPos();
        if (maxPos == null)
            maxPos = getDefaultRangeMaxPos();
        int procX;
        int procY;
        int procZ;
        for (procY = maxPos.getY(); procY > WorldUtil.getBottomY(getWorld()); procY--) {
            if (minPos.getY() - 1 >= procY) {
                for (procX = minPos.getX() + 1; procX < maxPos.getX() - 1; procX++) {
                    for (procZ = minPos.getZ() + 1; procZ < maxPos.getZ() - 1; procZ++) {
                        BlockPos procPos = PosUtil.flooredBlockPos(procX, procY, procZ);
                        if (WorldUtil.getBlockState(getWorld(), procPos) == null) continue;
                        if (WorldUtil.getBlockEntity(getWorld(), procPos) instanceof QuarryTile && getWorld().getBlockEntity(procPos) == this) continue;

                        Block procBlock = WorldUtil.getBlockState(getWorld(), procPos).getBlock();
                        if (procBlock instanceof AirBlock || (procBlock.equals(Blocks.BEDROCK) && !canBedrockBreak)) {
                            if (canReplaceFluid()) {
                                double time = tryFluidReplace(procPos);
                                if (time != 0) {
                                    useEnergy((long) time * getReplaceFluidEnergyCost());
                                }
                            }
                            continue;
                        }
                        if (minPos.getY() - 1 >= procY) {
                            if ( procBlock instanceof FluidBlock) {
                                if (canReplaceFluid()
                                        && WorldUtil.getFluidState(getWorld(), procPos).isStill()
                                        && getEnergy() > getReplaceFluidEnergyCost()) {
                                    WorldUtil.setBlockState(getWorld(), procPos, BlockStateUtil.getDefaultState(getReplaceFluidWithBlock()));
                                    useEnergy(getReplaceFluidEnergyCost());
                                } else {
                                    continue;
                                }
                            }
                            if (canReplaceFluid()) {
                                double time = tryFluidReplace(procPos);
                                if (time != 0) {
                                    useEnergy((long) time * getReplaceFluidEnergyCost());
                                }
                            }
                            if (isSetMobDelete) {
                                tryDeleteMob(procPos);
                            }
                            if (isSetMobKill) {
                                tryKillMob(procPos);
                            }
                            if (isSetExpCollect) {
                                tryCollectExp(procPos);
                            }
                            breakBlock(procPos, true);
                            List<ItemEntity> entities = getWorld().getEntitiesByType(EntityType.ITEM, BoxUtil.createBox(PosUtil.flooredBlockPos(procX - 1, procY - 1, procZ - 1), PosUtil.flooredBlockPos(procX + 1, procY + 1, procZ + 1)), EntityPredicates.VALID_ENTITY);
                            if (entities.isEmpty()) return true;
                            for(ItemEntity itemEntity : entities) {
                                addStack(itemEntity.getStack());
                                EntityUtil.kill(itemEntity);
                            }
                            return true;
                        }
                    }
                }
            }
            else if (minPos.getY() <= procY && maxPos.getY() >= procY) {
                // procX < pos2.getX()を=<するとposのずれ問題は修正可能だが、別の方法で対処しているので、時間があればこっちで修正したい。
                for (procX = minPos.getX(); procX < maxPos.getX(); procX++) {
                    for (procZ = minPos.getZ(); procZ < maxPos.getZ(); procZ++) {
                        BlockPos procPos = PosUtil.flooredBlockPos(procX, procY, procZ);
                        if (WorldUtil.getBlockState(getWorld(), procPos) == null) continue;
                        if (WorldUtil.getBlockEntity(getWorld(), procPos) instanceof QuarryTile && WorldUtil.getBlockEntity(getWorld(), procPos) == this) continue;

                        Block procBlock = WorldUtil.getBlockState(getWorld(), procPos).getBlock();
                        if (procBlock instanceof AirBlock || (procBlock.equals(Blocks.BEDROCK) && !canBedrockBreak)) {
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
                            continue;
                        }
                        if (canReplaceFluid()) {
                            double time = tryFluidReplace(procPos);
                            if (time != 0) {
                                useEnergy((long) time * getReplaceFluidEnergyCost());
                            }
                        }
                        if (isSetMobDelete) {
                            tryDeleteMob(procPos);
                        }
                        if (isSetMobKill) {
                            tryKillMob(procPos);
                        }
                        if (isSetExpCollect) {
                            tryCollectExp(procPos);
                        }
                        if (procBlock instanceof FluidBlock
                                && !FluidUtil.isStill(WorldUtil.getFluidState(getWorld(), procPos))) {
                            continue;
                        }
                        if (procBlock instanceof Frame) continue;
                        breakBlock(procPos, false);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Integer getEmptyOrCanInsertIndex(Inventory inventory, ItemStack stack) {
        int index = 0;
        for (; index < getItems().size(); index++) {
            if (inventory.getStack(index).isEmpty()) {
                return index;
            }
            ItemStack inStack = getItems().get(index);
            if (stack.getItem().equals(inStack.getItem()) && (ItemStackUtil.areNbtOrComponentEqual(stack, inStack) || !ItemStackUtil.hasNbtOrComponent(stack) == !ItemStackUtil.hasNbtOrComponent(inStack)) && inStack.getItem().getMaxCount() != 1) {
                int originInCount = getItems().get(index).getCount();
                getItems().get(index).setCount(Math.min(stack.getMaxCount(), stack.getCount() + originInCount));
                if (stack.getMaxCount() >= stack.getCount() + originInCount) {
                    return index;
                }
            }
        }

        return null;
    }

    public void addStack(ItemStack stack) {
        if (getWorld() == null || getWorld().isClient()) return;
        int index = 0;
        for (; index < getItems().size(); index++) {
            if (stack.isEmpty() || stack.getCount() == 0) return;
            if (getItems().get(index).isEmpty()) {
                getItems().set(index, stack);
                return;
            }
            ItemStack inStack = getItems().get(index);
            if (stack.getItem().equals(inStack.getItem()) && (ItemStackUtil.areNbtOrComponentEqual(stack, inStack) || !ItemStackUtil.hasNbtOrComponent(stack) == !ItemStackUtil.hasNbtOrComponent(inStack)) && inStack.getItem().getMaxCount() != 1) {
                int originInCount = getItems().get(index).getCount();
                getItems().get(index).setCount(Math.min(stack.getMaxCount(), stack.getCount() + originInCount));
                if (stack.getMaxCount() >= stack.getCount() + originInCount) {
                    return;
                }
                stack.setCount(stack.getCount() + originInCount - stack.getMaxCount());
            }
        }
        getWorld().spawnEntity(new ItemEntity(getWorld(), getPos().getX(), getPos().getY(), getPos().getZ(), stack));
    }

    public QuarryTile(BlockEntityType<?> type, TileCreateEvent event) {
        super(type, event);
    }

    public void init() {

    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return invItems;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        int[] result = new int[getItems().size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }
        return result;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }
}
