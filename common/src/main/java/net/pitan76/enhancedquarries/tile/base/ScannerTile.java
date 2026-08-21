package net.pitan76.enhancedquarries.tile.base;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.pitan76.enhancedquarries.EnhancedQuarries;
import net.pitan76.enhancedquarries.Items;
import net.pitan76.enhancedquarries.block.base.Scanner;
import net.pitan76.enhancedquarries.screen.ScannerScreenHandler;
import net.pitan76.enhancedquarries.util.BlueprintUtil;
import net.pitan76.enhancedquarries.util.TemplateUtil;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.event.container.factory.DisplayNameArgs;
import net.pitan76.mcpitanlib.api.event.nbt.ReadNbtArgs;
import net.pitan76.mcpitanlib.api.event.nbt.WriteNbtArgs;
import net.pitan76.mcpitanlib.api.event.tile.TileTickEvent;
import net.pitan76.mcpitanlib.api.gui.args.CreateMenuEvent;
import net.pitan76.mcpitanlib.api.gui.inventory.IInventory;
import net.pitan76.mcpitanlib.api.gui.v2.SimpleScreenHandlerFactory;
import net.pitan76.mcpitanlib.api.packet.UpdatePacketType;
import net.pitan76.mcpitanlib.api.registry.CompatRegistryLookup;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.mcpitanlib.api.util.collection.ItemStackList;
import net.pitan76.mcpitanlib.api.util.nbt.v2.NbtRWUtil;
import net.pitan76.mcpitanlib.midohra.block.BlockState;
import net.pitan76.mcpitanlib.midohra.block.MCBlocks;
import net.pitan76.mcpitanlib.midohra.item.ItemWrapper;
import net.pitan76.mcpitanlib.midohra.util.math.BlockPos;
import net.pitan76.mcpitanlib.midohra.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class ScannerTile extends BaseEnergyTile implements IInventory, SimpleScreenHandlerFactory, RangeTile {

    // Container
    public ItemStackList invItems = ItemStackList.ofSize(27, ItemStackUtil.empty());

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


    @Override
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

        // 範囲が保存されていないときに(0,0,0)にしてしまうと、範囲なしではなく原点を指してしまう
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

    // 範囲の枠線を描くために、クライアントへ更新パケットを飛ばす
    public void syncRangeToClient() {
        World world = getMidohraWorld();
        if (world.isNull() || world.isClient()) return;

        net.minecraft.block.BlockState state = world.getBlockState(getMidohraPos()).toMinecraft();
        WorldUtil.updateListeners(world.toMinecraft(), callGetPos(), state, state, 3);
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
        if (world == null || e.isClient()) return;

        // レッドストーン受信で無効
        if (world.isReceivingRedstonePower(pos)) {
            if (isActive())
                Scanner.setActive(false, world, pos);
            
            return;
        }
        if (getEnergy() > getEnergyCost()) {
            // ここに処理を記入
            if (canScan() && coolTime <= 0 && getItems().get(1).isEmpty()) {
                coolTime = getSettingCoolTime();
                Map<BlockPos, BlockState> blocks = new LinkedHashMap<>();
                if (tryScanning(blocks)) {
                    ItemStack stack = createScannedStack(blocks);
                    getItems().set(1, stack);
                    getItems().set(0, ItemStackUtil.empty());
                    useEnergy(getEnergyCost());
                    callMarkDirty();
                }

            }
            coolTimeBonus();
            coolTime = coolTime - getBasicSpeed();
            if (!isActive()) {
                Scanner.setActive(true, world, pos);
            }
        } else if (isActive()) {
            Scanner.setActive(false, world, pos);
        }
    }

    public boolean isTemplateInput() {
        ItemWrapper item = getItemsM().get(0).getItem();
        if (item.isEmpty()) return false;

        return item.equals(Items.EMPTY_TEMPLATE) || item.equals(Items.TEMPLATE);
    }

    public boolean canScan() {
        ItemWrapper item = getItemsM().get(0).getItem();
        if (item.isEmpty()) return false;

        return item.equals(Items.EMPTY_BLUEPRINT) || item.equals(Items.BLUEPRINT) || item.equals(Items.EMPTY_TEMPLATE) || item.equals(Items.TEMPLATE);
    }

    public ItemStack createScannedStack(Map<BlockPos, BlockState> blocks) {
        int count = getItems().getAsMidohra(0).getCount();

        if (isTemplateInput()) {
            ItemStack stack = ItemStackUtil.create(Items.TEMPLATE.get(), count);
            TemplateUtil.writeNbt(net.pitan76.mcpitanlib.midohra.item.ItemStack.of(stack), blocks.keySet());
            return stack;
        }

        ItemStack stack = ItemStackUtil.create(Items.BLUEPRINT.get(), count);
        BlueprintUtil.writeNbt(net.pitan76.mcpitanlib.midohra.item.ItemStack.of(stack), blocks);
        return stack;
    }

    // blocks...スキャナーを基準とした相対的な座標
    public boolean tryScanning(Map<BlockPos, BlockState> blocks) {
        EnhancedQuarries.logIfDev("scanning");

        World world = getMidohraWorld();

        if (world.toMinecraft() == null || world.isClient() || pos1 == null || pos2 == null)
            return false;
        
        int procX;
        int procY;
        int procZ;
        for (procY = pos1.getY(); procY <= pos2.getY(); procY++) {
            for (procX = pos1.getX(); procX <= pos2.getX(); procX++) {
                for (procZ = pos1.getZ(); procZ <= pos2.getZ(); procZ++) {
                    BlockPos procPos = BlockPos.of(procX, procY, procZ);
                    BlockState procState = world.getBlockState(procPos);

                    if (procState.getBlock() == MCBlocks.AIR) continue;

                    blocks.put(procPos.subtract(pos1), procState);
                }
            }
        }
        return true;
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

    public ScannerTile(BlockEntityType<?> type, TileCreateEvent e) {
        super(type, e);
    }
    
    @Override
    public ItemStackList getItems() {
        return invItems;
    }

    @Override
    public Text getDisplayName(DisplayNameArgs args) {
        return TextUtil.translatable("screen.enhanced_quarries.scanner.title");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(CreateMenuEvent e) {
        return new ScannerScreenHandler(e.syncId, e.playerInventory, this);
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
