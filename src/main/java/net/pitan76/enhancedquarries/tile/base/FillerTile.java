package net.pitan76.enhancedquarries.tile.base;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
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
import net.pitan76.enhancedquarries.block.base.Filler;
import net.pitan76.enhancedquarries.event.FillerModuleReturn;
import net.pitan76.enhancedquarries.event.FillerProcessEvent;
import net.pitan76.enhancedquarries.item.base.FillerModule;
import net.pitan76.enhancedquarries.registry.Registry;
import net.pitan76.enhancedquarries.screen.FillerScreenHandler;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.event.nbt.ReadNbtArgs;
import net.pitan76.mcpitanlib.api.event.nbt.WriteNbtArgs;
import net.pitan76.mcpitanlib.api.gui.inventory.IInventory;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.storagebox.api.StorageBoxUtil;
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

    // The maximum number of checks to do each `tryFill` call
    // Note it will still be limited to at most the width * length of the area.
    private int maxBlockChecks = 1000;
    public int maxBlockChecks() { return maxBlockChecks; }
    public void setMaxBlockChecks(int maxBlockChecks) { this.maxBlockChecks = maxBlockChecks; }

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

    public void writeNbt(WriteNbtArgs args) {
        NbtCompound tag = args.getNbt();
        NbtCompound invTag = new NbtCompound();
        InventoryUtil.writeNbt(getWorld(), invTag, craftingInvItems);
        tag.put("craftingInv", invTag);

        InventoryUtil.writeNbt(args, getItems());

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

        if (lastCheckedPos != null) {
            tag.putInt("lastPosX", getLastCheckedPos().getX());
            tag.putInt("lastPosY", getLastCheckedPos().getY());
            tag.putInt("lastPosZ", getLastCheckedPos().getZ());
        }

        if (canBedrockBreak)
            tag.putBoolean("module_bedrock_break", true);
    }

    public void readNbt(ReadNbtArgs args) {
        NbtCompound tag = args.getNbt();
        if (tag.contains("craftingInv")) {
            NbtCompound invTag = tag.getCompound("craftingInv");
            if (getWorld() != null)
                InventoryUtil.readNbt(getWorld(), invTag, craftingInvItems);
        }

        if (tag.contains("Items")) {
            InventoryUtil.readNbt(args, getItems());
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
        if (tag.contains("lastPosX") && tag.contains("lastPosY") && tag.contains("lastPosZ")) {
            this.lastCheckedPos = new BlockPos(tag.getInt("lastPosX"), tag.getInt("lastPosY"), tag.getInt("lastPosZ"));
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
        if (FabricLoader.getInstance().isModLoaded("storagebox")) {
            return ItemUtil.toID(stack.getItem()).toString().equals("storagebox:storagebox");
        }

        return false;
    }

    public ItemStack getInventoryStack() {
        for (ItemStack stack : getItems()) {
            latestGotStack = stack;
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof BlockItem) return stack;
            // StorageBox
            if (isStorageBox(stack)) {
                ItemStack itemInBox = StorageBoxUtil.getStackInStorageBox(stack);
                if (itemInBox == null) continue;

                if (itemInBox.getItem() instanceof BlockItem) return itemInBox;
            }
            // ---- StorageBox
        }
        return ItemStack.EMPTY;
    }

    public boolean tryPlacing(BlockPos blockPos, Block block, ItemStack stack) {
        if (getWorld().setBlockState(blockPos, BlockStateUtil.getDefaultState(block))) {
            WorldUtil.playSound(getWorld(), null, blockPos, BlockStateUtil.getSoundGroup(BlockStateUtil.getDefaultState(block)).getPlaceSound(), SoundCategory.BLOCKS, 1F, 1F);
            if (isStorageBox(latestGotStack)) {
                if (StorageBoxUtil.hasStackInStorageBox(latestGotStack)) {
                    int countInBox = StorageBoxUtil.getAmountInStorageBox(latestGotStack);
                    countInBox--;
                    if (countInBox < 1) {
                        StorageBoxUtil.setStackInStorageBox(latestGotStack, ItemStack.EMPTY);
                    } else {
                        StorageBoxUtil.setAmountInStorageBox(latestGotStack, countInBox);
                    }

                    return true;
                }
            }
            latestGotStack.decrement(1);
            return true;
        }
        return false;

    }

    public boolean tryBreaking(BlockPos procPos) {
        return WorldUtil.breakBlock(getWorld(), procPos, true);
    }

    private int getScanStartY(FillerModule module) {
        return module.getRange(pos1, pos2).start;
    }

    /** Returns the final Y height of this module's check zone.
     *
     * @param module
     * @return The final y-level to be checked.
     */
    private int getScanEndY(FillerModule module) {
        return module.getRange(pos1, pos2).end;
    }

    public boolean tryFilling(Item item) {
        if (getWorld() == null || getWorld().isClient()) return false;
        if (pos1 == null || pos2 == null) return false;
        
        // Get item type
        FillerModule module = null;
        for (FillerModule fillerModule : Registry.getINSTANCE().getModules()) {
            if (item.equals(fillerModule)) module = fillerModule;
        }
        if (module == null) return false;
        if (lastFillerModule == null) {
            lastFillerModule = module;
        } else if (!module.equals(lastFillerModule)) {
            setLastCheckedPos(null);
            lastFillerModule = module;
        }

        // Out of blocks!
        ItemStack stack = getInventoryStack();
        if (stack.isEmpty() && module.requiresBlocks()) return false;

        BlockPos lastChecked = this.getLastCheckedPos();
        int x = (lastChecked != null) ? lastChecked.getX() : pos1.getX();
        int y = (lastChecked != null) ? lastChecked.getY() : getScanStartY(module);
        int z = (lastChecked != null) ? lastChecked.getZ() : pos1.getZ() - 1 /* see below */;

        int diffX = pos2.getX() - pos1.getX();
//        int diffY = pos2.getY() - pos1.getY();
        int diffZ = pos2.getZ() - pos1.getZ();

        // Max blocks checked
        // Limit it to the size of a flat plane of it
        // This is so we don't check the same block multiple times on smaller fills.
        // You can multiply by `diffY` if you want
        int maxChecks = Math.min(maxBlockChecks, diffX * diffZ);
        int i = 0;
        while (i <= maxChecks) {
            i++;
            // Wrap each coordinate back to the beginning when it goes out of bounds
            // We do this at the beginning of the loop because `lastChecked` stores
            // the *last* checked block, not the next block to check. To prevent
            // off-by-one at the beginning of the filling when `lastChecked` is null,
            // we sub one off `z` when we initialize it then. Gets added back here anyway.
            // Note that the following code makes the assumption each component
            // of pos1 is smaller than that of pos2
            z++;
            if (z > pos2.getZ()) {
                z = pos1.getZ();
                x++;

                if (x > pos2.getX()) {
                    x = pos1.getX();
                    y++;

                    if (y > getScanEndY(module)) {
                        y = getScanStartY(module);
                    }
                }
            }

            BlockPos procPos = new BlockPos(x, y, z);
            this.setLastCheckedPos(procPos);
            Block procBlock = getWorld().getBlockState(procPos).getBlock();

            boolean isThis = getWorld().getBlockEntity(procPos) instanceof FillerTile || getWorld().getBlockEntity(procPos) == this;
            if (!isThis) {
                FillerProcessEvent event = new FillerProcessEvent(this, procPos, procBlock);
                FillerModuleReturn returnEvent = module.onProcess(event);

                if (returnEvent.equals(FillerModuleReturn.RETURN_FALSE)) return false;
                if (returnEvent.equals(FillerModuleReturn.RETURN_TRUE)) return true;
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
    private BlockPos lastCheckedPos = null;
    private FillerModule lastFillerModule = null;

    public BlockPos getPos1() {
        return pos1;
    }

    public BlockPos getPos2() {
        return pos2;
    }

    public BlockPos getLastCheckedPos() { return lastCheckedPos; }

    public void setPos1(BlockPos pos1) {
        this.pos1 = pos1;
    }

    public void setPos2(BlockPos pos2) {
        this.pos2 = pos2;
    }

    public void setLastCheckedPos(BlockPos lastPos) { this.lastCheckedPos = lastPos; }


    public FillerTile(BlockEntityType<?> type, TileCreateEvent event) {
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
