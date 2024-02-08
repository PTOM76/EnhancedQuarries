package ml.pkom.enhancedquarries.tile.base;

import ml.pkom.enhancedquarries.Items;
import ml.pkom.enhancedquarries.block.base.Scanner;
import ml.pkom.enhancedquarries.screen.ScannerScreenHandler;
import ml.pkom.enhancedquarries.util.BlueprintUtil;
import ml.pkom.mcpitanlibarch.api.gui.inventory.IInventory;
import ml.pkom.mcpitanlibarch.api.util.TextUtil;
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
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
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
    public long getBaseMaxPower() {
        return 5000;
    }

    // エネルギーの最大出力(不要なので0)
    public long getBaseMaxOutput() {
        return 0;
    }

    // エネルギーの最大入力
    public long getBaseMaxInput() {
        return 500;
    }

    // エネルギーの生産をするか？→false
    public boolean canProvideEnergy(final Direction direction) { return false; }

    // ----

    // NBT

    public void writeNbt(NbtCompound tag) {
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
        super.writeNbt(tag);
    }

    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

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
        // 1.--
        super.tick(world, pos, state, blockEntity);
        if (getWorld() == null || getWorld().isClient())
        {
            return;
        }
        // ----
        //BlockState state = getWorld().getBlockState(getPos());

        // レッドストーン受信で無効
        if (getWorld().isReceivingRedstonePower(getPos())) {
            if (isActive()) {
                Scanner.setActive(false, getWorld(), getPos());
            }
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
        if (getBaseMaxPower() / 1.125 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed() * 5;
        }
        if (getBaseMaxPower() / 1.25 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed() * 3;
        }
        if (getBaseMaxPower() / 3 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed() * 2;
        }
        if (getBaseMaxPower() / 5 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getBaseMaxPower() / 7 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getBaseMaxPower() / 10 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getBaseMaxPower() / 12 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getBaseMaxPower() / 15 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getBaseMaxPower() / 16 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getBaseMaxPower() / 20 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getBaseMaxPower() / 30 < getEnergy()) {
            coolTime = coolTime - getBasicSpeed();
        }
        if (getBaseMaxPower() / 40 < getEnergy()) {
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

    public ScannerTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void init() {
        /*
        int index = 0;
        SlotConfiguration.SlotIO output = new SlotConfiguration.SlotIO(SlotConfiguration.ExtractConfig.OUTPUT);
        SlotConfiguration.SlotIO input = new SlotConfiguration.SlotIO(SlotConfiguration.ExtractConfig.INPUT);
        SlotConfiguration slotConfig = new SlotConfiguration(getInventory());
        for (;index < getInventory().size();index++){
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.NORTH, input, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.SOUTH, input, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.EAST, input, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.WEST, input, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.UP, input, index));
            slotConfig.getSlotDetails(index).updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.DOWN, output, index));
            markDirty();
        }
        ((MachineBaseBlockEntityAccessor) this).setSlotConfiguration(slotConfig);
        
         */
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
