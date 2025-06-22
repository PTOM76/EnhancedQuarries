package net.pitan76.enhancedquarries.tile.base;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.pitan76.enhancedquarries.EnhancedQuarries;
import net.pitan76.enhancedquarries.Items;
import net.pitan76.enhancedquarries.block.base.Scanner;
import net.pitan76.enhancedquarries.screen.ScannerScreenHandler;
import net.pitan76.enhancedquarries.util.BlueprintUtil;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.event.container.factory.DisplayNameArgs;
import net.pitan76.mcpitanlib.api.event.nbt.ReadNbtArgs;
import net.pitan76.mcpitanlib.api.event.nbt.WriteNbtArgs;
import net.pitan76.mcpitanlib.api.event.tile.TileTickEvent;
import net.pitan76.mcpitanlib.api.gui.args.CreateMenuEvent;
import net.pitan76.mcpitanlib.api.gui.inventory.IInventory;
import net.pitan76.mcpitanlib.api.gui.v2.SimpleScreenHandlerFactory;
import net.pitan76.mcpitanlib.api.util.*;
import net.pitan76.mcpitanlib.api.util.collection.ItemStackList;
import net.pitan76.mcpitanlib.api.util.math.PosUtil;
import net.pitan76.mcpitanlib.api.util.nbt.v2.NbtRWUtil;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class ScannerTile extends BaseEnergyTile implements IInventory, SimpleScreenHandlerFactory {

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
            NbtRWUtil.putInt(args, "rangePos1X", PosUtil.x(pos1));
            NbtRWUtil.putInt(args, "rangePos1Y", PosUtil.y(pos1));
            NbtRWUtil.putInt(args, "rangePos1Z", PosUtil.z(pos1));
        }
        if (pos2 != null) {
            NbtRWUtil.putInt(args, "rangePos2X", PosUtil.x(pos2));
            NbtRWUtil.putInt(args, "rangePos2Y", PosUtil.y(pos2));
            NbtRWUtil.putInt(args, "rangePos2Z", PosUtil.z(pos2));
        }
    }

    public void readNbt(ReadNbtArgs args) {
        super.readNbt(args);
        NbtRWUtil.getInv(args, getItems());
        coolTime = NbtRWUtil.getDoubleOrDefault(args, "coolTime", getSettingCoolTime());

        int pos1x = NbtRWUtil.getIntOrDefault(args, "rangePos1X", 0);
        int pos1y = NbtRWUtil.getIntOrDefault(args, "rangePos1Y", 0);
        int pos1z = NbtRWUtil.getIntOrDefault(args, "rangePos1Z", 0);

        int pos2x = NbtRWUtil.getIntOrDefault(args, "rangePos2X", 0);
        int pos2y = NbtRWUtil.getIntOrDefault(args, "rangePos2Y", 0);
        int pos2z = NbtRWUtil.getIntOrDefault(args, "rangePos2Z", 0);

        setPos1(PosUtil.flooredBlockPos(pos1x, pos1y, pos1z));
        setPos2(PosUtil.flooredBlockPos(pos2x, pos2y, pos2z));
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
                    BlueprintUtil.writeNbt2(stack, blocks);
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
        if (callGetWorld() == null || WorldUtil.isClient(callGetWorld()) || pos1 == null || pos2 == null)
            return false;
        
        int procX;
        int procY;
        int procZ;
        for (procY = pos1.getY(); procY <= pos2.getY(); procY++) {
            for (procX = pos1.getX(); procX <= pos2.getX(); procX++) {
                for (procZ = pos1.getZ(); procZ <= pos2.getZ(); procZ++) {
                    BlockPos procPos = PosUtil.flooredBlockPos(procX, procY, procZ);
                    BlockState procState = WorldUtil.getBlockState(callGetWorld(), procPos);

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
}
