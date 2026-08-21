package net.pitan76.enhancedquarries.tile.base;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.pitan76.enhancedquarries.Items;
import net.pitan76.enhancedquarries.block.base.Builder;
import net.pitan76.enhancedquarries.inventory.DisabledInventory;
import net.pitan76.enhancedquarries.screen.BuilderScreenHandler;
import net.pitan76.enhancedquarries.util.BlueprintUtil;
import net.pitan76.mcpitanlib.api.event.block.BlockPlacedEvent;
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
import net.pitan76.mcpitanlib.api.packet.UpdatePacketType;
import net.pitan76.mcpitanlib.api.registry.CompatRegistryLookup;
import net.pitan76.mcpitanlib.api.sound.CompatSoundCategory;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.mcpitanlib.api.util.collection.ItemStackList;
import net.pitan76.mcpitanlib.api.util.event.BlockEventGenerator;
import net.pitan76.mcpitanlib.api.util.item.ItemUtil;
import net.pitan76.mcpitanlib.api.util.nbt.v2.NbtRWUtil;
import net.pitan76.mcpitanlib.midohra.block.BlockState;
import net.pitan76.mcpitanlib.midohra.block.BlockWrapper;
import net.pitan76.mcpitanlib.midohra.block.MCBlocks;
import net.pitan76.mcpitanlib.midohra.item.ItemStack;
import net.pitan76.mcpitanlib.midohra.item.ItemWrapper;
import net.pitan76.mcpitanlib.midohra.util.math.BlockPos;
import net.pitan76.mcpitanlib.midohra.world.World;
import net.pitan76.enhancedquarries.platform.StorageBoxHooks;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BuilderTile extends BaseEnergyTile implements IInventory, ChestStyleSidedInventory, SimpleScreenHandlerFactory {

    // Container
    public ItemStackList invItems = ItemStackList.ofSize(28, ItemStackUtil.empty());

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
        super.writeNbt(args);
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
    }

    public void readNbt(ReadNbtArgs args) {
        super.readNbt(args);
        NbtRWUtil.getInv(args, getItems());
        coolTime = NbtRWUtil.getDoubleOrDefault(args, "coolTime", getSettingCoolTime());

        // 範囲が未設定のときにnullのままにしないと、レンダラーが原点に枠を描いてしまう
        setPos1(readRangePos(args, "rangePos1"));
        setPos2(readRangePos(args, "rangePos2"));
    }

    private BlockPos readRangePos(ReadNbtArgs args, String key) {
        if (!NbtUtil.has(args.getNbt(), key + "X")) return null;

        return BlockPos.of(NbtRWUtil.getIntOrDefault(args, key + "X", 0),
                NbtRWUtil.getIntOrDefault(args, key + "Y", 0),
                NbtRWUtil.getIntOrDefault(args, key + "Z", 0));
    }

    @Override
    public UpdatePacketType getUpdatePacketType() {
        return UpdatePacketType.BLOCK_ENTITY_UPDATE_S2C;
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(CompatRegistryLookup registryLookup) {
        NbtCompound nbt = NbtUtil.create();
        writeNbt(new WriteNbtArgs(nbt, registryLookup));
        return nbt;
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

    public Map<net.pitan76.mcpitanlib.midohra.util.math.BlockPos, net.pitan76.mcpitanlib.midohra.block.BlockState> blueprintMap = new LinkedHashMap<>();

    @Override
    public void tick(TileTickEvent<BaseEnergyTile> e) {
        super.tick(e);
        World world = e.getMidohraWorld();
        BlockPos pos = e.getMidohraPos();
        if (e.isClient()) return;

        // レッドストーン受信で無効
        if (world.isReceivingRedstonePower(pos)) {
            if (isActive())
                Builder.setActive(false, world, pos);
            return;
        }

        ItemStack blueprint = ItemStack.of(InventoryUtil.getStack(inventory, 0));

        if (blueprint.hasCustomNbt() && blueprint.getRawItem() == Items.BLUEPRINT) {
            if (blueprintMap.isEmpty()) {
                blueprintMap = BlueprintUtil.readNbt(blueprint, getFacing());

                BlockPos min = BlueprintUtil.getMinPos(blueprintMap);
                BlockPos max = BlueprintUtil.getMaxPos(blueprintMap);
                BlockPos origin = getBuildOrigin(pos, min, max);

                pos1 = origin.add(min);
                pos2 = origin.add(max);
                syncRangeToClient();

                // 必要なアイテム数
                List<ItemStack> needStacks = new ArrayList<>();
                for (net.pitan76.mcpitanlib.midohra.block.BlockState blockState : blueprintMap.values()) {
                    if (blockState.isAir()) continue;
                    ItemWrapper item = blockState.getBlock().asItem();

                    boolean b = false;
                    for (ItemStack stack : needStacks) {
                        if (stack.getItem() != item) continue;
                        stack.increment(1);
                        b = true;
                    }
                    if (b) continue;
                    needStacks.add(item.createStack(1));
                }
                int i = 0;
                for (ItemStack stack : needStacks) {
                    if (stack.isEmpty()) continue;
                    InventoryUtil.setStack(needInventory, i, stack.toMinecraft());
                    i++;
                    if (i == InventoryUtil.getSize(needInventory)) break;
                }
            }
        } else {
            if (pos1 != null || pos2 != null) {
                pos1 = pos2 = null;
                syncRangeToClient();
            }
            blueprintMap = new LinkedHashMap<>();
            for (int i = 0; i < InventoryUtil.getSize(needInventory); i++) {
                InventoryUtil.setStack(needInventory, i, ItemStackUtil.empty());
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

    // 範囲の枠線を描くために、クライアントへ更新パケットを飛ばす
    public void syncRangeToClient() {
        World world = getMidohraWorld();
        if (world.isNull() || world.isClient()) return;

        net.minecraft.block.BlockState state = world.getBlockState(getMidohraPos()).toMinecraft();
        WorldUtil.updateListeners(world.toMinecraft(), callGetPos(), state, state, 3);
    }

    // 構造物がビルダー自身に重ならないよう正面側に寄せた原点
    public BlockPos getBuildOrigin(BlockPos pos, BlockPos min, BlockPos max) {
        net.pitan76.mcpitanlib.midohra.util.math.Direction front = net.pitan76.mcpitanlib.midohra.util.math.Direction.of(getFacing()).getOpposite();

        int nearMin = min.getX() * front.getOffsetX() + min.getZ() * front.getOffsetZ();
        int nearMax = max.getX() * front.getOffsetX() + max.getZ() * front.getOffsetZ();

        return pos.offset(front, 1 - Math.min(nearMin, nearMax));
    }

    public ItemStack latestGotStack = ItemStack.EMPTY;

    public static boolean isStorageBox(ItemStack stack) {
        return ItemUtil.toIdAsString(stack.getRawItem()).equals("storagebox:storagebox");
    }

    public ItemStack getInventoryStack(BlockWrapper block) {
        for (ItemStack stack : getItems().toMidohra()) {
            if (stack.isEmpty()) continue;

            latestGotStack = stack;
            if (stack.isBlockItem() && stack.getItem().equals(block.asItem())) return stack;
            // StorageBox
            if (isStorageBox(stack)) {
                ItemStack itemInBox = ItemStack.of(StorageBoxHooks.getStackInStorageBox(stack.toMinecraft()));
                if (itemInBox == null) continue;

                if (itemInBox.isBlockItem() && itemInBox.getItem().equals(block.asItem())) return itemInBox;
            }
            // ---- StorageBox
        }
        return ItemStack.EMPTY;
    }

    public boolean tryPlacing(BlockPos blockPos, BlockState state) {
        if (getInventoryStack(state.getBlock()).isEmpty()) return false;

        World world = getMidohraWorld();

        if (world.setBlockState(blockPos, state)) {
            BlockEventGenerator.onPlaced(state.getBlock().get(), new BlockPlacedEvent(callGetWorld(), blockPos.toMinecraft(), state.toMinecraft(), null, latestGotStack.toMinecraft()));
            world.playSound(null, blockPos, state.getSoundGroup().getPlaceSound(), CompatSoundCategory.BLOCKS, 1F, 1F);
            if (isStorageBox(latestGotStack)) {
                if (StorageBoxHooks.hasStackInStorageBox(latestGotStack.toMinecraft())) {
                    int countInBox = StorageBoxHooks.getAmountInStorageBox(latestGotStack.toMinecraft());
                    countInBox--;
                    if (countInBox < 1) {
                        StorageBoxHooks.setStackInStorageBox(latestGotStack.toMinecraft(), ItemStackUtil.empty());
                    } else {
                        StorageBoxHooks.setAmountInStorageBox(latestGotStack.toMinecraft(), countInBox);
                    }

                    markDirty();
                    return true;
                }
            }
            latestGotStack.decrement(1);
            markDirty();
            return true;
        }
        return false;

    }

    public boolean tryBuilding() {
        World world = getMidohraWorld();

        if (world.isNull() || world.isClient()) return false;
        if (pos1 == null || pos2 == null)
            return false;

        BlockPos origin = pos1.subtract(BlueprintUtil.getMinPos(blueprintMap));

        int procX;
        int procY;
        int procZ;
        for (procY = pos1.getY(); procY <= pos2.getY(); procY++) {
            for (procX = pos1.getX(); procX <= pos2.getX(); procX++) {
                for (procZ = pos1.getZ(); procZ <= pos2.getZ(); procZ++) {
                    BlockPos procPos = BlockPos.of(procX, procY, procZ);

                    if (procPos.equals(getMidohraPos())) continue;

                    BlockWrapper procBlock = world.getBlockState(procPos).getBlock();

                    BlockPos pos = procPos.subtract(origin);

                    net.pitan76.mcpitanlib.midohra.block.BlockState buildingState = blueprintMap.get(pos);
                    if (buildingState == null) continue;
                    if (buildingState.getBlock().equals(MCBlocks.AIR) || procBlock.equals(buildingState.getBlock())) continue;
                    if (!procBlock.equals(MCBlocks.AIR)) continue;

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

    public BuilderTile(BlockEntityType<?> type, TileCreateEvent e) {
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
    public Text getDisplayName(DisplayNameArgs args) {
        return TextUtil.translatable("screen.enhanced_quarries.builder.title");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(CreateMenuEvent e) {
        return new BuilderScreenHandler(e.syncId, e.playerInventory, inventory, needInventory);
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
