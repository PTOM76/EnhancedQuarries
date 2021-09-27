package ml.pkom.enhancedquarries.tile.base;

import ml.pkom.enhancedquarries.Items;
import ml.pkom.enhancedquarries.block.base.Filler;
import ml.pkom.enhancedquarries.event.FillerModuleReturn;
import ml.pkom.enhancedquarries.event.FillerProcessEvent;
import ml.pkom.enhancedquarries.inventory.ImplementedInventory;
import ml.pkom.enhancedquarries.item.base.FillerModuleItem;
import ml.pkom.enhancedquarries.mixin.MachineBaseBlockEntityAccessor;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.SlotConfiguration;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.util.RebornInventory;

public class FillerTile extends PowerAcceptorBlockEntity implements InventoryProvider {// implements IInventory {

    // Container
    public RebornInventory<FillerTile> inventory = new RebornInventory<>(27, "FillerTile", 64, this);

    //public DefaultedList<ItemStack> invItems = DefaultedList.ofSize(27, ItemStack.EMPTY);
    public DefaultedList<ItemStack> craftingInvItems = DefaultedList.ofSize(10, ItemStack.EMPTY);

    //public ImplementedInventory inventory = () -> invItems;
    public ImplementedInventory craftingInventory = () -> craftingInvItems;

    public RebornInventory<FillerTile> getInventory() {
        return inventory;
    }

    public Inventory getCraftingInventory() {
        return craftingInventory;
    }

    public boolean hasModule() {
        return !getModule().isEmpty();
    }

    public ItemStack getModule() {
        return getCraftingInventory().getStack(9).copy();
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

    // ----


    // TR
    // デフォルトコスト
    private long defaultEnergyCost = 30;

    // ブロック1回設置分に対するエネルギーのコスト
    public long getEnergyCost() {
        return defaultEnergyCost;
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

    public NbtCompound writeNbt(NbtCompound tag) {
        NbtCompound invTag = new NbtCompound();
        Inventories.writeNbt(invTag, craftingInvItems);
        tag.put("craftingInv", invTag);
        tag.putDouble("coolTime", coolTime);
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

        if (canBedrockBreak)
            tag.putBoolean("module_bedrock_break", true);
        return super.writeNbt(tag);
    }

    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        NbtCompound invTag = tag.getCompound("craftingInv");
        Inventories.readNbt(invTag, craftingInvItems);

        if (tag.contains("coolTime")) coolTime = tag.getDouble("coolTime");
        if (tag.contains("rangePos1X")
                && tag.contains("rangePos1Y")
                && tag.contains("rangePos1Z")
                && tag.contains("rangePos2X")
                && tag.contains("rangePos2Y")
                && tag.contains("rangePos2Z")) {
            setPos1(new BlockPos(tag.getInt("rangePos1X"), tag.getInt("rangePos1Y"), tag.getInt("rangePos1Z")));
            setPos2(new BlockPos(tag.getInt("rangePos2X"), tag.getInt("rangePos2Y"), tag.getInt("rangePos2Z")));
        }

        if (tag.contains("module_bedrock_break"))
            canBedrockBreak = tag.getBoolean("module_bedrock_break");
    }

    // ----

    private double defaultBasicSpeed = 5;

    // 基準の速度
    public double getBasicSpeed() {
        return defaultBasicSpeed;
    }

    // TR用のTick
    public void TRTick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
        super.tick(world, pos, state, blockEntity);
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
        Filler filler = (Filler) state.getBlock();

        // レッドストーン受信で無効
        if (getWorld().isReceivingRedstonePower(getPos())) {
            if (isActive()) {
                filler.setActive(false, getWorld(), getPos());
            }
            return;
        }
        if (!hasModule()){
            return;
        }
        if (getEnergy() > getEuPerTick(getEnergyCost())) {
            // ここに処理を記入
            if (coolTime <= 0) {
                coolTime = getSettingCoolTime();
                if (tryFilling(getModule().getItem())) {
                    useEnergy(getEnergyCost());
                }
            }
            coolTimeBonus();
            coolTime = coolTime - getBasicSpeed();
            if (!isActive()) {
                filler.setActive(true, getWorld(), getPos());
            }
        } else if (isActive()) {
            filler.setActive(false, getWorld(), getPos());
        }
    }

    public ItemStack latestGotStack = ItemStack.EMPTY;

    public static boolean isStorageBox(ItemStack stack) {
        return Registry.ITEM.getId(stack.getItem()).toString().equals("storagebox:storagebox");
    }

    public ItemStack getInventoryStack() {
        for (ItemStack stack : getInventory().getStacks()) {
            latestGotStack = stack;
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof BlockItem) return stack;
            // StorageBox
            if (isStorageBox(stack)) {
                NbtCompound tag = stack.getNbt();
                if (tag.contains("item")) {
                    ItemStack itemInBox = ItemStack.fromNbt(tag.getCompound("item"));
                    if (itemInBox.getItem() instanceof BlockItem) return itemInBox;
                }
            }
            // ---- StorageBox
        }
        return ItemStack.EMPTY;
    }

    public static int moduleInterval = 6;

    public int getModuleInterval() {
        /*
        if (!getModule().hasTag()) return 6;
        NbtCompound tag = getModule().getTag();
        if (!tag.contains("interval")) return 6;
        return tag.getInt("interval");

         */
        return moduleInterval;
    }

    public boolean tryPlacing(BlockPos blockPos, Block block, ItemStack stack) {
        if (getWorld().setBlockState(blockPos, block.getDefaultState())) {
            getWorld().playSound(null, blockPos, block.getSoundGroup(block.getDefaultState()).getPlaceSound(), SoundCategory.BLOCKS, 1F, 1F);
            if (isStorageBox(latestGotStack)) {
                NbtCompound tag = latestGotStack.getNbt();
                if (tag.contains("countInBox")) {
                    int countInBox = tag.getInt("countInBox");
                    countInBox--;
                    tag.putInt("countInBox", countInBox);
                    if (countInBox <= 0) {
                        tag.remove("item");
                        tag.remove("countInBox");
                    }
                    latestGotStack.setNbt(tag);
                }
                return true;
            }
            latestGotStack.setCount(latestGotStack.getCount() - 1);
            return true;
        }
        return false;

    }

    public boolean tryBreaking(BlockPos procPos) {
        return getWorld().breakBlock(procPos, true);
    }

    public boolean tryFilling(Item item) {
        if (getWorld() == null || getWorld().isClient()) return false;
        if (pos1 == null || pos2 == null)
            return false;
        int procX;
        int procY;
        int procZ;
        //procY = pos1.getY(); procY <= pos2.getY(); procY++
        for (procY = 0; procY <= getWorld().getDimension().getHeight(); procY++) {
            for (procX = pos1.getX(); procX <= pos2.getX(); procX++) {
                for (procZ = pos1.getZ(); procZ >= pos2.getZ(); procZ--) {
                    BlockPos procPos = new BlockPos(procX, procY, procZ);
                    Block procBlock = getWorld().getBlockState(procPos).getBlock();
                    if ( procY <= pos2.getY() && procY >= pos1.getY()) {
                        // 埋め立てモジュール
                        if (item.equals(Items.fillerALL_FILL)) {
                            if (procBlock instanceof AirBlock || procBlock instanceof FluidBlock) {
                                ItemStack stack = getInventoryStack();
                                if (stack.isEmpty()) return false;
                                Block block = Block.getBlockFromItem(stack.getItem());
                                if (block.equals(procBlock)) continue;
                                if (tryPlacing(procPos, block, stack)) return true;
                            }
                        }
                        // 消去モジュール
                        if (item.equals(Items.fillerALL_DELETE)) {
                            if (procBlock instanceof AirBlock || (procBlock.equals(Blocks.BEDROCK) && !canBedrockBreak)) continue;
                            return getWorld().removeBlock(procPos, false);
                        }
                        // 撤去モジュール
                        if (item.equals(Items.fillerALL_REMOVE)) {
                            if (procBlock instanceof AirBlock || (procBlock.equals(Blocks.BEDROCK) && !canBedrockBreak)) continue;
                            return tryBreaking(procPos);
                        }
                        // 整地モジュール
                        if (item.equals(Items.fillerLEVELING)) {
                            if (!(procBlock instanceof AirBlock) && !(procBlock.equals(Blocks.BEDROCK) && !canBedrockBreak))
                                return tryBreaking(procPos);
                        }
                        // 箱モジュール
                        if (item.equals(Items.fillerBOX)) {
                            if ((procBlock instanceof AirBlock || procBlock instanceof FluidBlock)
                            && (
                                    procX == pos1.getX() || procX == pos2.getX() || procZ == pos1.getZ() || procZ == pos2.getZ() // 壁
                                || procY == pos1.getY() || procY == pos2.getY())) {
                                ItemStack stack = getInventoryStack();
                                if (stack.isEmpty()) return false;
                                Block block = Block.getBlockFromItem(stack.getItem());
                                if (block.equals(procBlock)) continue;
                                if (tryPlacing(procPos, block, stack)) return true;
                            }
                        }
                        // 壁モジュール
                        if (item.equals(Items.fillerWALL)) {
                            if ((procBlock instanceof AirBlock || procBlock instanceof FluidBlock)
                                    && (
                                    procX == pos1.getX() || procX == pos2.getX() || procZ == pos1.getZ() || procZ == pos2.getZ()
                            )) {
                                ItemStack stack = getInventoryStack();
                                if (stack.isEmpty()) return false;
                                Block block = Block.getBlockFromItem(stack.getItem());
                                if (block.equals(procBlock)) continue;
                                if (tryPlacing(procPos, block, stack)) return true;
                            }
                        }
                        // 松明モジュール
                        if (item.equals(Items.fillerTORCH)) {
                            if ((procBlock instanceof AirBlock || procBlock instanceof FluidBlock)
                                    && (
                                    ((procY - pos1.getY() + getModuleInterval()) % getModuleInterval() == 0)
                                            && ((procX - pos1.getX() + getModuleInterval()) % getModuleInterval() == 0)
                                            && ((procZ - pos1.getZ() + getModuleInterval()) % getModuleInterval() == 0)
                            ) && !(getWorld().getBlockState(procPos.down()).getBlock() instanceof AirBlock)) {
                                ItemStack stack = getInventoryStack();
                                if (stack.isEmpty()) return false;
                                Block block = Block.getBlockFromItem(stack.getItem());
                                if (block.equals(procBlock)) continue;
                                if (tryPlacing(procPos, block, stack)) return true;
                            }
                        }
                        // - 登録モジュールの処理
                        FillerModuleReturn returnEvent = null;
                        for(FillerModuleItem fillerModule : ml.pkom.enhancedquarries.registry.Registry.getINSTANCE().getModules()) {
                            if (item.equals(fillerModule)) {
                                returnEvent = fillerModule.onProcessInRange(new FillerProcessEvent(this, procPos, procBlock));
                                break;
                            }
                        }
                        if (returnEvent != null) {
                            if (returnEvent.equals(FillerModuleReturn.RETURN_FALSE)) return false;
                            if (returnEvent.equals(FillerModuleReturn.RETURN_TRUE)) return true;
                            if (returnEvent.equals(FillerModuleReturn.BREAK)) break;
                            if (returnEvent.equals(FillerModuleReturn.CONTINUE)) continue;
                        }
                        // ----
                    }
                    // 選択範囲より上～最高域
                    if (procY <= getWorld().getDimension().getHeight() && procY >= pos2.getY() + 1) {
                        // 整地モジュール
                        if (item.equals(Items.fillerLEVELING)) {
                            if (!(procBlock instanceof AirBlock))
                                return tryBreaking(procPos);
                        }
                        // - 登録モジュールの処理
                        FillerModuleReturn returnEvent = null;
                        for(FillerModuleItem fillerModule : ml.pkom.enhancedquarries.registry.Registry.getINSTANCE().getModules()) {
                            if (item.equals(fillerModule)) {
                                returnEvent = fillerModule.onProcessOnRange(new FillerProcessEvent(this, procPos, procBlock));
                                break;
                            }
                        }
                        if (returnEvent != null) {
                            if (returnEvent.equals(FillerModuleReturn.RETURN_FALSE)) return false;
                            if (returnEvent.equals(FillerModuleReturn.RETURN_TRUE)) return true;
                            if (returnEvent.equals(FillerModuleReturn.BREAK)) break;
                            if (returnEvent.equals(FillerModuleReturn.CONTINUE)) continue;
                        }
                        // ----
                    }
                    // 選択範囲より下～0
                    if (procY >= 0 && procY <= pos1.getY() - 1) {
                        // 整地モジュール
                        if (item.equals(Items.fillerLEVELING)) {
                            if (procBlock instanceof AirBlock || procBlock instanceof FluidBlock) {
                                ItemStack stack = getInventoryStack();
                                if (stack.isEmpty()) continue;
                                Block block = Block.getBlockFromItem(stack.getItem());
                                if (block.equals(procBlock)) continue;
                                if (tryPlacing(procPos, block, stack)) return true;
                            }
                        }
                        // - 登録モジュールの処理
                        FillerModuleReturn returnEvent = null;
                        for(FillerModuleItem fillerModule : ml.pkom.enhancedquarries.registry.Registry.getINSTANCE().getModules()) {
                            if (item.equals(fillerModule)) {
                                returnEvent = fillerModule.onProcessUnderRange(new FillerProcessEvent(this, procPos, procBlock));
                                break;
                            }
                        }
                        if (returnEvent != null) {
                            if (returnEvent.equals(FillerModuleReturn.RETURN_FALSE)) return false;
                            if (returnEvent.equals(FillerModuleReturn.RETURN_TRUE)) return true;
                            if (returnEvent.equals(FillerModuleReturn.BREAK)) break;
                            if (returnEvent.equals(FillerModuleReturn.CONTINUE)) continue;
                        }
                        // ----
                    }

                }
            }
        }
        return false;
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

    public FillerTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void init() {
        int index = 0;
        SlotConfiguration.SlotIO output = new SlotConfiguration.SlotIO(SlotConfiguration.ExtractConfig.OUTPUT);
        SlotConfiguration.SlotIO input = new SlotConfiguration.SlotIO(SlotConfiguration.ExtractConfig.INPUT);
        SlotConfiguration slotConfig = new SlotConfiguration(getInventory());
        for (;index < getInventory().size();index++){
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.NORTH, input, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.SOUTH, input, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.EAST, input, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.WEST, input, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.UP, input, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.DOWN, output, index));
            markDirty();
        }
        ((MachineBaseBlockEntityAccessor) this).setSlotConfiguration(slotConfig);
    }
}
