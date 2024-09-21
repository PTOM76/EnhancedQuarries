package net.pitan76.enhancedquarries.tile.base;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.pitan76.enhancedquarries.EnhancedQuarries;
import net.pitan76.enhancedquarries.Items;
import net.pitan76.enhancedquarries.block.base.Scanner;
import net.pitan76.enhancedquarries.screen.ScannerScreenHandler;
import net.pitan76.enhancedquarries.util.BlueprintUtil;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.event.nbt.ReadNbtArgs;
import net.pitan76.mcpitanlib.api.event.nbt.WriteNbtArgs;
import net.pitan76.mcpitanlib.api.event.tile.TileTickEvent;
import net.pitan76.mcpitanlib.api.gui.inventory.IInventory;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class ScannerTile extends BaseEnergyTile implements IInventory, NamedScreenHandlerFactory {

    // Container
    public DefaultedList<ItemStack> invItems = DefaultedList.ofSize(27, ItemStackUtil.empty());

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

    public void tick(TileTickEvent<BaseEnergyTile> e) {
        super.tick(e);
        World world = e.world;
        BlockPos pos = e.pos;
        if (world == null || WorldUtil.isClient(world)) return;

        // レッドストーン受信で無効
        if (WorldUtil.isReceivingRedstonePower(world, pos)) {
            if (isActive())
                Scanner.setActive(false, world, pos);
            
            return;
        }
        if (getEnergy() > getEnergyCost()) {
            // ここに処理を記入
            if ((
                    getItems().get(0).getItem().equals(Items.EMPTY_BLUEPRINT)
                            || getItems().get(0).getItem().equals(Items.BLUEPRINT)
                    ) && coolTime <= 0 && getItems().get(1).isEmpty()) {
                coolTime = getSettingCoolTime();
                Map<BlockPos, BlockState> blocks = new LinkedHashMap<>();
                if (tryScanning(blocks)) {
                    ItemStack stack = ItemStackUtil.create(Items.BLUEPRINT, getItems().get(0).getCount());
                    BlueprintUtil.writeNbt(stack, blocks);
                    getItems().set(1, stack);
                    getItems().set(0, ItemStackUtil.empty());
                    useEnergy(getEnergyCost());
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

    // blocks...スキャナーを基準とした相対的な座標
    public boolean tryScanning(Map<BlockPos, BlockState> blocks) {
        EnhancedQuarries.logIfDev("scanning");
        if (getWorld() == null || getWorld().isClient() || pos1 == null || pos2 == null)
            return false;
        
        int procX;
        int procY;
        int procZ;
        for (procY = pos1.getY(); procY <= pos2.getY(); procY++) {
            for (procX = pos1.getX(); procX <= pos2.getX(); procX++) {
                for (procZ = pos1.getZ(); procZ <= pos2.getZ(); procZ++) {
                    BlockPos procPos = PosUtil.flooredBlockPos(procX, procY, procZ);
                    BlockState procState = getWorld().getBlockState(procPos);

                    if (procState.getBlock() == Blocks.AIR) continue;

                    blocks.put(PosUtil.flooredBlockPos(procX - pos1.getX(), procY - pos1.getY(), procZ - pos1.getZ()), procState);
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

    public ScannerTile(BlockEntityType<?> type, TileCreateEvent event) {
        super(type, event);
    }

    public void init() {

    }
    
    @Override
    public DefaultedList<ItemStack> getItems() {
        return invItems;
    }

    @Override
    public Text getDisplayName() {
        return TextUtil.translatable("screen.enhanced_quarries.scanner.title");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ScannerScreenHandler(syncId, playerInventory, this);
    }
}
