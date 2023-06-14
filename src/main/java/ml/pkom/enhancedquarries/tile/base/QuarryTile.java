package ml.pkom.enhancedquarries.tile.base;

import ml.pkom.enhancedquarries.block.Frame;
import ml.pkom.enhancedquarries.block.base.Quarry;
import ml.pkom.enhancedquarries.mixin.MachineBaseBlockEntityAccessor;
import ml.pkom.mcpitanlibarch.api.util.ItemStackUtil;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.SlotConfiguration;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.util.RebornInventory;

import java.util.List;

public class QuarryTile extends PowerAcceptorBlockEntity implements InventoryProvider {// implements IInventory {
    // Container
    public RebornInventory<QuarryTile> inventory = new RebornInventory<>(27, "QuarryTile", 64, this);

    public RebornInventory<QuarryTile> getInventory() {
        return inventory;
    }
    // ----

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

    // TR
    // デフォルトコスト
    private long defaultEnergyCost = 30;
    private long defaultPlaceFrameEnergyCost = 40;
    private long defaultReplaceFluidEnergyCost = 120;

    // ブロック1回破壊分に対するエネルギーのコスト
    public long getEnergyCost() {
        return defaultEnergyCost;
    }

    // フレーム設置に必要なエネルギーのコスト
    public long getPlaceFrameEnergyCost() {
        return defaultPlaceFrameEnergyCost;
    }

    // 液体をガラスに置き換えるエネルギーのコスト
    public long getReplaceFluidEnergyCost() {
        return defaultReplaceFluidEnergyCost;
    }


    // エネルギーの容量
    public long getBaseMaxPower() {
        return 5000;
    }

    // エネルギーの最大出力(不要なので0)
    public long getBaseMaxOutput() {
        return 0;
    }

    // エネルギーの最大入力
    public long getBaseMaxInput() {
        return 500;
    }

    // エネルギーの生産をするか？→false
    public boolean canProvideEnergy(final Direction direction) { return false; }

    // ----

    // NBT

    public void writeNbt(NbtCompound tag) {
        tag.putDouble("coolTime", coolTime);
        if (canBedrockBreak)
            tag.putBoolean("module_bedrock_break", true);
        if (isSetMobKill)
            tag.putBoolean("module_mob_kill", true);
        if (isSetLuck)
            tag.putBoolean("module_luck", true);
        if (isSetSilkTouch)
            tag.putBoolean("module_silk_touch", true);
        if (isSetMobDelete)
            tag.putBoolean("module_mob_delete", true);

        if (pos1 != null) {
            tag.putInt("rangePos1X", getPos1().getX());
            tag.putInt("rangePos1Y", getPos1().getY());
            tag.putInt("rangePos1Z", getPos1().getZ());
        }
        if (pos2 != null) {
            tag.putInt("rangePos2X", getPos2().getX());
            tag.putInt("rangePos2Y", getPos2().getY());
            tag.putInt("rangePos2Z", getPos2().getZ());
        }
        super.writeNbt(tag);
    }

    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        if (tag.contains("coolTime")) coolTime = tag.getDouble("coolTime");
        if (tag.contains("module_bedrock_break")) canBedrockBreak = tag.getBoolean("module_bedrock_break");
        if (tag.contains("module_mob_delete")) isSetMobDelete = tag.getBoolean("module_mob_delete");
        if (tag.contains("module_mob_kill")) isSetMobKill = tag.getBoolean("module_mob_kill");
        if (tag.contains("module_luck")) isSetLuck = tag.getBoolean("module_luck");
        if (tag.contains("module_silk_touch")) isSetSilkTouch = tag.getBoolean("module_silk_touch");
        if (tag.contains("rangePos1X")
                && tag.contains("rangePos1Y")
                && tag.contains("rangePos1Z")
                && tag.contains("rangePos2X")
                && tag.contains("rangePos2Y")
                && tag.contains("rangePos2Z")) {
            setPos1(new BlockPos(tag.getInt("rangePos1X"), tag.getInt("rangePos1Y"), tag.getInt("rangePos1Z")));
            setPos2(new BlockPos(tag.getInt("rangePos2X"), tag.getInt("rangePos2Y"), tag.getInt("rangePos2Z")));
        }

    }

    // ----

    private double defaultBasicSpeed = 5;

    // 基準の速度
    public double getBasicSpeed() {
        return defaultBasicSpeed;
    }

    // TR用のTick
    public void TRTick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity2) {
        super.tick(world, pos, state, blockEntity2);
    }

    public double defaultSettingCoolTime = 300;

    // クールダウンの基準
    public double getSettingCoolTime() {
        return defaultSettingCoolTime;
    }

    public double coolTime = getSettingCoolTime();

    public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
        // 1.--
        super.tick(world, pos, state, blockEntity);
        if (getWorld() == null || getWorld().isClient())
        {
            return;
        }
        // ----
        //BlockState state = getWorld().getBlockState(getPos());
        Quarry quarry = (Quarry) state.getBlock();

        // レッドストーン受信で無効
        if (getWorld().isReceivingRedstonePower(getPos())) {
            if (isActive()) {
                quarry.setActive(false, getWorld(), getPos());
            }
            return;
        }

        if (getEnergy() > getEuPerTick(getEnergyCost())) {
            // ここに処理を記入
            if (coolTime <= 0) {
                coolTime = getSettingCoolTime();
                if (tryQuarrying()) {
                    useEnergy(getEnergyCost());
                    //insertChest();
                }
            }
            coolTimeBonus();
            coolTime = coolTime - getBasicSpeed();
            if (!isActive()) {
                quarry.setActive(true, getWorld(), getPos());
            }
        } else if (isActive()) {
            quarry.setActive(false, getWorld(), getPos());
        }


    }

    // この関数は失敗作なのでTRで処理することにした。
    public boolean insertChest() {
        // チェスト自動挿入
        if (getAnyContainerBlock() != null) {
            int i;
            for (i = 0; i < getInventory().size(); i++) {
                ItemStack stack = getInventory().getStack(i);
                if (!stack.isEmpty()) {
                    Inventory container = getAnyContainerBlock();
                    if (getEmptyOrCanInsertIndex(container, stack) != null) {
                        try {
                            int canIndex = getEmptyOrCanInsertIndex(container, stack);
                            ItemStack containerStack = container.getStack(canIndex);
                            if (containerStack.isEmpty()) {
                                container.setStack(canIndex, stack.copy());
                                getInventory().setStack(i, ItemStack.EMPTY);
                            } else {
                                int originInCount = containerStack.getCount();
                                container.getStack(i).setCount(Math.min(stack.getMaxCount(), stack.getCount() + originInCount));
                                if (stack.getMaxCount() >= stack.getCount() + originInCount) {
                                    getInventory().setStack(i, ItemStack.EMPTY);
                                    return true;
                                }
                                stack.setCount(stack.getCount() + originInCount - stack.getMaxCount());
                                getInventory().setStack(i, stack);
                            }
                            return true;
                        } catch (NullPointerException e) {

                        }
                    }

                }

            }
        }
        return false;
    }

    public Inventory getAnyContainerBlock() {
        return getAnyContainerBlock(0);
    }

    public Inventory getAnyContainerBlock(int i) {
        if (getWorld().getBlockEntity(getPos().up()) instanceof Inventory) {
            Inventory inventory = (Inventory) getWorld().getBlockEntity(getPos().up());
            return inventory;
        }
        if (getWorld().getBlockEntity(getPos().down()) instanceof Inventory) {
            Inventory inventory = (Inventory) getWorld().getBlockEntity(getPos().down());
            return inventory;
        }
        if (getWorld().getBlockEntity(getPos().north()) instanceof Inventory) {
            Inventory inventory = (Inventory) getWorld().getBlockEntity(getPos().north());
            return inventory;
        }
        if (getWorld().getBlockEntity(getPos().south()) instanceof Inventory) {
            Inventory inventory = (Inventory) getWorld().getBlockEntity(getPos().south());
            return inventory;
        }
        if (getWorld().getBlockEntity(getPos().west()) instanceof Inventory) {
            Inventory inventory = (Inventory) getWorld().getBlockEntity(getPos().west());
            return inventory;
        }
        if (getWorld().getBlockEntity(getPos().east()) instanceof Inventory) {
            Inventory inventory = (Inventory) getWorld().getBlockEntity(getPos().east());
            return inventory;
        }
        return null;
    }

    // クールダウンのエネルギー量による追加ボーナス
    public void coolTimeBonus() {
        if (getBaseMaxPower() / 1.125 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed() * 5;
        }
        if (getBaseMaxPower() / 1.25 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed() * 3;
        }
        if (getBaseMaxPower() / 3 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed() * 2;
        }
        if (getBaseMaxPower() / 5 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getBaseMaxPower() / 7 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getBaseMaxPower() / 10 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getBaseMaxPower() / 12 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getBaseMaxPower() / 15 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getBaseMaxPower() / 16 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getBaseMaxPower() / 20 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getBaseMaxPower() / 30 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getBaseMaxPower() / 40 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
    }

    public BlockPos getRangePos1() {
        BlockPos blockPos = null;
        // default
        if (getFacing().equals(Direction.NORTH)) {
            blockPos = new BlockPos(getPos().getX() - 5, getPos().getY(), getPos().getZ() + 11);
        }
        if (getFacing().equals(Direction.SOUTH)) {
            blockPos = new BlockPos(getPos().getX() - 5, getPos().getY(), getPos().getZ() - 1);
        }
        if (getFacing().equals(Direction.WEST)) {
            blockPos = new BlockPos(getPos().getX() + 1, getPos().getY(), getPos().getZ() + 5);
        }
        if (getFacing().equals(Direction.EAST)) {
            blockPos = new BlockPos(getPos().getX() - 11, getPos().getY(), getPos().getZ() + 5);
        }
        return blockPos;
    }

    public BlockPos getRangePos2() {
        BlockPos blockPos = null;
        // default
        if (getFacing().equals(Direction.NORTH)) {
            blockPos = new BlockPos(getPos().getX() + 6, getPos().getY() + 4, getPos().getZ());
        }
        if (getFacing().equals(Direction.SOUTH)) {
            blockPos = new BlockPos(getPos().getX() + 6, getPos().getY() + 4, getPos().getZ() - 12);
        }
        if (getFacing().equals(Direction.WEST)) {
            blockPos = new BlockPos(getPos().getX() + 12, getPos().getY() + 4, getPos().getZ() - 6);
        }
        if (getFacing().equals(Direction.EAST)) {
            blockPos = new BlockPos(getPos().getX(), getPos().getY() + 4, getPos().getZ() - 6);
        }
        return blockPos;
    }

    // マーカーによる範囲指定を許可するか？
    public boolean canSetPosByMarker() {
        return true;
    }

    private BlockPos pos1 = null;
    private BlockPos pos2 = null;

    public BlockPos getPos1() {
        return pos1;
    }

    public BlockPos getPos2() {
        return pos2;
    }

    public void setPos1(BlockPos pos1) {
        this.pos1 = pos1;
    }

    public void setPos2(BlockPos pos2) {
        this.pos2 = pos2;
    }

    public void breakBlock(BlockPos pos, boolean drop) {
        if (getWorld().isClient()) return;
        if (isSetSilkTouch || isSetLuck) {
            if (drop) {
                List<ItemStack> drops = Block.getDroppedStacks(getWorld().getBlockState(pos), (ServerWorld) getWorld(), pos, getWorld().getBlockEntity(pos), null, getQuarryStack());
                drops.forEach((stack) -> {
                    addStack(stack);
                });
            }
            getWorld().breakBlock(pos, false);
        } else {
            getWorld().breakBlock(pos, drop);
        }
    }

    private ItemStack getQuarryStack() {
        ItemStack stack = new ItemStack(Items.DIAMOND_PICKAXE);
        if (isSetSilkTouch)
            stack.addEnchantment(Enchantments.SILK_TOUCH, 3);
        if (isSetLuck)
            stack.addEnchantment(Enchantments.FORTUNE, 3);
        return stack;
    }

    public void tryDeleteMob(BlockPos blockPos) {
        if (getWorld().isClient()) return;
        List<MobEntity> mobs = getWorld().getEntitiesByClass(MobEntity.class, new Box(blockPos.add(-1, -1, -1), blockPos.add(1, 1, 1)), EntityPredicates.VALID_ENTITY);
        mobs.forEach((mob) -> {
            mob.discard();
        });
    }

    public void tryKillMob(BlockPos blockPos) {
        if (getWorld().isClient()) return;
        List<MobEntity> mobs = getWorld().getEntitiesByClass(MobEntity.class, new Box(blockPos.add(-1, -1, -1), blockPos.add(1, 1, 1)), EntityPredicates.VALID_ENTITY);
        mobs.forEach((mob) -> {
            mob.kill();
        });
    }

    public double tryFluidReplace(BlockPos blockPos) {
        double time = 0;
        BlockPos tmpPos = null;
        int i;
        for (i = 0;i < 4;i++) {
            if (i == 0)
                tmpPos = blockPos.add(1 , 0, 0);
            if (i == 1)
                tmpPos = blockPos.add(-1 , 0, 0);
            if (i == 2)
                tmpPos = blockPos.add(0 , 0, 1);
            if (i == 3)
                tmpPos = blockPos.add(0 , 0, -1);
            if (getWorld().getBlockState(tmpPos).getBlock() instanceof FluidBlock
                    && getEnergy() > getEuPerTick(getReplaceFluidEnergyCost())) {
                if (getWorld().getFluidState(tmpPos).isStill()) {
                    getWorld().setBlockState(tmpPos, getReplaceFluidWithBlock().getDefaultState());
                    time++;
                    continue;
                }
            }
            if (!getWorld().getFluidState(tmpPos).isEmpty() || getWorld().getBlockState(tmpPos).getBlock() instanceof FluidBlock) {
                if (i == 0 && getEnergy() > getEuPerTick(getReplaceFluidEnergyCost())) {
                    if (tmpPos.getX() + 1 == pos2.getX()) {
                        BlockState tmpBlock = getWorld().getBlockState(tmpPos);
                        if (!tmpBlock.isAir() && !(tmpBlock.getBlock() instanceof FluidBlock)) {
                            breakBlock(tmpPos, true);
                            List<ItemEntity> entities = getWorld().getEntitiesByType(EntityType.ITEM, new Box(new BlockPos(tmpPos.getX() - 1, tmpPos.getY() - 1, tmpPos.getZ() - 1), new BlockPos(tmpPos.getX() + 1, tmpPos.getY() + 1, tmpPos.getZ() + 1)), EntityPredicates.VALID_ENTITY);
                            if (!entities.isEmpty())
                                for(ItemEntity itemEntity : entities) {
                                    addStack(itemEntity.getStack());
                                    itemEntity.kill();
                                }
                        }
                        getWorld().setBlockState(tmpPos, getReplaceFluidWithBlock().getDefaultState());
                        time++;
                    }
                }
                if (i == 1 && getEnergy() > getEuPerTick(getReplaceFluidEnergyCost())) {
                    if (tmpPos.getX() == pos1.getX()) {
                        BlockState tmpBlock = getWorld().getBlockState(tmpPos);
                        if (!tmpBlock.isAir() && !(tmpBlock.getBlock() instanceof FluidBlock)) {
                            breakBlock(tmpPos, true);
                            List<ItemEntity> entities = getWorld().getEntitiesByType(EntityType.ITEM, new Box(new BlockPos(tmpPos.getX() - 1, tmpPos.getY() - 1, tmpPos.getZ() - 1), new BlockPos(tmpPos.getX() + 1, tmpPos.getY() + 1, tmpPos.getZ() + 1)), EntityPredicates.VALID_ENTITY);
                            if (!entities.isEmpty())
                                for(ItemEntity itemEntity : entities) {
                                    addStack(itemEntity.getStack());
                                    itemEntity.kill();
                                }
                        }
                        getWorld().setBlockState(tmpPos, getReplaceFluidWithBlock().getDefaultState());
                        time++;
                    }
                }
                if (i == 2 && getEnergy() > getEuPerTick(getReplaceFluidEnergyCost())) {
                    if (tmpPos.getZ() == pos1.getZ()) {
                        BlockState tmpBlock = getWorld().getBlockState(tmpPos);
                        if (!tmpBlock.isAir() && !(tmpBlock.getBlock() instanceof FluidBlock)) {
                            breakBlock(tmpPos, true);
                            List<ItemEntity> entities = getWorld().getEntitiesByType(EntityType.ITEM, new Box(new BlockPos(tmpPos.getX() - 1, tmpPos.getY() - 1, tmpPos.getZ() - 1), new BlockPos(tmpPos.getX() + 1, tmpPos.getY() + 1, tmpPos.getZ() + 1)), EntityPredicates.VALID_ENTITY);
                            if (!entities.isEmpty())
                                for(ItemEntity itemEntity : entities) {
                                    addStack(itemEntity.getStack());
                                    itemEntity.kill();
                                }
                        }
                        getWorld().setBlockState(tmpPos, getReplaceFluidWithBlock().getDefaultState());
                        time++;
                    }
                }
                if (i == 3 && getEnergy() > getEuPerTick(getReplaceFluidEnergyCost())) {
                    if (tmpPos.getZ() - 1 == pos2.getZ()) {
                        BlockState tmpBlock = getWorld().getBlockState(tmpPos);
                        if (!tmpBlock.isAir() && !(tmpBlock.getBlock() instanceof FluidBlock)) {
                            breakBlock(tmpPos, true);
                            List<ItemEntity> entities = getWorld().getEntitiesByType(EntityType.ITEM, new Box(new BlockPos(tmpPos.getX() - 1, tmpPos.getY() - 1, tmpPos.getZ() - 1), new BlockPos(tmpPos.getX() + 1, tmpPos.getY() + 1, tmpPos.getZ() + 1)), EntityPredicates.VALID_ENTITY);
                            if (!entities.isEmpty())
                                for(ItemEntity itemEntity : entities) {
                                    addStack(itemEntity.getStack());
                                    itemEntity.kill();
                                }
                        }
                        getWorld().setBlockState(tmpPos, getReplaceFluidWithBlock().getDefaultState());
                        time++;
                    }
                }
            }
        }

        return time;
    }

    public boolean tryPlaceFrame(BlockPos blockPos) {
        if (getEnergy() > getEuPerTick(getPlaceFrameEnergyCost())) {
            Block block = getWorld().getBlockState(blockPos).getBlock();
            if (block instanceof Frame) return false;
            if (
                    ((blockPos.getX() == pos1.getX()
                    || blockPos.getZ() == pos1.getZ()
                    || blockPos.getX() + 1 == pos2.getX()
                    || blockPos.getZ() - 1 == pos2.getZ())
                            && (blockPos.getY() == pos1.getY()
                            || blockPos.getY() == pos2.getY())
                    )
                    ||
                    (
                            (blockPos.getX() == pos1.getX() && blockPos.getZ() == pos1.getZ())
                                    || (blockPos.getX() + 1 == pos2.getX() && blockPos.getZ() - 1 == pos2.getZ())
                                    || (blockPos.getX() == pos1.getX() && blockPos.getZ() - 1 == pos2.getZ())
                                    || (blockPos.getX() + 1 == pos2.getX() && blockPos.getZ() == pos1.getZ())
                    )
            ) {
                getWorld().setBlockState(blockPos, Frame.getPlacementStateByTile(world, blockPos));
                return true;
            }

        }
        return false;
    }

    public boolean tryQuarrying() {
        if (getWorld() == null || getWorld().isClient()) return false;
        if (pos1 == null)
            pos1 = getRangePos1();
        if (pos2 == null)
            pos2 = getRangePos2();
        int procX;
        int procY;
        int procZ;
        for (procY = pos2.getY(); procY > getWorld().getBottomY(); procY--) {
            if (pos1.getY() - 1 >= procY) {
                for (procX = pos1.getX() + 1; procX < pos2.getX() - 1; procX++) {
                    for (procZ = pos1.getZ() - 1; procZ > pos2.getZ() + 1; procZ--) {
                        BlockPos procPos = new BlockPos(procX, procY, procZ);
                        if (getWorld().getBlockState(procPos) == null) continue;
                        Block procBlock = getWorld().getBlockState(procPos).getBlock();
                        if (procBlock instanceof AirBlock || (procBlock.equals(Blocks.BEDROCK) && !canBedrockBreak)) {
                            if (canReplaceFluid()) {
                                double time = tryFluidReplace(procPos);
                                if (time != 0) {
                                    useEnergy((long) time * getReplaceFluidEnergyCost());
                                }
                            }
                            continue;
                        }
                        if (pos1.getY() - 1 >= procY) {
                            if ( procBlock instanceof FluidBlock) {
                                if (canReplaceFluid()
                                        && getWorld().getFluidState(procPos).isStill()
                                        && getEnergy() > getEuPerTick(getReplaceFluidEnergyCost())) {
                                    getWorld().setBlockState(procPos, getReplaceFluidWithBlock().getDefaultState());
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
                            breakBlock(procPos, true);
                            List<ItemEntity> entities = getWorld().getEntitiesByType(EntityType.ITEM, new Box(new BlockPos(procX - 1, procY - 1, procZ - 1), new BlockPos(procX + 1, procY + 1, procZ + 1)), EntityPredicates.VALID_ENTITY);
                            if (entities.isEmpty()) return true;
                            for(ItemEntity itemEntity : entities) {
                                addStack(itemEntity.getStack());
                                itemEntity.kill();
                            }
                            return true;
                        }
                    }
                }
            }
            else if (pos1.getY() <= procY && pos2.getY() >= procY) {
                // procX < pos2.getX()を=<するとposのずれ問題は修正可能だが、別の方法で対処しているので、時間があればこっちで修正したい。
                for (procX = pos1.getX(); procX < pos2.getX(); procX++) {
                    for (procZ = pos1.getZ(); procZ > pos2.getZ(); procZ--) {
                        BlockPos procPos = new BlockPos(procX, procY, procZ);
                        if (getWorld().getBlockState(procPos) == null) continue;
                        Block procBlock = getWorld().getBlockState(procPos).getBlock();
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
                        if (procBlock instanceof FluidBlock && !getWorld().getFluidState(procPos).isStill()) {
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
        for (; index < getInventory().size(); index++) {
            if (inventory.getStack(index).isEmpty()) {
                return index;
            }
            ItemStack inStack = getInventory().getStack(index);
            if (stack.getItem().equals(inStack.getItem()) && (ItemStackUtil.areNbtEqual(stack, inStack) || !stack.hasNbt() == !inStack.hasNbt()) && inStack.getItem().getMaxCount() != 1) {
                int originInCount = getInventory().getStack(index).getCount();
                getInventory().getStack(index).setCount(Math.min(stack.getMaxCount(), stack.getCount() + originInCount));
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
        for (;index < getInventory().size();index++) {
            if (stack.isEmpty() || stack.getCount() == 0) return;
            if (getInventory().getStack(index).isEmpty()) {
                getInventory().setStack(index, stack);
                return;
            }
            ItemStack inStack = getInventory().getStack(index);
            if (stack.getItem().equals(inStack.getItem()) && (ItemStackUtil.areNbtEqual(stack, inStack) || !stack.hasNbt() == !inStack.hasNbt()) && inStack.getItem().getMaxCount() != 1) {
                int originInCount = getInventory().getStack(index).getCount();
                getInventory().getStack(index).setCount(Math.min(stack.getMaxCount(), stack.getCount() + originInCount));
                if (stack.getMaxCount() >= stack.getCount() + originInCount) {
                    return;
                }
                stack.setCount(stack.getCount() + originInCount - stack.getMaxCount());
            }
        }
        getWorld().spawnEntity(new ItemEntity(getWorld(), getPos().getX(), getPos().getY(), getPos().getZ(), stack));
    }

    public QuarryTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void init() {
        int index = 0;
        SlotConfiguration.SlotIO slotIO = new SlotConfiguration.SlotIO(SlotConfiguration.ExtractConfig.OUTPUT);
        SlotConfiguration slotConfig = new SlotConfiguration(getInventory());
        for (;index < getInventory().size();index++){
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.NORTH, slotIO, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.SOUTH, slotIO, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.EAST, slotIO, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.WEST, slotIO, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.UP, slotIO, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.DOWN, slotIO, index));
            slotConfig.getSlotDetails(index).setOutput(true);
            markDirty();
        }
        ((MachineBaseBlockEntityAccessor) this).setSlotConfiguration(slotConfig);
        //FillerPlus.log(Level.INFO, "north output: " + slotConfig.getSlotDetails(0).getSideDetail(Direction.NORTH).getSlotIO().getIoConfig().name());
    }
}
