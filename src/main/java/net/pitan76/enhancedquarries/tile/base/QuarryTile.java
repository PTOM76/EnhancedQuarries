package net.pitan76.enhancedquarries.tile.base;

import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
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
import net.pitan76.enhancedquarries.item.base.MachineModule;
import net.pitan76.enhancedquarries.item.quarrymodule.DropRemovalModule;
import net.pitan76.enhancedquarries.item.quarrymodule.ModuleItems;
import net.pitan76.mcpitanlib.api.enchantment.CompatEnchantment;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.event.nbt.ReadNbtArgs;
import net.pitan76.mcpitanlib.api.event.nbt.WriteNbtArgs;
import net.pitan76.mcpitanlib.api.event.tile.TileTickEvent;
import net.pitan76.mcpitanlib.api.gui.inventory.IInventory;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.mcpitanlib.api.util.entity.ItemEntityUtil;
import net.pitan76.mcpitanlib.api.util.math.BoxUtil;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public int getMaxModuleCount() {
        return 64;
    }

    // モジュール
    public DefaultedList<ItemStack> moduleStacks = DefaultedList.ofSize(getMaxModuleCount(), ItemStackUtil.empty());

    public void addModuleStack(ItemStack stack) {
        if (!(stack.getItem() instanceof MachineModule))
            return;

        int nextIndex = 0;
        for (nextIndex = 0; nextIndex < moduleStacks.size(); nextIndex++) {
            if (moduleStacks.get(nextIndex).isEmpty())
                break;
        }

        moduleStacks.add(nextIndex, stack);
        CACHE_isEnchanted = null;
        CACHE_moduleItems.add(stack.getItem());
    }

    public void insertModuleStack(ItemStack stack) {
        ItemStack copyStack = ItemStackUtil.copy(stack);
        ItemStackUtil.setCount(copyStack, 1);
        addModuleStack(copyStack);

        ItemStackUtil.decrementCount(stack, 1);
    }

    public boolean removeModuleStack(ItemStack stack) {
        CACHE_isEnchanted = null;
        CACHE_moduleItems.remove(stack.getItem());

        return moduleStacks.remove(stack);
    }

    public boolean hasModuleStack(ItemStack stack) {
        return moduleStacks.contains(stack);
    }

    private final List<Item> CACHE_moduleItems = new ArrayList<>();

    public boolean hasModuleItem(Item item) {
        if (CACHE_moduleItems.contains(item)) return true;

        for (ItemStack stack : getModuleStacks()) {
            if (stack.getItem().equals(item)) {
                CACHE_moduleItems.add(item);
                return true;
            }
        }

        return false;
    }

    public DefaultedList<ItemStack> getModuleStacks() {
        return moduleStacks;
    }

    public boolean isEmptyInModules() {
        return getModuleStacks().isEmpty();
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
        super.writeNbt(args);
        NbtCompound nbt = args.getNbt();
        InventoryUtil.writeNbt(args, getItems());

        NbtUtil.putDouble(nbt, "coolTime", coolTime);

        if (!isEmptyInModules()) {
            NbtCompound moduleNbt = NbtUtil.create();
            InventoryUtil.writeNbt(args.registryLookup, moduleNbt, getModuleStacks());
            NbtUtil.put(nbt, "modules", moduleNbt);
        }

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
        super.readNbt(args);
        NbtCompound nbt = args.getNbt();
        if (NbtUtil.has(nbt, "Items"))
            InventoryUtil.readNbt(args, getItems());

        if (NbtUtil.has(nbt, "coolTime"))
            coolTime = NbtUtil.getDouble(nbt, "coolTime");

        if (NbtUtil.has(nbt, "modules")) {
            NbtCompound moduleNbt = NbtUtil.get(nbt, "modules");
            InventoryUtil.readNbt(args.registryLookup, moduleNbt, getModuleStacks());
        }

        addModulesFromOldNbt(nbt);

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

    protected void addModulesFromOldNbt(NbtCompound nbt) {
        if (NbtUtil.has(nbt, "module_bedrock_break") && NbtUtil.getBoolean(nbt, "module_bedrock_break")) {
            if (!hasModuleItem(net.pitan76.enhancedquarries.Items.BEDROCK_BREAK_MODULE))
                addModuleStack(ItemStackUtil.create(net.pitan76.enhancedquarries.Items.BEDROCK_BREAK_MODULE, 1));
        }
        if (NbtUtil.has(nbt, "module_mob_delete") && NbtUtil.getBoolean(nbt, "module_mob_delete")) {
            if (!hasModuleItem(net.pitan76.enhancedquarries.Items.MOB_DELETE_MODULE))
                addModuleStack(ItemStackUtil.create(net.pitan76.enhancedquarries.Items.MOB_DELETE_MODULE, 1));
        }
        if (NbtUtil.has(nbt, "module_mob_kill") && NbtUtil.getBoolean(nbt, "module_mob_kill")) {
            if (!hasModuleItem(net.pitan76.enhancedquarries.Items.MOB_KILL_MODULE))
                addModuleStack(ItemStackUtil.create(net.pitan76.enhancedquarries.Items.MOB_KILL_MODULE, 1));
        }
        if (NbtUtil.has(nbt, "module_luck") && NbtUtil.getBoolean(nbt, "module_luck")) {
            if (!hasModuleItem(net.pitan76.enhancedquarries.Items.LUCK_MODULE))
                addModuleStack(ItemStackUtil.create(net.pitan76.enhancedquarries.Items.LUCK_MODULE, 1));
        }
        if (NbtUtil.has(nbt, "module_silk_touch") && NbtUtil.getBoolean(nbt, "module_silk_touch")) {
            if (!hasModuleItem(net.pitan76.enhancedquarries.Items.SILK_TOUCH_MODULE))
                addModuleStack(ItemStackUtil.create(net.pitan76.enhancedquarries.Items.SILK_TOUCH_MODULE, 1));
        }
        if (NbtUtil.has(nbt, "module_exp_collect") && NbtUtil.getBoolean(nbt, "module_exp_collect")) {
            if (!hasModuleItem(net.pitan76.enhancedquarries.Items.EXP_COLLECT_MODULE))
                addModuleStack(ItemStackUtil.create(net.pitan76.enhancedquarries.Items.EXP_COLLECT_MODULE, 1));
        }
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

    public boolean isRemovalItem(ItemStack stack) {
        if (!hasModuleItem(ModuleItems.DROP_REMOVAL_MODULE)) return false;

        for (ItemStack moduleStack : getModuleStacks()) {
            if (!(moduleStack.getItem() instanceof DropRemovalModule)) continue;

            List<Item> removalItems = DropRemovalModule.getRemovalItems(moduleStack);
            if (removalItems.contains(stack.getItem()))
                return true;

        }
        return false;
    }

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

    public void setMinPos(BlockPos pos) {
        this.minPos = pos;
    }

    public void setMaxPos(BlockPos pos) {
        this.maxPos = pos;
    }

    public void breakBlock(BlockPos pos, boolean drop) {
        if (WorldUtil.isClient(getWorld())) return;

        if (isEnchanted()) {
            if (drop) {
                List<ItemStack> drops = Block.getDroppedStacks(WorldUtil.getBlockState(getWorld(), pos), (ServerWorld) getWorld(), pos, getWorld().getBlockEntity(pos), null, getQuarryStack());
                drops.forEach(this::addStack);
            }
            WorldUtil.breakBlock(getWorld(), pos, false);
        } else {
            WorldUtil.breakBlock(getWorld(), pos, drop);
        }
    }

    private Boolean CACHE_isEnchanted = null;

    public boolean isEnchanted() {
        if (CACHE_isEnchanted != null) return CACHE_isEnchanted;

        for (ItemStack stack : getModuleStacks()) {
            if (!(stack.getItem() instanceof MachineModule)) continue;

            MachineModule module = (MachineModule) stack.getItem();
            if (module.getEnchantments() != null) {
                CACHE_isEnchanted = true;
                return true;
            }
        }

        CACHE_isEnchanted = false;
        return false;
    }

    private ItemStack getQuarryStack() {
        ItemStack stack = ItemStackUtil.create(Items.DIAMOND_PICKAXE);
        applyEnchantment(stack);
        return stack;
    }

    private void applyEnchantment(ItemStack stack) {
        boolean applied = false;

        Map<CompatEnchantment, Integer> enchantments = EnchantmentUtil.getEnchantment(stack, getWorld());

        if (isEmptyInModules()) return;
        for (ItemStack moduleStack : getModuleStacks()) {
            if (!(moduleStack.getItem() instanceof MachineModule)) continue;
            MachineModule module = (MachineModule) moduleStack.getItem();

            if (module.getEnchantments() == null) continue;
            enchantments.putAll(module.getEnchantments());
            applied = true;
        }

        if (applied)
            EnchantmentUtil.setEnchantment(stack, enchantments, getWorld());
    }

    public void tryDeleteMob(BlockPos blockPos) {
        if (getWorld() == null || getWorld().isClient()) return;
        List<MobEntity> mobs = getWorld().getEntitiesByClass(MobEntity.class, BoxUtil.createBox(blockPos.add(-2, -2, -2), blockPos.add(2, 2, 2)), EntityPredicates.VALID_ENTITY);
        mobs.forEach(EntityUtil::discard);
    }

    public void tryKillMob(BlockPos blockPos) {
        if (getWorld() == null || getWorld().isClient()) return;
        List<MobEntity> mobs = getWorld().getEntitiesByClass(MobEntity.class, BoxUtil.createBox(blockPos.add(-2, -2, -2), blockPos.add(2, 2, 2)), EntityPredicates.VALID_ENTITY);
        mobs.forEach(EntityUtil::kill);
    }

    public void tryCollectExp(BlockPos blockPos) {
        if (getWorld() == null || getWorld().isClient()) return;
        List<ExperienceOrbEntity> entities = getWorld().getEntitiesByClass(ExperienceOrbEntity.class, BoxUtil.createBox(blockPos.add(-2, -2, -2), blockPos.add(2, 2, 2)), EntityPredicates.VALID_ENTITY);
        entities.forEach((entity) -> {
            if (getStoredExp() + entity.getExperienceAmount() > getMaxStoredExp()) return;
            addStoredExp(entity.getExperienceAmount());
            EntityUtil.discard(entity);
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
                        if (procBlock instanceof AirBlock || (procBlock.equals(Blocks.BEDROCK) && !hasModuleItem(ModuleItems.BEDROCK_BREAK_MODULE))) {
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
                            continue;
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

        if (isRemovalItem(stack)) return;

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

        ItemEntity itemEntity = ItemEntityUtil.create(getWorld(), getPos().getX(), getPos().getY(), getPos().getZ(), stack);
        WorldUtil.spawnEntity(getWorld(), itemEntity);
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
