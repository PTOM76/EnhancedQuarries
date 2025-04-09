package net.pitan76.enhancedquarries.tile.base;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.pitan76.enhancedquarries.block.base.Filler;
import net.pitan76.enhancedquarries.event.FillerModuleReturn;
import net.pitan76.enhancedquarries.event.FillerProcessEvent;
import net.pitan76.enhancedquarries.item.base.FillerModule;
import net.pitan76.enhancedquarries.registry.ModuleRegistry;
import net.pitan76.enhancedquarries.screen.FillerScreenHandler;
import net.pitan76.enhancedquarries.util.EQStorageBoxUtil;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.event.container.factory.DisplayNameArgs;
import net.pitan76.mcpitanlib.api.event.nbt.ReadNbtArgs;
import net.pitan76.mcpitanlib.api.event.nbt.WriteNbtArgs;
import net.pitan76.mcpitanlib.api.event.tile.TileTickEvent;
import net.pitan76.mcpitanlib.api.gui.args.CreateMenuEvent;
import net.pitan76.mcpitanlib.api.gui.inventory.IInventory;
import net.pitan76.mcpitanlib.api.gui.inventory.sided.ChestStyleSidedInventory;
import net.pitan76.mcpitanlib.api.gui.inventory.sided.args.AvailableSlotsArgs;
import net.pitan76.mcpitanlib.api.gui.v2.SimpleScreenHandlerFactory;
import net.pitan76.mcpitanlib.api.sound.CompatSoundCategory;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.mcpitanlib.api.util.collection.ItemStackList;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;
import net.pitan76.storagebox.api.StorageBoxUtil;
import org.jetbrains.annotations.Nullable;

public class FillerTile extends BaseEnergyTile implements IInventory, ChestStyleSidedInventory, SimpleScreenHandlerFactory {

    // Container
    public ItemStackList invItems = ItemStackList.ofSize(27, ItemStackUtil.empty());
    public ItemStackList craftingInvItems = ItemStackList.ofSize(10, ItemStackUtil.empty());

    public IInventory craftingInventory = () -> craftingInvItems;
    public IInventory inventory = this;

    public Inventory getCraftingInventory() {
        return craftingInventory;
    }

    public boolean hasModule() {
        return !getModule().isEmpty();
    }

    public ItemStack getModule() {
        ItemStack stack = InventoryUtil.getStack(getCraftingInventory(), 9);
        return ItemStackUtil.copy(stack);
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
        super.writeNbt(args);
        NbtCompound nbt = args.getNbt();
        NbtCompound invNbt = NbtUtil.create();

        if (callGetWorld() != null) {
            if (!args.hasRegistryLookup())
                args.registryLookup = RegistryLookupUtil.getRegistryLookup(callGetWorld());

            InventoryUtil.writeNbt(args.registryLookup, invNbt, craftingInvItems);
        }
        NbtUtil.put(nbt, "craftingInv", invNbt);

        InventoryUtil.writeNbt(args, getItems());

        NbtUtil.putDouble(nbt, "coolTime", coolTime);
        if (pos1 != null) {
            NbtUtil.putInt(nbt, "rangePos1X", getPos1().getX());
            NbtUtil.putInt(nbt, "rangePos1Y", getPos1().getY());
            NbtUtil.putInt(nbt, "rangePos1Z", getPos1().getZ());
        }
        if (pos2 != null) {
            NbtUtil.putInt(nbt, "rangePos2X", getPos2().getX());
            NbtUtil.putInt(nbt, "rangePos2Y", getPos2().getY());
            NbtUtil.putInt(nbt, "rangePos2Z", getPos2().getZ());
        }

        if (lastCheckedPos != null) {
            NbtUtil.putInt(nbt, "lastPosX", getLastCheckedPos().getX());
            NbtUtil.putInt(nbt, "lastPosY", getLastCheckedPos().getY());
            NbtUtil.putInt(nbt, "lastPosZ", getLastCheckedPos().getZ());
        }

        if (canBedrockBreak)
            NbtUtil.putBoolean(nbt, "module_bedrock_break", true);
    }

    public void readNbt(ReadNbtArgs args) {
        super.readNbt(args);
        NbtCompound nbt = args.getNbt();
        if (NbtUtil.has(nbt, "craftingInv")) {
            NbtCompound invNbt = NbtUtil.get(nbt, "craftingInv");

            if (callGetWorld() != null) {
                if (!args.hasRegistryLookup())
                    args.registryLookup = RegistryLookupUtil.getRegistryLookup(callGetWorld());

                InventoryUtil.readNbt(args.registryLookup, invNbt, craftingInvItems);
            }
        }

        if (NbtUtil.has(nbt, "Items")) {
            InventoryUtil.readNbt(args, getItems());
        }

        if (NbtUtil.has(nbt, "coolTime")) coolTime = NbtUtil.getDouble(nbt, "coolTime");
        if (NbtUtil.has(nbt, "rangePos1X")
                && NbtUtil.has(nbt, "rangePos1Y")
                && NbtUtil.has(nbt, "rangePos1Z")
                && NbtUtil.has(nbt, "rangePos2X")
                && NbtUtil.has(nbt, "rangePos2Y")
                && NbtUtil.has(nbt, "rangePos2Z")) {
            setPos1(PosUtil.flooredBlockPos(NbtUtil.getInt(nbt, "rangePos1X"), NbtUtil.getInt(nbt, "rangePos1Y"), NbtUtil.getInt(nbt, "rangePos1Z")));
            setPos2(PosUtil.flooredBlockPos(NbtUtil.getInt(nbt, "rangePos2X"), NbtUtil.getInt(nbt, "rangePos2Y"), NbtUtil.getInt(nbt, "rangePos2Z")));
        }
        if (NbtUtil.has(nbt, "lastPosX") && NbtUtil.has(nbt, "lastPosY") && NbtUtil.has(nbt, "lastPosZ")) {
            this.lastCheckedPos = PosUtil.flooredBlockPos(NbtUtil.getInt(nbt, "lastPosX"), NbtUtil.getInt(nbt, "lastPosY"), NbtUtil.getInt(nbt, "lastPosZ"));
        }

        if (NbtUtil.has(nbt, "module_bedrock_break"))
            canBedrockBreak = NbtUtil.getBoolean(nbt, "module_bedrock_break");
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

    @Override
    public void tick(TileTickEvent<BaseEnergyTile> e) {
        super.tick(e);
        World world = e.world;
        BlockPos pos = e.pos;
        if (WorldUtil.isClient(world)) return;

        // レッドストーン受信で無効
        if (WorldUtil.isReceivingRedstonePower(world, pos)) {
            if (isActive())
                Filler.setActive(false, world, pos);
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
                Filler.setActive(true, world, pos);

        } else if (isActive()) {
            Filler.setActive(false, world, pos);
        }
    }

    public ItemStack latestGotStack = ItemStackUtil.empty();

    public ItemStack getInventoryStack() {
        for (ItemStack stack : getItems()) {
            latestGotStack = stack;
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof BlockItem) return stack;
            // StorageBox
            if (EQStorageBoxUtil.isStorageBox(stack)) {
                ItemStack itemInBox = StorageBoxUtil.getStackInStorageBox(stack);
                if (itemInBox == null) continue;

                if (itemInBox.getItem() instanceof BlockItem) return itemInBox;
            }
            // ---- StorageBox
        }
        return ItemStackUtil.empty();
    }

    public boolean tryPlacing(BlockPos blockPos, Block block, ItemStack stack) {
        if (WorldUtil.setBlockState(callGetWorld(), blockPos, BlockStateUtil.getDefaultState(block))) {
            WorldUtil.playSound(callGetWorld(), null, blockPos, BlockStateUtil.getCompatSoundGroup(BlockStateUtil.getDefaultState(block)).getPlaceSound(), CompatSoundCategory.BLOCKS, 1F, 1F);
            if (EQStorageBoxUtil.isStorageBox(latestGotStack)) {
                if (StorageBoxUtil.hasStackInStorageBox(latestGotStack)) {
                    int countInBox = StorageBoxUtil.getAmountInStorageBox(latestGotStack);
                    countInBox--;
                    if (countInBox < 1) {
                        StorageBoxUtil.setStackInStorageBox(latestGotStack, ItemStackUtil.empty());
                    } else {
                        StorageBoxUtil.setAmountInStorageBox(latestGotStack, countInBox);
                    }

                    return true;
                }
            }
            ItemStackUtil.decrementCount(latestGotStack, 1);
            return true;
        }
        return false;

    }

    public boolean tryBreaking(BlockPos procPos) {
        return WorldUtil.breakBlock(callGetWorld(), procPos, true);
    }

    private int getScanStartY(FillerModule module) {
        return module.getRange(pos1, pos2).start;
    }

    /** Returns the final Y height of this module's check zone.
     *
     * @param module The module to check.
     * @return The final y-level to be checked.
     */
    private int getScanEndY(FillerModule module) {
        return module.getRange(pos1, pos2).end;
    }

    public boolean tryFilling(Item item) {
        if (callGetWorld() == null || WorldUtil.isClient(callGetWorld())) return false;
        if (pos1 == null || pos2 == null) return false;
        
        // Get item type
        FillerModule module = null;
        for (FillerModule fillerModule : ModuleRegistry.getINSTANCE().getModules()) {
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

            BlockPos procPos = PosUtil.flooredBlockPos(x, y, z);
            this.setLastCheckedPos(procPos);
            Block procBlock = WorldUtil.getBlockState(callGetWorld(), procPos).getBlock();

            BlockEntity blockEntity = WorldUtil.getBlockEntity(callGetWorld(), procPos);
            boolean isThis = blockEntity instanceof FillerTile || blockEntity == this;
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
    public Text getDisplayName(DisplayNameArgs args) {
        return TextUtil.translatable("screen.enhanced_quarries.filler.title");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(CreateMenuEvent e) {
        return new FillerScreenHandler(e.syncId, e.playerInventory, this, getCraftingInventory());
    }
}
