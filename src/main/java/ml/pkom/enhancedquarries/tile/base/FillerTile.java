package ml.pkom.enhancedquarries.tile.base;

import ml.pkom.enhancedquarries.Items;
import ml.pkom.enhancedquarries.block.base.Filler;
import ml.pkom.enhancedquarries.event.FillerModuleReturn;
import ml.pkom.enhancedquarries.event.FillerProcessEvent;
import ml.pkom.enhancedquarries.item.base.FillerModuleItem;
import ml.pkom.enhancedquarries.screen.FillerScreenHandler;
import ml.pkom.mcpitanlibarch.api.gui.inventory.IInventory;
import ml.pkom.mcpitanlibarch.api.util.ItemUtil;
import ml.pkom.mcpitanlibarch.api.util.TextUtil;
import ml.pkom.mcpitanlibarch.api.util.WorldUtil;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FillerTile extends BaseEnergyTile implements IInventory, SidedInventory, NamedScreenHandlerFactory {

    // Container
    public DefaultedList<ItemStack> invItems = DefaultedList.ofSize(27, ItemStack.EMPTY);
    public DefaultedList<ItemStack> craftingInvItems = DefaultedList.ofSize(10, ItemStack.EMPTY);

    public IInventory craftingInventory = () -> craftingInvItems;
    public IInventory inventory = this;

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

    // ブロック1回設置分に対するエネルギーのコスト
    public long getEnergyCost() {
        return 30;
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

    public void writeNbtOverride(NbtCompound tag) {
        NbtCompound invTag = new NbtCompound();
        Inventories.writeNbt(invTag, craftingInvItems);
        tag.put("craftingInv", invTag);

        NbtCompound itemsNbt = new NbtCompound();
        Inventories.writeNbt(itemsNbt, getItems());
        tag.put("Items", itemsNbt);

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

        super.writeNbtOverride(tag);
    }

    public void readNbtOverride(NbtCompound tag) {
        super.readNbtOverride(tag);
        if (tag.contains("craftingInv")) {
            NbtCompound invTag = tag.getCompound("craftingInv");
            Inventories.readNbt(invTag, craftingInvItems);
        }

        if (tag.contains("Items")) {
            NbtCompound itemsNbt = tag.getCompound("Items");
            Inventories.readNbt(itemsNbt, getItems());
        }

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

    // 基準の速度
    public double getBasicSpeed() {
        return 5;
    }

    public double defaultSettingCoolTime = 300;

    // クールダウンの基準
    public double getSettingCoolTime() {
        return defaultSettingCoolTime;
    }

    public double coolTime = getSettingCoolTime();

    public void tick(World world, BlockPos pos, BlockState state, BaseEnergyTile blockEntity) {
        super.tick(world, pos, state, blockEntity);
        if (world.isClient()) return;

        // レッドストーン受信で無効
        if (WorldUtil.isReceivingRedstonePower(world, getPos())) {
            if (isActive())
                Filler.setActive(false, world, getPos());
            return;
        }
        if (!hasModule()) return;

        if (getEnergy() > getEnergyCost()) {
            // ここに処理を記入
            if (coolTime <= 0) {
                coolTime = getSettingCoolTime();
                if (tryFilling(getModule().getItem())) {
                    useEnergy(getEnergyCost());
                }
            }
            coolTimeBonus();
            coolTime = coolTime - getBasicSpeed();
            if (!isActive())
                Filler.setActive(true, world, getPos());

        } else if (isActive()) {
            Filler.setActive(false, world, getPos());
        }
    }

    public ItemStack latestGotStack = ItemStack.EMPTY;

    public static boolean isStorageBox(ItemStack stack) {
        return ItemUtil.toID(stack.getItem()).toString().equals("storagebox:storagebox");
    }

    public ItemStack getInventoryStack() {
        for (ItemStack stack : getItems()) {
            latestGotStack = stack;
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof BlockItem) return stack;
            // StorageBox
            if (isStorageBox(stack)) {
                NbtCompound tag = stack.getNbt();
                if (tag.contains("StorageItemData")) {
                    ItemStack itemInBox = ItemStack.fromNbt(tag.getCompound("StorageItemData"));
                    if (itemInBox.getItem() instanceof BlockItem) return itemInBox;
                }
            }
            // ---- StorageBox
        }
        return ItemStack.EMPTY;
    }

    public static int moduleInterval = 6;

    public int getModuleInterval() {
        return moduleInterval;
    }

    public boolean tryPlacing(BlockPos blockPos, Block block, ItemStack stack) {
        if (getWorld().setBlockState(blockPos, block.getDefaultState())) {
            getWorld().playSound(null, blockPos, block.getSoundGroup(block.getDefaultState()).getPlaceSound(), SoundCategory.BLOCKS, 1F, 1F);
            if (isStorageBox(latestGotStack)) {
                NbtCompound tag = latestGotStack.getNbt();
                if (tag.contains("StorageSize")) {
                    int countInBox = tag.getInt("StorageSize");
                    countInBox--;
                    tag.putInt("StorageSize", countInBox);
                    if (countInBox <= 0) {
                        tag.remove("StorageItemData");
                        tag.remove("StorageSize");
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
        for (procY = WorldUtil.getBottomY(getWorld()); procY <= WorldUtil.getDimensionHeight(getWorld()); procY++) {
            for (procX = pos1.getX(); procX <= pos2.getX(); procX++) {
                for (procZ = pos1.getZ(); procZ <= pos2.getZ(); procZ++) {
                    BlockPos procPos = new BlockPos(procX, procY, procZ);
                    Block procBlock = getWorld().getBlockState(procPos).getBlock();
                    if (getWorld().getBlockEntity(procPos) instanceof QuarryTile && getWorld().getBlockEntity(procPos) == this) continue;

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
                    if (procY <= getWorld().getDimension().height() && procY >= pos2.getY() + 1) {
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
                    if (procY >= getWorld().getBottomY() && procY <= pos1.getY() - 1) {
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
        return dir != Direction.DOWN;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return dir == Direction.DOWN;
    }

    @Override
    public Text getDisplayName() {
        return TextUtil.translatable("screen.enhanced_quarries.filler.title");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new FillerScreenHandler(syncId, playerInventory, this, getCraftingInventory());
    }
}
