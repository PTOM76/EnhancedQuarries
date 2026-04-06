package net.pitan76.enhancedquarries.tile.base;

import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
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
import net.pitan76.mcpitanlib.api.util.nbt.v2.NbtRWUtil;
import net.pitan76.mcpitanlib.api.util.world.ServerWorldUtil;
import net.pitan76.mcpitanlib.midohra.block.BlockWrapper;
import net.pitan76.mcpitanlib.midohra.block.MCBlocks;
import net.pitan76.mcpitanlib.midohra.block.entity.BlockEntityWrapper;
import net.pitan76.mcpitanlib.midohra.entity.EntityWrapper;
import net.pitan76.mcpitanlib.midohra.entity.ItemEntityWrapper;
import net.pitan76.mcpitanlib.midohra.item.MCItems;
import net.pitan76.mcpitanlib.midohra.util.math.BlockPos;
import net.pitan76.mcpitanlib.midohra.util.math.Box;
import net.pitan76.mcpitanlib.midohra.util.math.Direction;
import net.pitan76.mcpitanlib.midohra.world.ServerWorld;
import net.pitan76.mcpitanlib.midohra.world.World;

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

    // 液体を何に置き換えるか？
    public BlockWrapper getReplaceFluidWithBlockWrapper() {
        return MCBlocks.GLASS;
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
            NbtRWUtil.putInt(args, "rangePos1X", minPos.getX());
            NbtRWUtil.putInt(args, "rangePos1Y", minPos.getY());
            NbtRWUtil.putInt(args, "rangePos1Z", minPos.getZ());
        }
        if (maxPos != null) {
            NbtRWUtil.putInt(args, "rangePos2X", maxPos.getX());
            NbtRWUtil.putInt(args, "rangePos2Y", maxPos.getY());
            NbtRWUtil.putInt(args, "rangePos2Z", maxPos.getZ());
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

        setMinPos(BlockPos.of(pos1x, pos1y, pos1z));
        setMaxPos(BlockPos.of(pos2x, pos2y, pos2z));

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
        World world = e.getMidohraWorld();
        BlockPos pos = e.getMidohraPos();
        if (world.toMinecraft() == null || e.isClient()) return;

        // レッドストーン受信で無効
        if (world.isReceivingRedstonePower(pos)) {
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

            if (world.getTime() % 2L == 0L)
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

                long amount = StorageUtil.move(InventoryStorage.of(this, null).getSlot(i),
                        ItemStorage.SIDED.find(callGetWorld(),
                                getMidohraPos().offset(dir).toMinecraft(),
                                dir.getOpposite().toMinecraft()),
                        (iv) -> true,
                        Long.MAX_VALUE, null);
                if (amount < stack.getCount()) continue;

                ++time;
                break;
            }
        }
    }

    public List<Direction> getDirsOfAnyContainerBlock() {
        if (callGetWorld() == null) return new ArrayList<>();
        World world = getMidohraWorld();
        BlockPos tilePos = getMidohraPos();

        List<Direction> usableDirs = new ArrayList<>();

        for (Direction dir : Direction.values()) {
            BlockPos pos = tilePos.offset(dir);
            if (world.getBlockEntity(pos) == null) continue;
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
        BlockPos pos = getMidohraPos();

        switch (getFacing()) {
            case NORTH:
                return pos.add(-5, 0, 1);
            case SOUTH:
                return pos.add(-5, 0, -11);
            case WEST:
                return pos.add(1, 0, -5);
            case EAST:
                return pos.add(-11, 0, -5);
            default:
                return null;
        }
    }

    public BlockPos getDefaultRangeMaxPos() {
        // default
        BlockPos pos = getMidohraPos();

        switch (getFacing()) {
            case NORTH:
                return pos.add(6, 4, 12);
            case SOUTH:
                return pos.add(6, 4, 0);
            case WEST:
                return pos.add(12, 4, 6);
            case EAST:
                return pos.add(0, 4, 6);
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
        World world = getMidohraWorld();
        if (world.isClient()) return;

        if (isEnchanted()) {
            if (drop) {
                ServerWorld serverWorld = world.toServerWorld().get();
                List<ItemStack> drops = ServerWorldUtil.getDroppedStacksOnBlock(world.getBlockState(pos).toMinecraft(), serverWorld.toMinecraft(), pos.toMinecraft(), world.getBlockEntity(pos).get(), null, getQuarryStack());
                drops.forEach(this::addStack);
            }
            world.breakBlock(pos, false);
        } else {
            world.breakBlock(pos, drop);
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
        ItemStack stack = MCItems.DIAMOND_PICKAXE.createStack().toMinecraft();
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
        World world = getMidohraWorld();
        if (world.toMinecraft() == null || world.isClient()) return;

        List<EntityWrapper> mobs = world.getMobs(new Box(blockPos.add(-2, -2, -2), blockPos.add(2, 2, 2)));
        mobs.forEach(EntityWrapper::discard);
    }

    public void tryKillMob(BlockPos blockPos) {
        World world = getMidohraWorld();
        if (world.toMinecraft() == null || world.isClient()) return;

        List<EntityWrapper> mobs = world.getMobs(new Box(blockPos.add(-2, -2, -2), blockPos.add(2, 2, 2)));
        mobs.forEach(EntityWrapper::kill);
    }

    public void tryCollectExp(BlockPos blockPos) {
        World world = getMidohraWorld();
        if (world.toMinecraft() == null || world.isClient()) return;

        List<ExperienceOrbEntity> entities = world.getEntitiesByClass(ExperienceOrbEntity.class, new Box(blockPos.add(-2, -2, -2), blockPos.add(2, 2, 2)));
        entities.forEach((entity) -> {
            if (getStoredExp() + entity.getExperienceAmount() > getMaxStoredExp()) return;
            addStoredExp(entity.getExperienceAmount());
            EntityUtil.discard(entity);
        });
    }

    public double tryFluidReplace(BlockPos blockPos) {
        double time = 0;
        if (getEnergy() < getReplaceFluidEnergyCost()) return 0;

        World world = getMidohraWorld();
        for (Direction value : Direction.values()) {
            BlockPos offsetBlockPos = blockPos.offset(value);

            if (world.getBlockState(offsetBlockPos).getBlock().get() instanceof FluidBlock) {
                // replace fluid block
                world.setBlockState(offsetBlockPos, getReplaceFluidWithBlockWrapper().getDefaultState());
                time += 0.1;
            }

            if (!world.getFluidState(offsetBlockPos.toMinecraft()).isEmpty() || callGetWorld().getFluidState(offsetBlockPos.toMinecraft()).isStill()) {
                breakBlock(offsetBlockPos, true);

                List<ItemEntityWrapper> entities = ItemEntityUtil.getEntityWrappers(world, new Box(blockPos.add(-1, -1, -1), blockPos.add(1 ,1 ,1)));
                for (ItemEntityWrapper itemEntity : entities) {
                    if (!itemEntity.isAlive()) continue;
                    addStack(itemEntity.getStackRaw());
                    itemEntity.discard();
                }

                world.setBlockState(offsetBlockPos, getReplaceFluidWithBlockWrapper().getDefaultState());
                time += 0.1;
            }
        }

        return time;
    }
    
    public boolean tryPlaceFrame(BlockPos blockPos) {
        if (getEnergy() > getPlaceFrameEnergyCost()) {
            World world = getMidohraWorld();
            Block block = world.getBlockState(blockPos).getBlock().get();
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
                world.setBlockState(blockPos, Frame.getPlacementStateByTile(world, blockPos));
                return true;
            }

        }
        return false;
    }

    public boolean tryQuarrying() {
        World world = getMidohraWorld();
        if (world.toMinecraft() == null || world.isClient()) return false;
        if (minPos == null)
            minPos = getDefaultRangeMinPos();
        if (maxPos == null)
            maxPos = getDefaultRangeMaxPos();
        int procX;
        int procY;
        int procZ;
        for (procY = maxPos.getY(); procY > world.getBottomY(); procY--) {
            if (minPos.getY() - 1 >= procY) {
                for (procX = minPos.getX() + 1; procX < maxPos.getX() - 1; procX++) {
                    for (procZ = minPos.getZ() + 1; procZ < maxPos.getZ() - 1; procZ++) {
                        BlockPos procPos = BlockPos.of(procX, procY, procZ);
                        if (world.getBlockState(procPos).toMinecraft() == null) continue;
                        if (world.getBlockEntity(procPos).get() instanceof QuarryTile && world.getBlockEntity(procPos).get() == this) continue;

                        Block procBlock = world.getBlockState(procPos).getBlock().get();
                        if (procBlock instanceof AirBlock || (UnbreakableBlocks.isUnbreakable(procBlock) && !hasModuleItem(ModuleItems.BEDROCK_BREAK_MODULE))) {
                            if (canReplaceFluid()) {
                                double time = tryFluidReplace(procPos);
                                if (time != 0) {
                                    useEnergy((long) time * getReplaceFluidEnergyCost());
                                }
                            }
                            continue;
                        }
                        if (minPos.getY() - 1 >= procY) {
                            if (procBlock instanceof FluidBlock) {
                                if (canReplaceFluid()
                                        && world.getFluidState(procPos.toMinecraft()).isStill()
                                        && getEnergy() > getReplaceFluidEnergyCost()) {
                                    world.setBlockState(procPos, getReplaceFluidWithBlockWrapper().getDefaultState());
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
                            List<ItemEntityWrapper> entities = ItemEntityUtil.getEntityWrappers(world, new Box(BlockPos.of(procX - 1, procY - 1, procZ - 1), BlockPos.of(procX + 1, procY + 1, procZ + 1)));
                            if (entities.isEmpty()) return true;
                            for (ItemEntityWrapper itemEntity : entities) {
                                if (!itemEntity.isAlive()) continue;
                                addStack(itemEntity.getStackRaw());
                                itemEntity.discard();
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
                        BlockPos procPos = BlockPos.of(procX, procY, procZ);
                        if (world.getBlockState(procPos).toMinecraft() == null) continue;
                        BlockEntityWrapper blockEntity = world.getBlockEntity(procPos);
                        if (blockEntity.get() instanceof QuarryTile && blockEntity.get() == this) continue;

                        Block procBlock = world.getBlockState(procPos).getBlock().get();
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
                                && !FluidUtil.isStill(world.getFluidState(procPos.toMinecraft()))) {
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

    @Override
    public boolean canInsertEnergy() {
        return true;
    }

    @Override
    public boolean canExtractEnergy() {
        return false;
    }
}
