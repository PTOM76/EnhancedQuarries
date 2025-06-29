package net.pitan76.enhancedquarries.tile.base;

import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pitan76.enhancedquarries.block.Frame;
import net.pitan76.enhancedquarries.block.base.Quarry;
import net.pitan76.enhancedquarries.item.base.MachineModule;
import net.pitan76.enhancedquarries.item.quarrymodule.DropRemovalModule;
import net.pitan76.enhancedquarries.item.quarrymodule.ModuleItems;
import net.pitan76.enhancedquarries.util.UnbreakableBlocks;
import net.pitan76.mcpitanlib.api.block.CompatBlocks;
import net.pitan76.mcpitanlib.api.enchantment.CompatEnchantment;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.event.nbt.ReadNbtArgs;
import net.pitan76.mcpitanlib.api.event.nbt.WriteNbtArgs;
import net.pitan76.mcpitanlib.api.event.tile.TileTickEvent;
import net.pitan76.mcpitanlib.api.gui.inventory.IInventory;
import net.pitan76.mcpitanlib.api.gui.inventory.sided.ChestStyleSidedInventory;
import net.pitan76.mcpitanlib.api.gui.inventory.sided.args.AvailableSlotsArgs;
import net.pitan76.mcpitanlib.api.gui.inventory.sided.args.CanExtractArgs;
import net.pitan76.mcpitanlib.api.gui.inventory.sided.args.CanInsertArgs;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.mcpitanlib.api.util.collection.ClippedItemStackList;
import net.pitan76.mcpitanlib.api.util.collection.ItemStackList;
import net.pitan76.mcpitanlib.api.util.entity.ItemEntityUtil;
import net.pitan76.mcpitanlib.api.util.math.BoxUtil;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;
import net.pitan76.mcpitanlib.api.util.nbt.v2.NbtRWUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//@SuppressWarnings("UnstableApiUsage")
public class QuarryTile extends BaseEnergyTile implements IInventory, ChestStyleSidedInventory {

    public int getInvSize() {
        return 27;
    }

    public int getMaxModuleCount() {
        return 64;
    }

    // モジュール
    //public ItemStackList moduleStacks = ItemStackList.ofSize(getMaxModuleCount(), ItemStackUtil.empty());

    // Container
    public ItemStackList allStacks = ItemStackList.ofSize(getInvSize() + getMaxModuleCount(), ItemStackUtil.empty());

    public ClippedItemStackList invItems = ClippedItemStackList.of(getAllStacks(), 0, getInvSize());
    public ClippedItemStackList moduleStacks = ClippedItemStackList.of(getAllStacks(), getInvSize(), getInvSize() + getMaxModuleCount());

    public ItemStackList getAllStacks() {
        return allStacks;
    }

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

    public void addModuleStack(ItemStack stack) {
        Item item = ItemStackUtil.getItem(stack);
        if (!(item instanceof MachineModule))
            return;

        int nextIndex = 0;
        int size = moduleStacks.size();
        for (nextIndex = 0; nextIndex < size; nextIndex++) {
            if (moduleStacks.get(nextIndex).isEmpty())
                break;
        }

        moduleStacks.set(nextIndex, stack);
        CACHE_isEnchanted = null;
        CACHE_moduleItems.add(item);
    }

    public void insertModuleStack(ItemStack stack) {
        ItemStack copyStack = ItemStackUtil.copy(stack);
        ItemStackUtil.setCount(copyStack, 1);
        addModuleStack(copyStack);

        ItemStackUtil.decrementCount(stack, 1);
    }

    public boolean removeModuleStack(ItemStack stack) {
        CACHE_isEnchanted = null;
        CACHE_moduleItems.remove(ItemStackUtil.getItem(stack));

        return moduleStacks.remove(stack);
    }

    public boolean hasModuleStack(ItemStack stack) {
        return moduleStacks.contains(stack);
    }

    private final List<Item> CACHE_moduleItems = new ArrayList<>();

    public boolean hasModuleItem(Item item) {
        if (CACHE_moduleItems.contains(item)) return true;

        for (ItemStack stack : getModuleStacks()) {
            if (ItemStackUtil.getItem(stack).equals(item)) {
                CACHE_moduleItems.add(item);
                return true;
            }
        }

        return false;
    }

    public ItemStackList getModuleStacks() {
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
        return CompatBlocks.GLASS;
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
    boolean needRemoveModules = false;

    public void writeNbt(WriteNbtArgs args) {
        super.writeNbt(args);

        NbtRWUtil.putInv(args, getAllStacks());
        NbtRWUtil.putDouble(args, "coolTime", coolTime);

        if (!args.hasRegistryLookup() && callGetWorld() != null)
            args.registryLookup = RegistryLookupUtil.getRegistryLookup(callGetWorld());

        if (needRemoveModules && NbtUtil.has(args.getNbt(), "modules")) {
            args.getNbt().remove("modules");
            needRemoveModules = false;
        }

        //WriteNbtArgs moduleArgs = NbtRWUtil.putWithCreate(args, "modules");
        //NbtRWUtil.putInv(moduleArgs, getModuleStacks());

        if (minPos != null) {
            NbtRWUtil.putInt(args, "rangePos1X", PosUtil.x(minPos));
            NbtRWUtil.putInt(args, "rangePos1Y", PosUtil.y(minPos));
            NbtRWUtil.putInt(args, "rangePos1Z", PosUtil.z(minPos));
        }
        if (maxPos != null) {
            NbtRWUtil.putInt(args, "rangePos2X", PosUtil.x(maxPos));
            NbtRWUtil.putInt(args, "rangePos2Y", PosUtil.y(maxPos));
            NbtRWUtil.putInt(args, "rangePos2Z", PosUtil.z(maxPos));
        }

        NbtRWUtil.putInt(args, "storedExp", getStoredExp());

        /*
        NbtUtil.put(nbt, "variant", fluidStorage.variant.toNbt());
        tag.putLong("amount", fluidStorage.amount);
         */
    }

    public void readNbt(ReadNbtArgs args) {
        super.readNbt(args);

        NbtRWUtil.getInv(args, getAllStacks());
        coolTime = NbtRWUtil.getDoubleOrDefault(args, "coolTime", getSettingCoolTime());

        if (!args.hasRegistryLookup() && callGetWorld() != null)
            args.registryLookup = RegistryLookupUtil.getRegistryLookup(callGetWorld());

        //ReadNbtArgs moduleArgs = NbtRWUtil.get(args, "modules");
        if (!getModuleStacks().isEmpty()) {
            //NbtRWUtil.getInv(moduleArgs, getModuleStacks());
            if (!isEmptyInModules()) {
                CACHE_isEnchanted = null;
                CACHE_moduleItems.clear();
                for (ItemStack stack : getModuleStacks()) {
                    CACHE_moduleItems.add(ItemStackUtil.getItem(stack));
                }
            }
        }

        addModulesFromOldNbt(args.nbt);

        int pos1x = NbtRWUtil.getIntOrDefault(args, "rangePos1X", 0);
        int pos1y = NbtRWUtil.getIntOrDefault(args, "rangePos1Y", 0);
        int pos1z = NbtRWUtil.getIntOrDefault(args, "rangePos1Z", 0);

        int pos2x = NbtRWUtil.getIntOrDefault(args, "rangePos2X", 0);
        int pos2y = NbtRWUtil.getIntOrDefault(args, "rangePos2Y", 0);
        int pos2z = NbtRWUtil.getIntOrDefault(args, "rangePos2Z", 0);

        setMinPos(PosUtil.flooredBlockPos(pos1x, pos1y, pos1z));
        setMaxPos(PosUtil.flooredBlockPos(pos2x, pos2y, pos2z));

        setStoredExp(NbtRWUtil.getIntOrDefault(args, "storedExp", 0));

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
        // TODO: キャッシュ機構実装
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

        int size = getItems().size();
        for (int i = 0; i < size; i++) {
            if (time > limit) break;
            for (Direction dir : dirs) {
                ItemStack stack = getItems().get(i);
                if (stack.isEmpty()) continue;

                long amount = StorageUtil.move(InventoryStorage.of(this, null).getSlot(i), ItemStorage.SIDED.find(callGetWorld(), callGetPos().offset(dir), dir.getOpposite()), (iv) -> true, Long.MAX_VALUE, null);
                if (amount < stack.getCount()) continue;

                ++time;
                break;
            }
        }
    }

    public List<Direction> getDirsOfAnyContainerBlock() {
        if (callGetWorld() == null) return new ArrayList<>();

        Direction[] dirs = new Direction[]{Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
        List<Direction> usableDirs = new ArrayList<>();

        for (Direction dir : dirs) {
            BlockPos pos = callGetPos().offset(dir);
            if (WorldUtil.getBlockEntity(callGetWorld(), pos) == null) continue;
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
                return PosUtil.flooredBlockPos(callGetPos().getX() - 5, callGetPos().getY(), callGetPos().getZ() + 1);
            case SOUTH:
                return PosUtil.flooredBlockPos(callGetPos().getX() - 5, callGetPos().getY(), callGetPos().getZ() - 11);
            case WEST:
                return PosUtil.flooredBlockPos(callGetPos().getX() + 1, callGetPos().getY(), callGetPos().getZ() - 5);
            case EAST:
                return PosUtil.flooredBlockPos(callGetPos().getX() - 11, callGetPos().getY(), callGetPos().getZ() - 5);
            default:
                return null;
        }
    }

    public BlockPos getDefaultRangeMaxPos() {
        // default
        switch (getFacing()) {
            case NORTH:
                return PosUtil.flooredBlockPos(callGetPos().getX() + 6, callGetPos().getY() + 4, callGetPos().getZ() + 12);
            case SOUTH:
                return PosUtil.flooredBlockPos(callGetPos().getX() + 6, callGetPos().getY() + 4, callGetPos().getZ());

            case WEST:
                return PosUtil.flooredBlockPos(callGetPos().getX() + 12, callGetPos().getY() + 4, callGetPos().getZ() + 6);
            case EAST:
                return PosUtil.flooredBlockPos(callGetPos().getX(), callGetPos().getY() + 4, callGetPos().getZ() + 6);
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
        if (WorldUtil.isClient(callGetWorld())) return;

        if (isEnchanted()) {
            if (drop) {
                List<ItemStack> drops = Block.getDroppedStacks(WorldUtil.getBlockState(callGetWorld(), pos), (ServerWorld) callGetWorld(), pos, callGetWorld().getBlockEntity(pos), null, getQuarryStack());
                drops.forEach(this::addStack);
            }
            WorldUtil.breakBlock(callGetWorld(), pos, false);
        } else {
            WorldUtil.breakBlock(callGetWorld(), pos, drop);
        }
    }

    private Boolean CACHE_isEnchanted = null;

    public boolean isEnchanted() {
        if (CACHE_isEnchanted != null) return CACHE_isEnchanted;

        for (ItemStack stack : getModuleStacks()) {
            Item item = ItemStackUtil.getItem(stack);
            if (!(item instanceof MachineModule)) continue;

            MachineModule module = (MachineModule) item;
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

        Map<CompatEnchantment, Integer> enchantments = EnchantmentUtil.getEnchantment(stack, callGetWorld());

        if (isEmptyInModules()) return;
        for (ItemStack moduleStack : getModuleStacks()) {
            if (!(moduleStack.getItem() instanceof MachineModule)) continue;
            MachineModule module = (MachineModule) moduleStack.getItem();

            if (module.getEnchantments() == null) continue;
            enchantments.putAll(module.getEnchantments());
            applied = true;
        }

        if (applied)
            EnchantmentUtil.setEnchantment(stack, enchantments, callGetWorld());
    }

    public void tryDeleteMob(BlockPos blockPos) {
        if (callGetWorld() == null || callGetWorld().isClient()) return;
        List<MobEntity> mobs = WorldUtil.getEntitiesByClass(callGetWorld(), MobEntity.class, BoxUtil.createBox(blockPos.add(-2, -2, -2), blockPos.add(2, 2, 2)), EntityPredicates.VALID_ENTITY);
        mobs.forEach(EntityUtil::discard);
    }

    public void tryKillMob(BlockPos blockPos) {
        if (callGetWorld() == null || callGetWorld().isClient()) return;
        List<MobEntity> mobs = WorldUtil.getEntitiesByClass(callGetWorld(), MobEntity.class, BoxUtil.createBox(blockPos.add(-2, -2, -2), blockPos.add(2, 2, 2)), EntityPredicates.VALID_ENTITY);
        mobs.forEach(EntityUtil::kill);
    }

    public void tryCollectExp(BlockPos blockPos) {
        if (callGetWorld() == null || callGetWorld().isClient()) return;
        List<ExperienceOrbEntity> entities = WorldUtil.getEntitiesByClass(callGetWorld(), ExperienceOrbEntity.class, BoxUtil.createBox(blockPos.add(-2, -2, -2), blockPos.add(2, 2, 2)), EntityPredicates.VALID_ENTITY);
        entities.forEach((entity) -> {
            if (getStoredExp() + entity.getExperienceAmount() > getMaxStoredExp()) return;
            addStoredExp(entity.getExperienceAmount());
            EntityUtil.discard(entity);
        });
    }

    public double tryFluidReplace(net.pitan76.mcpitanlib.midohra.util.math.BlockPos pos) {
        return tryFluidReplace(pos.toMinecraft());
    }

    public double tryFluidReplace(BlockPos blockPos) {
        double time = 0;
        if(getEnergy() < getReplaceFluidEnergyCost()) {
            return 0;
        }

        for (Direction value : Direction.values()) {
            BlockPos offsetBlockPos = blockPos.offset(value);

            if (WorldUtil.getBlockState(callGetWorld(), offsetBlockPos).getBlock() instanceof FluidBlock) {
                // replace fluid block
                WorldUtil.setBlockState(callGetWorld(), offsetBlockPos, getReplaceFluidWithBlock().getDefaultState());
                time += 0.1;
            }

            if (!WorldUtil.getFluidState(callGetWorld(), offsetBlockPos).isEmpty() || callGetWorld().getFluidState(offsetBlockPos).isStill()) {
                breakBlock(offsetBlockPos, true);

                List<ItemEntity> entities = ItemEntityUtil.getEntities(callGetWorld(), BoxUtil.createBox(blockPos.add(-1, -1, -1), blockPos.add( 1,  1, 1)));
                for (ItemEntity itemEntity : entities) {
                    addStack(ItemEntityUtil.getStack(itemEntity));
                    EntityUtil.kill(itemEntity);
                }

                WorldUtil.setBlockState(callGetWorld(), offsetBlockPos, getReplaceFluidWithBlock().getDefaultState());
                time += 0.1;
            }
        }

        return time;
    }

    public boolean tryPlaceFrame(net.pitan76.mcpitanlib.midohra.util.math.BlockPos pos) {
        return tryPlaceFrame(pos.toMinecraft());
    }

    public boolean tryPlaceFrame(BlockPos blockPos) {
        if (getEnergy() > getPlaceFrameEnergyCost()) {
            Block block = WorldUtil.getBlockState(callGetWorld(), blockPos).getBlock();
            if (block instanceof Frame) return false;
            if (
                    ((PosUtil.x(blockPos) == PosUtil.x(minPos)
                    || PosUtil.z(blockPos) == PosUtil.z(minPos)
                    || PosUtil.x(blockPos) + 1 == PosUtil.x(maxPos)
                    || PosUtil.z(blockPos) + 1 == PosUtil.z(maxPos))
                            && (PosUtil.y(blockPos) == PosUtil.y(minPos)
                            || PosUtil.y(blockPos) == PosUtil.y(maxPos))
                    )
                    ||
                    (
                            (PosUtil.x(blockPos) == PosUtil.x(minPos) && PosUtil.z(blockPos) == PosUtil.z(minPos))
                                    || (PosUtil.x(blockPos) + 1 == PosUtil.x(maxPos) && PosUtil.z(blockPos) + 1 == PosUtil.z(maxPos))
                                    || (PosUtil.x(blockPos) == PosUtil.x(minPos) && PosUtil.z(blockPos) + 1 == PosUtil.z(maxPos))
                                    || (PosUtil.x(blockPos) + 1 == PosUtil.x(maxPos) && PosUtil.z(blockPos) == PosUtil.z(minPos))
                    )
            ) {
                WorldUtil.setBlockState(callGetWorld(), blockPos, Frame.getPlacementStateByTile(callGetWorld(), blockPos));
                return true;
            }

        }
        return false;
    }

    public boolean tryQuarrying() {
        if (callGetWorld() == null || WorldUtil.isClient(callGetWorld())) return false;
        if (minPos == null)
            minPos = getDefaultRangeMinPos();
        if (maxPos == null)
            maxPos = getDefaultRangeMaxPos();
        int procX;
        int procY;
        int procZ;
        for (procY = PosUtil.y(maxPos); procY > WorldUtil.getBottomY(callGetWorld()); procY--) {
            if (PosUtil.y(minPos) - 1 >= procY) {
                for (procX = PosUtil.x(minPos) + 1; procX < PosUtil.x(maxPos) - 1; procX++) {
                    for (procZ = PosUtil.z(minPos) + 1; procZ < PosUtil.z(maxPos) - 1; procZ++) {
                        BlockPos procPos = PosUtil.flooredBlockPos(procX, procY, procZ);
                        if (WorldUtil.getBlockState(callGetWorld(), procPos) == null) continue;
                        if (WorldUtil.getBlockEntity(callGetWorld(), procPos) instanceof QuarryTile && callGetWorld().getBlockEntity(procPos) == this) continue;

                        Block procBlock = WorldUtil.getBlockState(callGetWorld(), procPos).getBlock();
                        if (procBlock instanceof AirBlock || (UnbreakableBlocks.isUnbreakable(procBlock) && !hasModuleItem(ModuleItems.BEDROCK_BREAK_MODULE))) {
                            if (canReplaceFluid()) {
                                double time = tryFluidReplace(procPos);
                                if (time != 0) {
                                    useEnergy((long) time * getReplaceFluidEnergyCost());
                                }
                            }
                            continue;
                        }
                        if (PosUtil.y(minPos) - 1 >= procY) {
                            if ( procBlock instanceof FluidBlock) {
                                if (canReplaceFluid()
                                        && WorldUtil.getFluidState(callGetWorld(), procPos).isStill()
                                        && getEnergy() > getReplaceFluidEnergyCost()) {
                                    WorldUtil.setBlockState(callGetWorld(), procPos, BlockStateUtil.getDefaultState(getReplaceFluidWithBlock()));
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
                            List<ItemEntity> entities = ItemEntityUtil.getEntities(callGetWorld(), BoxUtil.createBox(PosUtil.flooredBlockPos(procX - 1, procY - 1, procZ - 1), PosUtil.flooredBlockPos(procX + 1, procY + 1, procZ + 1)));
                            if (entities.isEmpty()) return true;
                            for(ItemEntity itemEntity : entities) {
                                addStack(ItemEntityUtil.getStack(itemEntity));
                                EntityUtil.kill(itemEntity);
                            }
                            return true;
                        }
                    }
                }
            }
            else if (PosUtil.y(minPos) <= procY && PosUtil.y(maxPos) >= procY) {
                // procX < pos2.getX()を=<するとposのずれ問題は修正可能だが、別の方法で対処しているので、時間があればこっちで修正したい。
                for (procX = PosUtil.x(minPos); procX < PosUtil.x(maxPos); procX++) {
                    for (procZ = PosUtil.z(minPos); procZ < PosUtil.z(maxPos); procZ++) {
                        BlockPos procPos = PosUtil.flooredBlockPos(procX, procY, procZ);
                        if (WorldUtil.getBlockState(callGetWorld(), procPos) == null) continue;
                        if (WorldUtil.getBlockEntity(callGetWorld(), procPos) instanceof QuarryTile && WorldUtil.getBlockEntity(callGetWorld(), procPos) == this) continue;

                        Block procBlock = WorldUtil.getBlockState(callGetWorld(), procPos).getBlock();
                        if (procBlock instanceof AirBlock || (UnbreakableBlocks.isUnbreakable(procBlock) && !hasModuleItem(ModuleItems.BEDROCK_BREAK_MODULE))) {
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
                                && !FluidUtil.isStill(WorldUtil.getFluidState(callGetWorld(), procPos))) {
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
        int size = getItems().size();
        for (int i = 0; i < size; i++) {
            if (InventoryUtil.getStack(inventory, i).isEmpty())
                return i;

            ItemStack inStack = getItems().get(i);
            if (stack.getItem().equals(inStack.getItem()) && (ItemStackUtil.areNbtOrComponentEqual(stack, inStack) || !ItemStackUtil.hasNbtOrComponent(stack) == !ItemStackUtil.hasNbtOrComponent(inStack)) && inStack.getItem().getMaxCount() != 1) {
                int originInCount = getItems().get(i).getCount();
                getItems().get(i).setCount(Math.min(stack.getMaxCount(), stack.getCount() + originInCount));
                if (stack.getMaxCount() >= stack.getCount() + originInCount) {
                    return i;
                }
            }
        }

        return null;
    }

    public void addStack(ItemStack stack) {
        if (callGetWorld() == null || callGetWorld().isClient()) return;

        if (isRemovalItem(stack)) return;

        int size = getItems().size();
        for (int i = 0; i < size; i++) {
            if (stack.isEmpty() || stack.getCount() == 0) return;
            if (getItems().get(i).isEmpty()) {
                getItems().set(i, stack);
                return;
            }
            ItemStack inStack = getItems().get(i);
            if (stack.getItem().equals(inStack.getItem()) && (ItemStackUtil.areNbtOrComponentEqual(stack, inStack) || !ItemStackUtil.hasNbtOrComponent(stack) == !ItemStackUtil.hasNbtOrComponent(inStack)) && inStack.getItem().getMaxCount() != 1) {
                int originInCount = getItems().get(i).getCount();
                getItems().get(i).setCount(Math.min(stack.getMaxCount(), stack.getCount() + originInCount));
                if (stack.getMaxCount() >= stack.getCount() + originInCount)
                    return;

                ItemStackUtil.setCount(stack, stack.getCount() + originInCount - stack.getMaxCount());
            }
        }

        ItemEntityUtil.createWithSpawn(callGetWorld(), stack, callGetPos());
    }

    public QuarryTile(BlockEntityType<?> type, TileCreateEvent e) {
        super(type, e);
    }

    @Override
    public ItemStackList getItems() {
        return invItems;
    }

    @Override
    public int[] getAvailableSlots(AvailableSlotsArgs args) {
        int[] result = new int[getItems().size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }
        return result;
    }

    @Override
    public boolean canInsert(CanInsertArgs args) {
        return false;
    }

    @Override
    public boolean canExtract(CanExtractArgs args) {
        return true;
    }
}
