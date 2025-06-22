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
import net.pitan76.mcpitanlib.api.util.nbt.v2.NbtRWUtil;
import net.pitan76.mcpitanlib.midohra.util.math.BlockPos;
import net.pitan76.mcpitanlib.midohra.world.World;
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
        WriteNbtArgs invArgs = NbtRWUtil.putWithCreate(args, "craftingInv");

        if (callGetWorld() != null) {
            if (!args.hasRegistryLookup())
                args.registryLookup = RegistryLookupUtil.getRegistryLookup(callGetWorld());

            NbtRWUtil.putInv(invArgs, craftingInvItems);
        }

        NbtRWUtil.putInv(args, getItems());
        NbtRWUtil.putDouble(args, "coolTime", coolTime);
        if (pos1 != null) {
            NbtRWUtil.putInt(args, "rangePos1X", pos1.getX());
            NbtRWUtil.putInt(args, "rangePos1Y", pos1.getY());
            NbtRWUtil.putInt(args, "rangePos1Z", pos1.getZ());
        }
        if (pos2 != null) {
            NbtRWUtil.putInt(args, "rangePos2X", pos2.getX());
            NbtRWUtil.putInt(args, "rangePos2Y", pos2.getY());
            NbtRWUtil.putInt(args, "rangePos2Z", pos2.getZ());
        }

        if (lastCheckedPos != null) {
            NbtRWUtil.putInt(args, "lastPosX", getLastCheckedPos().getX());
            NbtRWUtil.putInt(args, "lastPosY", getLastCheckedPos().getY());
            NbtRWUtil.putInt(args, "lastPosZ", getLastCheckedPos().getZ());
        }

        if (canBedrockBreak)
            NbtRWUtil.putBoolean(args, "module_bedrock_break", true);
    }

    public void readNbt(ReadNbtArgs args) {
        super.readNbt(args);
        NbtCompound nbt = args.getNbt();

        if (callGetWorld() != null) {
            if (!args.hasRegistryLookup())
                args.registryLookup = RegistryLookupUtil.getRegistryLookup(callGetWorld());

            ReadNbtArgs invArgs = NbtRWUtil.get(args, "craftingInv");
            NbtRWUtil.getInv(invArgs, craftingInvItems);
        }

        NbtRWUtil.getInv(args, getItems());

        coolTime = NbtRWUtil.getDoubleOrDefault(args, "coolTime", getSettingCoolTime());

        int pos1x = NbtRWUtil.getIntOrDefault(args, "rangePos1X", 0);
        int pos1y = NbtRWUtil.getIntOrDefault(args, "rangePos1Y", 0);
        int pos1z = NbtRWUtil.getIntOrDefault(args, "rangePos1Z", 0);

        int pos2x = NbtRWUtil.getIntOrDefault(args, "rangePos2X", 0);
        int pos2y = NbtRWUtil.getIntOrDefault(args, "rangePos2Y", 0);
        int pos2z = NbtRWUtil.getIntOrDefault(args, "rangePos2Z", 0);

        setPos1(PosUtil.flooredMidohraBlockPos(pos1x, pos1y, pos1z));
        setPos2(PosUtil.flooredMidohraBlockPos(pos2x, pos2y, pos2z));

        int lastCheckedX = NbtRWUtil.getIntOrDefault(args, "lastPosX", 0);
        int lastCheckedY = NbtRWUtil.getIntOrDefault(args, "lastPosY", 0);
        int lastCheckedZ = NbtRWUtil.getIntOrDefault(args, "lastPosZ", 0);
        this.lastCheckedPos = PosUtil.flooredMidohraBlockPos(lastCheckedX, lastCheckedY, lastCheckedZ);

        canBedrockBreak = NbtRWUtil.getBooleanOrDefault(args, "module_bedrock_break", false);
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
        World world = e.getMidohraWorld();
        BlockPos pos = e.getMidohraPos();
        if (world.isClient()) return;

        // レッドストーン受信で無効
        if (world.isReceivingRedstonePower(pos)) {
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

    public net.pitan76.mcpitanlib.midohra.world.World getWorldM() {
        return net.pitan76.mcpitanlib.midohra.world.World.of(callGetWorld());
    }

    public boolean tryPlacing(BlockPos blockPos, Block block, ItemStack stack) {
        net.pitan76.mcpitanlib.midohra.world.World world = getWorldM();

        if (world.setBlockState(blockPos, BlockStateUtil.getMidohraDefaultState(block))) {
            world.playSound(blockPos, BlockStateUtil.getCompatSoundGroup(BlockStateUtil.getDefaultState(block)).getPlaceSound(), CompatSoundCategory.BLOCKS, 1F, 1F);
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
        World world = getWorldM();
        return world.breakBlock(procPos, true);
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
        World world = getWorldM();
        if (world.getRaw() == null || world.isClient()) return false;
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

            BlockPos procPos = PosUtil.flooredMidohraBlockPos(x, y, z);
            this.setLastCheckedPos(procPos);
            Block procBlock = world.getBlockState(procPos).getBlock().get();

            BlockEntity blockEntity = world.getBlockEntity(procPos.toRaw());
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
