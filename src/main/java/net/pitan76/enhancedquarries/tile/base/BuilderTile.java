package net.pitan76.enhancedquarries.tile.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import net.pitan76.enhancedquarries.Items;
import net.pitan76.enhancedquarries.block.base.Builder;
import net.pitan76.enhancedquarries.inventory.DisabledInventory;
import net.pitan76.enhancedquarries.screen.BuilderScreenHandler;
import net.pitan76.enhancedquarries.util.BlueprintUtil;
import net.pitan76.mcpitanlib.api.event.block.BlockPlacedEvent;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.event.nbt.ReadNbtArgs;
import net.pitan76.mcpitanlib.api.event.nbt.WriteNbtArgs;
import net.pitan76.mcpitanlib.api.event.tile.TileTickEvent;
import net.pitan76.mcpitanlib.api.gui.inventory.IInventory;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.mcpitanlib.api.util.event.BlockEventGenerator;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;
import net.pitan76.storagebox.api.StorageBoxUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BuilderTile extends BaseEnergyTile implements IInventory, SidedInventory, NamedScreenHandlerFactory {

    // Container
    public DefaultedList<ItemStack> invItems = DefaultedList.ofSize(28, ItemStackUtil.empty());

    public IInventory inventory = this;

    public Inventory needInventory = new DisabledInventory(27);

    // ブロック1回設置分に対するエネルギーのコスト
    public long getEnergyCost() {
        return 30;
    }

    // エネルギーの容量
    public long getMaxEnergy() {
        return 5000;
    }

    // エネルギーの最大出力(不要なので0)
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
    }

    public void readNbt(ReadNbtArgs args) {
        NbtCompound nbt = args.getNbt();

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

    public Map<BlockPos, BlockState> blueprintMap = new LinkedHashMap<>();

    @Override
    public void tick(TileTickEvent<BaseEnergyTile> e) {
        super.tick(e);
        World world = e.world;
        BlockPos pos = e.pos;
        if (WorldUtil.isClient(world)) return;

        // レッドストーン受信で無効
        if (WorldUtil.isReceivingRedstonePower(world, pos)) {
            if (isActive())
                Builder.setActive(false, world, pos);
            return;
        }

        ItemStack blueprint = inventory.getStack(0);

        if (CustomDataUtil.hasNbt(blueprint) && blueprint.getItem() == Items.BLUEPRINT) {
            if (blueprintMap.isEmpty()) {
                blueprintMap = BlueprintUtil.readNBt(blueprint, getFacing());
                pos1 = pos.add(BlueprintUtil.getMinPos(blueprintMap));
                pos2 = pos.add(BlueprintUtil.getMaxPos(blueprintMap));

                // 必要なアイテム数
                List<ItemStack> needStacks = new ArrayList<>();
                for (BlockState blockState : blueprintMap.values()) {
                    if (blockState.isAir()) continue;
                    Item item = blockState.getBlock().asItem();

                    boolean b = false;
                    for (ItemStack stack : needStacks) {
                        if (stack.getItem() != item) continue;
                        stack.increment(1);
                        b = true;
                    }
                    if (b) continue;
                    needStacks.add(ItemStackUtil.create(item, 1));
                }
                int i = 0;
                for (ItemStack stack : needStacks) {
                    if (stack.isEmpty()) continue;
                    needInventory.setStack(i, stack);
                    i++;
                    if (i == needInventory.size()) break;
                }
            }
        } else {
            pos1 = pos2 = null;
            blueprintMap = new LinkedHashMap<>();
            for (int i = 0; i < needInventory.size(); i++) {
                needInventory.setStack(i, ItemStackUtil.empty());
            }
            return;
        }
        if (inventory.isEmpty()) return;

        if (blueprintMap.isEmpty()) return;
        if (getEnergy() > getEnergyCost()) {
            // ここに処理を記入
            if (coolTime <= 0) {
                coolTime = getSettingCoolTime();
                if (tryBuilding()) {
                    useEnergy(getEnergyCost());
                }
            }
            coolTimeBonus();
            coolTime = coolTime - getBasicSpeed();
            if (!isActive()) {
                Builder.setActive(true, world, pos);
            }
        } else if (isActive()) {
            Builder.setActive(false, world, pos);
        }
    }

    public ItemStack latestGotStack = ItemStackUtil.empty();

    public static boolean isStorageBox(ItemStack stack) {
        return ItemUtil.toID(stack.getItem()).toString().equals("storagebox:storagebox");
    }

    public ItemStack getInventoryStack(Block block) {
        for (ItemStack stack : getItems()) {
            if (stack.isEmpty()) continue;

            latestGotStack = stack;
            if (stack.getItem() instanceof BlockItem && stack.getItem() == block.asItem()) return stack;
            // StorageBox
            if (isStorageBox(stack)) {
                ItemStack itemInBox = StorageBoxUtil.getStackInStorageBox(stack);
                if (itemInBox == null) continue;

                if (itemInBox.getItem() instanceof BlockItem && itemInBox.getItem() == block.asItem()) return itemInBox;
            }
            // ---- StorageBox
        }
        return ItemStackUtil.empty();
    }

    public boolean tryPlacing(BlockPos blockPos, BlockState state) {
        if (getInventoryStack(state.getBlock()).isEmpty()) return false;

        if (getWorld().setBlockState(blockPos, state)) {
            BlockEventGenerator.onPlaced(state.getBlock(), new BlockPlacedEvent(getWorld(), blockPos, state, null, latestGotStack));
            WorldUtil.playSound(getWorld(), null, blockPos, BlockStateUtil.getSoundGroup(state).getPlaceSound(), SoundCategory.BLOCKS, 1F, 1F);
            if (isStorageBox(latestGotStack)) {
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
            latestGotStack.decrement(1);
            return true;
        }
        return false;

    }

    public boolean tryBuilding() {
        if (getWorld() == null || getWorld().isClient()) return false;
        if (pos1 == null || pos2 == null)
            return false;
        int procX;
        int procY;
        int procZ;
        for (procY = pos1.getY(); procY <= pos2.getY(); procY++) {
            for (procX = pos1.getX(); procX <= pos2.getX(); procX++) {
                for (procZ = pos1.getZ(); procZ <= pos2.getZ(); procZ++) {
                    BlockPos procPos = PosUtil.flooredBlockPos(procX, procY, procZ);
                    Block procBlock = getWorld().getBlockState(procPos).getBlock();

                    BlockState buildingState = blueprintMap.get(procPos.add(-pos.getX(), -pos.getY(), -pos.getZ()));
                    if (buildingState == null) continue;
                    if (buildingState.getBlock() == Blocks.AIR || procBlock == buildingState.getBlock()) continue;
                    if (procBlock != Blocks.AIR) continue;

                    if (procBlock.asItem() == Items.NORMAL_BUILDER) {
                        continue;
                    }
                    if (tryPlacing(procPos, buildingState)) {
                        return true;
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
        return false;
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

    public BuilderTile(BlockEntityType<?> type, TileCreateEvent event) {
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
        return TextUtil.translatable("screen.enhanced_quarries.builder.title");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new BuilderScreenHandler(syncId, playerInventory, inventory, needInventory);
    }
}
