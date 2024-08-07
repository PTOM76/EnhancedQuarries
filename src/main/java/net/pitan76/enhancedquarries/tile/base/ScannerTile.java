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
import net.pitan76.enhancedquarries.Items;
import net.pitan76.enhancedquarries.block.base.Scanner;
import net.pitan76.enhancedquarries.screen.ScannerScreenHandler;
import net.pitan76.enhancedquarries.util.BlueprintUtil;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.event.nbt.ReadNbtArgs;
import net.pitan76.mcpitanlib.api.event.nbt.WriteNbtArgs;
import net.pitan76.mcpitanlib.api.gui.inventory.IInventory;
import net.pitan76.mcpitanlib.api.util.InventoryUtil;
import net.pitan76.mcpitanlib.api.util.TextUtil;
import net.pitan76.mcpitanlib.api.util.WorldUtil;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class ScannerTile extends BaseEnergyTile implements IInventory, NamedScreenHandlerFactory {

    // Container
    public DefaultedList<ItemStack> invItems = DefaultedList.ofSize(27, ItemStack.EMPTY);

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
        NbtCompound tag = args.getNbt();

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
    }

    public void readNbt(ReadNbtArgs args) {
        NbtCompound tag = args.getNbt();

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

    public void tick(World world, BlockPos pos, BlockState state, BaseEnergyTile blockEntity) {
        super.tick(world, pos, state, blockEntity);
        if (world.isClient()) return;

        // レッドストーン受信で無効
        if (WorldUtil.isReceivingRedstonePower(getWorld(), getPos())) {
            if (isActive())
                Scanner.setActive(false, getWorld(), getPos());
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
                    ItemStack stack = new ItemStack(Items.BLUEPRINT, getItems().get(0).getCount());
                    BlueprintUtil.writeNbt(stack, blocks);
                    getItems().set(1, stack);
                    getItems().set(0, ItemStack.EMPTY);
                    useEnergy(getEnergyCost());
                }

            }
            coolTimeBonus();
            coolTime = coolTime - getBasicSpeed();
            if (!isActive()) {
                Scanner.setActive(true, getWorld(), getPos());
            }
        } else if (isActive()) {
            Scanner.setActive(false, getWorld(), getPos());
        }
    }

    // blocks...スキャナーを基準とした相対的な座標
    public boolean tryScanning(Map<BlockPos, BlockState> blocks) {
        System.out.println("scanning");
        if (getWorld() == null || getWorld().isClient()) return false;
        if (pos1 == null || pos2 == null)
            return false;
        int procX;
        int procY;
        int procZ;
        for (procY = pos1.getY(); procY <= pos2.getY(); procY++) {
            for (procX = pos1.getX(); procX <= pos2.getX(); procX++) {
                for (procZ = pos1.getZ(); procZ <= pos2.getZ(); procZ++) {
                    BlockPos procPos = new BlockPos(procX, procY, procZ);
                    BlockState procState = getWorld().getBlockState(procPos);

                    if (procState.getBlock() == Blocks.AIR) continue;

                    blocks.put(new BlockPos(procX - pos1.getX(), procY - pos1.getY(), procZ - pos1.getZ()), procState);
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
